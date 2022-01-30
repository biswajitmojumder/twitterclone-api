package com.fattech.twitterclone.repos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class PlayerFollowerIdsRedisRepo extends BaseRedisRepo<List<Long>> {
    @Autowired
    protected PlayerFollowerIdsRedisRepo(RedisTemplate<String, Object> redisTemplate) {
        super(redisTemplate);
    }

    @Override
    protected String keyPrefix() {
        return "PFID$";
    }

    @Override
    protected Long lifetime() {
        return 3L;
    }

    @Override
    protected TimeUnit lifetimeUnit() {
        return TimeUnit.HOURS;
    }

    public void saveByPlayerId(Long playerId, List<Long> playerFollowerIds) {
        saveByPrefixLessKey(playerId.toString(), playerFollowerIds);
    }

    public void deleteByPlayerId(Long playerId) {
        deleteByPrefixLessKey(playerId.toString());
    }

    public List<Long> findByPlayerId(Long playerId) {
        return findByPrefixLessKey(playerId.toString());
    }
}
