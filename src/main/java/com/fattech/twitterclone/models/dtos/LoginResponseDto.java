package com.fattech.twitterclone.models.dtos;

public class LoginResponseDto {
    private String token;
    private Long playerId;

    public LoginResponseDto() {
    }

    public LoginResponseDto(String token, Long playerId) {
        this.token = token;
        this.playerId = playerId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }
}
