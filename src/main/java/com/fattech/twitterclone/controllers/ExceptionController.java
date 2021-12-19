package com.fattech.twitterclone.controllers;

import com.fattech.twitterclone.constants.ErrorCodes;
import com.fattech.twitterclone.utils.AppException;
import com.fattech.twitterclone.utils.ResponseHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Map;

@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler(value = {AppException.class})
    public ResponseEntity<Map<String, Object>> handleAppException(AppException e){
        return throwFailure(e.getMessage());
    }

    @ExceptionHandler(value = {
            MethodArgumentNotValidException.class,
            MethodArgumentTypeMismatchException.class
    })
    public ResponseEntity<Map<String, Object>> handleBadRequest(){
        return throwFailure(ErrorCodes.BAD_REQUEST.name());
    }

    private ResponseEntity<Map<String, Object>> throwFailure(String errorCode){
        return ResponseHandler.wrapFailResponse(errorCode);
    }
}
