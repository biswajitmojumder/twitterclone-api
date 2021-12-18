package com.fattech.twitterclone.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AccessTokenUtilsTest {
    private final AccessTokenUtils accessTokenUtils;

    @Autowired
    AccessTokenUtilsTest(AccessTokenUtils accessTokenUtils) {
        this.accessTokenUtils = accessTokenUtils;
    }

    @Test
    void shouldSecretNotEmpty() {
        String secret = accessTokenUtils.getSecret();
        Assertions.assertTrue(secret.length() > 0);
    }

    @Test
    void shouldSecretMinimumSixCharacters() {
        String secret = accessTokenUtils.getSecret();
        Assertions.assertTrue(secret.length() > 6);
    }

    @Test
    void shouldTokenContainCorrectPlayerId() {
        Long samplePlayerId = 6969L;
        String token = accessTokenUtils.getToken(samplePlayerId);
        Long extractedPlayerId = Long.valueOf(accessTokenUtils.extractPlayerId(token));
        Assertions.assertEquals(samplePlayerId, extractedPlayerId);
    }
}