package com.fattech.twitterclone.utils;

import com.fattech.twitterclone.constants.ErrorCodes;

public class AppException extends RuntimeException {
    public AppException(ErrorCodes errorCodes){
        super(errorCodes.name());
    }
}
