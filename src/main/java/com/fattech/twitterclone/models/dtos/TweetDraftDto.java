package com.fattech.twitterclone.models.dtos;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class TweetDraftDto {
    @NotNull
    @Size(min = 1, max = 200)
    private String message;

    @NotNull
    @Size(max = 500)
    private String imageUrl;

    @NotNull
    private List<String> tags;

    public TweetDraftDto() {
    }

    public TweetDraftDto(String message, String imageUrl, List<String> tags) {
        this.message = message;
        this.imageUrl = imageUrl;
        this.tags = tags;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
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
