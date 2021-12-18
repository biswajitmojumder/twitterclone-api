package com.fattech.twitterclone.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public abstract class BaseHasher {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    protected BaseHasher(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    protected abstract String getSalt();

    public Boolean getIsVerified(String text, String hashedText) {
        return bCryptPasswordEncoder.matches(text + getSalt(), hashedText);
    }

    public String getEncoded(String text) {
        return bCryptPasswordEncoder.encode(text + getSalt());
    }
}
