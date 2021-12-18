package com.fattech.twitterclone.repos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class AccessTokenRedisRepo extends BaseRedisRepo<String> {
    @Autowired
    protected AccessTokenRedisRepo(RedisTemplate<String, Object> redisTemplate) {
        super(redisTemplate);
    }

    @Override
    protected String keyPrefix() {
        return "TK$";
    }

    @Override
    protected Long lifetime() {
        return 2L;
    }

    @Override
    protected TimeUnit lifetimeUnit() {
        return TimeUnit.HOURS;
    }

    public void saveByPlayerId(Long playerId, String accessToken){
        saveByPrefixLessKey(playerId.toString(), accessToken);
    }

    public void deleteByPlayerId(Long playerId){
        deleteByPrefixLessKey(playerId.toString());
    }

    public String findByPlayerId(Long playerId){
        return findByPrefixLessKey(playerId.toString());
    }
}
