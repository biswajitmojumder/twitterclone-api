package com.fattech.twitterclone.models.dtos;

public class TweetDraftDto {
    private String message;
    private String imageUrl;

    public TweetDraftDto() {
    }

    public TweetDraftDto(String message,
                         String imageUrl) {
        this.message = message;
        this.imageUrl = imageUrl;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
