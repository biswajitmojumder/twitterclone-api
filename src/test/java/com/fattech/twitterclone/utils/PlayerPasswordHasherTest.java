package com.fattech.twitterclone.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PlayerPasswordHasherTest {
    private final PlayerPasswordHasher playerPasswordHasher;

    @Autowired
    PlayerPasswordHasherTest(PlayerPasswordHasher playerPasswordHasher) {
        this.playerPasswordHasher = playerPasswordHasher;
    }

    @Test
    void hashedPasswordMustBeDifferent() {
        String testPassword = "pwd123";
        String hashedPassword = playerPasswordHasher.getEncoded(testPassword);
        Assertions.assertNotEquals(hashedPassword, testPassword);
    }

    @Test
    void returnVerfiedIfPasswordCorrect() {
        String testPassword = "pwd123";
        String hashedPassword = playerPasswordHasher.getEncoded(testPassword);
        Boolean isVerified = playerPasswordHasher.getIsVerified(testPassword, hashedPassword);
        Assertions.assertTrue(isVerified);
    }

    @Test
    void returnRejectionIfPasswordIncorrect() {
        String correctPassword = "pwd123";
        String hashedPassword = playerPasswordHasher.getEncoded(correctPassword);
        String incorrectPassword = "pwd1234";
        Boolean isVerified = playerPasswordHasher.getIsVerified(incorrectPassword, hashedPassword);
        Assertions.assertFalse(isVerified);
    }
}