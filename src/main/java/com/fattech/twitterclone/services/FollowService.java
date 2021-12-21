package com.fattech.twitterclone.services;

import com.fattech.twitterclone.constants.ErrorCodes;
import com.fattech.twitterclone.models.Follow;
import com.fattech.twitterclone.models.Player;
import com.fattech.twitterclone.repos.FollowRepo;
import com.fattech.twitterclone.repos.PlayerRepo;
import com.fattech.twitterclone.utils.AccessTokenUtils;
import com.fattech.twitterclone.utils.AppException;
import com.fattech.twitterclone.utils.DateTimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class FollowService {
    private final FollowRepo followRepo;
    private final PlayerRepo playerRepo;
    private final AccessTokenUtils accessTokenUtils;
    private final DateTimeUtils dateTimeUtils;

    Logger logger = LoggerFactory.getLogger(FollowService.class);

    @Autowired
    public FollowService(FollowRepo followRepo,
                         PlayerRepo playerRepo,
                         AccessTokenUtils accessTokenUtils,
                         DateTimeUtils dateTimeUtils) {
        this.followRepo = followRepo;
        this.playerRepo = playerRepo;
        this.accessTokenUtils = accessTokenUtils;
        this.dateTimeUtils = dateTimeUtils;
    }

    public Follow followPlayer(Long targetPlayerId, String token) {
        Player foundPlayer = playerRepo.getById(targetPlayerId);
        if (!Objects.isNull(foundPlayer)) {
            Long playerId = Long.valueOf(accessTokenUtils.extractPlayerId(token));
            Follow foundFollow = followRepo.getByPlayerIdFollowerId(targetPlayerId, playerId);
            if (Objects.isNull(foundFollow)) {
                Follow follow = new Follow();
                follow.setPlayerId(targetPlayerId);
                follow.setFollowerId(playerId);
                final Long now = dateTimeUtils.getUnixNow();
                follow.setCreatedAt(now);
                follow.setLastModified(now);

                Long generatedId = followRepo.save(follow);
                return followRepo.getById(generatedId);
            }
            logger.warn("Already following, will throw bad req!");
            throw new AppException(ErrorCodes.BAD_REQUEST);
        }
        logger.warn("Target player to follow is not found, Will return bad request");
        throw new AppException(ErrorCodes.BAD_REQUEST);
    }

    public Long unfollowPlayer(Long targetPlayerId, String token) {
        Player foundPlayer = playerRepo.getById(targetPlayerId);
        if (!Objects.isNull(foundPlayer)) {
            Long playerId = Long.valueOf(accessTokenUtils.extractPlayerId(token));
            Follow foundFollow = followRepo.getByPlayerIdFollowerId(targetPlayerId, playerId);
            if (!Objects.isNull(foundFollow)) {
                return followRepo.delete(foundFollow.getId());
            }
            logger.warn("Follow object with playerId " + targetPlayerId + " & followerId " + playerId + " doesn't exist");
            throw new AppException(ErrorCodes.BAD_REQUEST);
        }
        logger.warn("Target player not found, will throw bad request");
        throw new AppException(ErrorCodes.BAD_REQUEST);
    }
}
