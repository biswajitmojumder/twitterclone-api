package com.fattech.twitterclone.models.dtos;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class PlayerUpdateDto {
    @NotEmpty
    @NotNull
    @Size(min = 6, max = 50)
    private String userName;

    @NotNull
    @NotEmpty
    @Size(min = 6, max = 50)
    private String fullName;

    @NotNull
    @NotEmpty
    @Size(min = 5, max = 50)
    private String email;

    public PlayerUpdateDto() {
    }

    public PlayerUpdateDto(String userName, String fullName, String email) {
        this.userName = userName;
        this.fullName = fullName;
        this.email = email;
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
}
