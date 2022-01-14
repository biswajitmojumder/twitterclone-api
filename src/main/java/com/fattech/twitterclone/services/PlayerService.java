package com.fattech.twitterclone.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fattech.twitterclone.constants.ErrorCodes;
import com.fattech.twitterclone.models.Player;
import com.fattech.twitterclone.models.dtos.*;
import com.fattech.twitterclone.repos.AccessTokenRedisRepo;
import com.fattech.twitterclone.repos.PlayerRepo;
import com.fattech.twitterclone.utils.AccessTokenUtils;
import com.fattech.twitterclone.utils.AppException;
import com.fattech.twitterclone.utils.DateTimeUtils;
import com.fattech.twitterclone.utils.PlayerPasswordHasher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class PlayerService {
    private final PlayerRepo playerRepo;
    private final ObjectMapper objectMapper;
    private final DateTimeUtils dateTimeUtils;
    private final PlayerPasswordHasher playerPasswordHasher;
    private final AccessTokenUtils accessTokenUtils;
    private final AccessTokenRedisRepo accessTokenRedisRepo;

    @Autowired
    public PlayerService(PlayerRepo playerRepo,
                         ObjectMapper objectMapper,
                         DateTimeUtils dateTimeUtils,
                         PlayerPasswordHasher playerPasswordHasher,
                         AccessTokenUtils accessTokenUtils,
                         AccessTokenRedisRepo accessTokenRedisRepo) {
        this.playerRepo = playerRepo;
        this.objectMapper = objectMapper;
        this.dateTimeUtils = dateTimeUtils;
        this.playerPasswordHasher = playerPasswordHasher;
        this.accessTokenUtils = accessTokenUtils;
        this.accessTokenRedisRepo = accessTokenRedisRepo;
    }

    public PlayerGetDto signUp(PlayerSignupDto playerSignupDto) {
        Player player = objectMapper.convertValue(playerSignupDto, Player.class);
        final Long time = dateTimeUtils.getUnixNow();
        player.setCreatedAt(time);
        player.setLastModified(time);
        player.setPassword(playerPasswordHasher.getEncoded(playerSignupDto.getPassword()));
        player.setImageUrl("https://dewcare.co.za/wp-content/uploads/2020/10/blank-avatar.png");
        var generatedId = playerRepo.save(player);
        Player savedPlayer = playerRepo.getById(generatedId);
        return objectMapper.convertValue(savedPlayer, PlayerGetDto.class);
    }

    public LoginResponseDto login(PlayerLoginDto playerLoginDto) {
        var userName = playerLoginDto.getUserName();
        Player foundPlayer = playerRepo.getByUserName(userName);
        if (Objects.isNull(foundPlayer)) {
            throw new AppException(ErrorCodes.INVALID_CREDENTIALS);
        } else {
            var isCredentialValid = playerPasswordHasher.getIsVerified(
                    playerLoginDto.getPassword(),
                    foundPlayer.getPassword()
            );
            if (Boolean.TRUE.equals(isCredentialValid)) {
                var playerId = foundPlayer.getId();
                var token = accessTokenUtils.getToken(playerId);
                accessTokenRedisRepo.saveByPlayerId(playerId, token);
                return new LoginResponseDto(token, playerId);
            } else {
                throw new AppException(ErrorCodes.INVALID_CREDENTIALS);
            }
        }
    }

    public PlayerGetDto update(PlayerUpdateDto playerUpdateDto, String token) {
        Long id = Long.valueOf(accessTokenUtils.extractPlayerId(token));
        var foundPlayer = playerRepo.getById(id);
        foundPlayer.setUserName(playerUpdateDto.getUserName());
        foundPlayer.setFullName(playerUpdateDto.getFullName());
        foundPlayer.setEmail(playerUpdateDto.getEmail());
        foundPlayer.setLastModified(dateTimeUtils.getUnixNow());
        playerRepo.update(foundPlayer, id);
        return objectMapper.convertValue(foundPlayer, PlayerGetDto.class);
    }

    public Boolean getAuthenticated(String token) {
        var userId = accessTokenUtils.extractPlayerId(token);
        var isValid = accessTokenUtils.getIsValid(token);
        var foundToken = accessTokenRedisRepo.findByPlayerId(Long.valueOf(userId));
        if (!Objects.isNull(foundToken) && isValid) {
            accessTokenRedisRepo.saveByPlayerId(Long.valueOf(userId), token);
            return true;
        }
        return false;
    }
}
