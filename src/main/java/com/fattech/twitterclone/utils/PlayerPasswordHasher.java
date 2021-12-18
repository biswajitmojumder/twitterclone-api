package com.fattech.twitterclone.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PlayerPasswordHasher extends BaseHasher {
    @Autowired
    protected PlayerPasswordHasher(BCryptPasswordEncoder bCryptPasswordEncoder) {
        super(bCryptPasswordEncoder);
    }

    @Override
    protected String getSalt() {
        return "TLK6e1zWyrQX97OJyccH";
    }
}
