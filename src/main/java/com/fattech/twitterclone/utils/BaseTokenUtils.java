package com.fattech.twitterclone.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Map;

public abstract class BaseTokenUtils {
    Algorithm algorithm = Algorithm.HMAC256(getSecret());

    protected abstract String getSecret();

    protected String generateToken(Map<String, Object> payloadClaims){
        return JWT
                .create()
                .withPayload(payloadClaims)
                .sign(algorithm);
    }

    public Boolean getIsValid(String token){
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        jwtVerifier.verify(token);
        return true;
    }

    private DecodedJWT getDecodedJwt(String token){
        return JWT.decode(token);
    }

    protected String getClaim(String token, String claimType){
        return getDecodedJwt(token).getClaim(claimType).asString();
    }
}
