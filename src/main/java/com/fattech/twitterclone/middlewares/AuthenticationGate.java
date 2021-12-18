package com.fattech.twitterclone.middlewares;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fattech.twitterclone.constants.ErrorCodes;
import com.fattech.twitterclone.constants.UnprotectedURIs;
import com.fattech.twitterclone.services.PlayerService;
import com.fattech.twitterclone.utils.AppException;
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
import java.util.Objects;

@Component
@Order(2)
public class AuthenticationGate extends BaseFilter {
    private final PlayerService playerService;

    Logger logger = LoggerFactory.getLogger(AuthenticationGate.class);

    @Autowired
    protected AuthenticationGate(ObjectMapper objectMapper,
                                 PlayerService playerService) {
        super(objectMapper);
        this.playerService = playerService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        var pathURI = httpServletRequest.getRequestURI();
        var token = httpServletRequest.getHeader("TK");
        if (Boolean.TRUE.equals(getIsProtected(pathURI))) {
            logger.info("Protected Endpoint [HIT]");
            if (!Objects.isNull(token)) {
                try {
                    Boolean isAuthenticated = playerService.getAuthenticated(token);
                    if (Boolean.TRUE.equals(isAuthenticated)) {
                        filterChain.doFilter(servletRequest, servletResponse);
                    } else {
                        onError(servletResponse, ErrorCodes.INVALID_CREDENTIALS);
                    }
                } catch (JWTVerificationException | JWTCreationException | AppException e) {
                    if (e.getMessage().equals(ErrorCodes.REDIS_ERROR.name())) {
                        onError(servletResponse, ErrorCodes.REDIS_ERROR);
                    } else {
                        onError(servletResponse, ErrorCodes.INVALID_CREDENTIALS);
                    }
                }
            } else {
                onError(servletResponse, ErrorCodes.INVALID_CREDENTIALS);
            }
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    private Boolean getIsProtected(String pathReqURI) {
        boolean isProtected = true;
        for (UnprotectedURIs path : UnprotectedURIs.values()) {
            if (pathReqURI.startsWith("/api" + path.getPath())) {
                isProtected = false;
                break;
            }
        }
        return isProtected;
    }
}
