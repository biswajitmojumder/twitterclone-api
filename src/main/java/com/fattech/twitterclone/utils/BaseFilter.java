package com.fattech.twitterclone.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fattech.twitterclone.constants.ErrorCodes;

import javax.servlet.Filter;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class BaseFilter implements Filter {
    private final ObjectMapper objectMapper;

    protected BaseFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    protected void onError(ServletResponse servletResponse, ErrorCodes errorCode) {
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        httpServletResponse.setContentType("application/json");
        httpServletResponse.setCharacterEncoding("UTF-8");
        var response = ResponseHandler.wrapFailResponse(errorCode.name());
        try {
            httpServletResponse
                    .getWriter()
                    .write(objectMapper.writeValueAsString(response.getBody()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
