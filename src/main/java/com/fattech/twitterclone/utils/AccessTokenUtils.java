package com.fattech.twitterclone.utils;

import com.fattech.twitterclone.constants.AccessTokenClaimTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AccessTokenUtils extends BaseTokenUtils {
    private final DateTimeUtils dateTimeUtils;

    @Autowired
    public AccessTokenUtils(DateTimeUtils dateTimeUtils) {
        this.dateTimeUtils = dateTimeUtils;
    }

    @Override
    protected String getSecret() {
        return "C8PvlQKTvdFcvAqsDIP52L0IKNV8lcC5Z4nAe7Kr";
    }

    public String getToken(Long playerId) {
        return generateToken(generateTokenClaims(playerId));
    }

    private Map<String, Object> generateTokenClaims(Long playerId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(AccessTokenClaimTypes.PLAYER_ID.name(), playerId.toString());
        Long genesisTime = dateTimeUtils.getUnixNow();
        claims.put(AccessTokenClaimTypes.CREATED_AT.name(), genesisTime.toString());
        return claims;
    }

    public String extractPlayerId(String token) {
        return getClaim(token, AccessTokenClaimTypes.PLAYER_ID.name());
    }
}
