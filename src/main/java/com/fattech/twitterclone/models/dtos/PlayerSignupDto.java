package com.fattech.twitterclone.models.dtos;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class PlayerSignupDto {
    @NotEmpty
    @NotNull
    @Size(min = 6, max = 50)
    private String userName;

    @NotEmpty
    @NotNull
    @Size(min = 6, max = 50)
    private String fullName;

    @NotEmpty
    @NotNull
    @Size(min = 5, max = 50)
    private String email;

    @NotEmpty
    @NotNull
    private String password;

    public PlayerSignupDto() {
    }

    public PlayerSignupDto(String userName,
                           String fullName,
                           String email,
                           String password) {
        this.userName = userName;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
