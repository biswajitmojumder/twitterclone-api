package com.fattech.twitterclone.repos;

import com.fattech.twitterclone.constants.ErrorCodes;
import com.fattech.twitterclone.utils.AppException;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

public abstract class BaseRedisRepo<T> {
    protected final RedisTemplate<String, Object> redisTemplate;

    protected BaseRedisRepo(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    protected abstract String keyPrefix();

    protected abstract Long lifetime();

    protected abstract TimeUnit lifetimeUnit();

    protected void saveByPrefixLessKey(String prefixLessKey, T value) {
        saveByKey(keyPrefix() + prefixLessKey, value);
    }

    protected void deleteByPrefixLessKey(String prefixLessKey) {
        deleteByKey(keyPrefix() + prefixLessKey);
    }

    protected T findByPrefixLessKey(String prefixLessKey) {
        return findByKey(keyPrefix() + prefixLessKey);
    }

    private void saveByKey(String key, T value) {
        try {
            redisTemplate.opsForValue().set(key, value, lifetime(), lifetimeUnit());
        } catch (RuntimeException e) {
            throw new AppException(ErrorCodes.REDIS_ERROR);
        }
    }

    private T findByKey(String key) {
        try {
            return (T) redisTemplate.opsForValue().get(key);
        } catch (RuntimeException e) {
            throw new AppException(ErrorCodes.REDIS_ERROR);
        }
    }

    private void deleteByKey(String key) {
        try {
            redisTemplate.delete(key);
        } catch (RuntimeException e) {
            throw new AppException(ErrorCodes.REDIS_ERROR);
        }
    }
}
