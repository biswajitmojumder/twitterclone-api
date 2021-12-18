package com.fattech.twitterclone.middlewares;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fattech.twitterclone.utils.BaseFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
@Order(1)
public class RequestLogger extends BaseFilter {
    Logger logger = LoggerFactory.getLogger(RequestLogger.class);

    @Autowired
    protected RequestLogger(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        var requestURI = request.getRequestURI();
        logger.info("REQUEST-HIT @" + requestURI);
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
