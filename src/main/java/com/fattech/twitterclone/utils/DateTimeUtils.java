package com.fattech.twitterclone.utils;

import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class DateTimeUtils {
    public Long getUnixNow() {
        Date now = new Date();
        return now.getTime();
    }

    public Long getUnixAfterMinutes(Long minutes) {
        Long now = getUnixNow();
        Date afterInterval = new Date(now + minutes * 60 * 1000);
        return afterInterval.getTime();
    }
}
