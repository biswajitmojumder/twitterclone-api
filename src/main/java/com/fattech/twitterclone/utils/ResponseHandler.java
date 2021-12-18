package com.fattech.twitterclone.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ResponseHandler {
    private static ResponseEntity<Map<String, Object>> wrapResponse(String errorCode, Object result) {
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("errorCode", errorCode);
        resMap.put("result", result);
        return new ResponseEntity<>(resMap, HttpStatus.OK);
    }

    public static ResponseEntity<Map<String, Object>> wrapSuccessResponse(Object result) {
        return wrapResponse(null, result);
    }

    public static ResponseEntity<Map<String, Object>> wrapFailResponse(String errorCode) {
        return wrapResponse(errorCode, null);
    }
}
