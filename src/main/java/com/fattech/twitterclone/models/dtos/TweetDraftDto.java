package com.fattech.twitterclone.models.dtos;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class TweetDraftDto {
    @NotNull
    @Size(min = 1, max = 200)
    private String message;

    @NotNull
    @Size(min = 1, max = 500)
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
