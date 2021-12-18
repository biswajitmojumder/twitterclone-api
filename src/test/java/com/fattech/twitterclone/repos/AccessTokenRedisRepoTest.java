package com.fattech.twitterclone.repos;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Objects;

@SpringBootTest
class AccessTokenRedisRepoTest {
    private final AccessTokenRedisRepo accessTokenRedisRepo;

    @Autowired
    AccessTokenRedisRepoTest(AccessTokenRedisRepo accessTokenRedisRepo) {
        this.accessTokenRedisRepo = accessTokenRedisRepo;
    }

    @Test
    void savedTokenShouldExist() {
        Long playerId = 67L;
        String dummyToken = "testToken67";
        accessTokenRedisRepo.saveByPlayerId(playerId, dummyToken);
        String foundToken = accessTokenRedisRepo.findByPlayerId(playerId);
        Assertions.assertNotNull(foundToken);
    }

    @Test
    void shouldTokenLostAfterDelete() {
        Long playerId = 343L;
        String dummyToken = "testToken343";
        accessTokenRedisRepo.saveByPlayerId(playerId, dummyToken);
        String foundTokenAfterSaved = accessTokenRedisRepo.findByPlayerId(playerId);
        accessTokenRedisRepo.deleteByPlayerId(playerId);
        String foundTokenAfterDelete = accessTokenRedisRepo.findByPlayerId(playerId);
        Assertions.assertTrue(Objects.isNull(foundTokenAfterDelete) && !Objects.isNull(foundTokenAfterSaved));
    }

    @Test
    void shouldKeyPrefixNotEmpty() {
        String keyPrefix = accessTokenRedisRepo.keyPrefix();
        Assertions.assertTrue(keyPrefix.length()>0);
    }

    @Test
    void shouldLifetimeMoreThanZero() {
        Long lifetime = accessTokenRedisRepo.lifetime();
        Assertions.assertTrue(lifetime>0L);
    }
}