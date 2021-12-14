package com.fattech.twitterclone.models;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Player extends BaseModel {
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

    @NotEmpty
    @NotNull
    private String password;

    @NotEmpty
    @NotNull
    private String imageUrl;

    public Player() {
    }

    public Player(Long id,
                  Long createdAt,
                  Long lastModified,
                  String userName,
                  String fullName,
                  String email,
                  String password,
                  String imageUrl) {
        super(id, createdAt, lastModified);
        this.userName = userName;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.imageUrl = imageUrl;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
