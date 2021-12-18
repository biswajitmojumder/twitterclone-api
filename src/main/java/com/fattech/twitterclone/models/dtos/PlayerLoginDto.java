package com.fattech.twitterclone.models.dtos;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class PlayerLoginDto {
    @NotEmpty
    @NotNull
    @Size(min = 6, max = 50)
    private String userName;

    @NotEmpty
    @NotNull
    private String password;

    public PlayerLoginDto() {
    }

    public PlayerLoginDto(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
