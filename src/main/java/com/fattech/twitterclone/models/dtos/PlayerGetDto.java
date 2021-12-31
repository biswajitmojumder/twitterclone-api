package com.fattech.twitterclone.models.dtos;

public class PlayerGetDto {
    private Long id;
    private String userName;
    private String fullName;
    private String email;
    private String imageUrl;

    public PlayerGetDto() {
    }

    public PlayerGetDto(Long id,
                        String userName,
                        String fullName,
                        String email,
                        String imageUrl) {
        this.id = id;
        this.userName = userName;
        this.fullName = fullName;
        this.email = email;
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
