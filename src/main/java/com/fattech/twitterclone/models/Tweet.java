package com.fattech.twitterclone.models;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Tweet extends BaseModel {
    @NotNull
    @Size(min = 1)
    private Long playerId;
    private String message;
    private String imageUrl;
    private Long replyOf;
    private Long retweetOf;

    public Tweet() {
    }

    public Tweet(Long id,
                 Long createdAt,
                 Long lastModified,
                 Long playerId,
                 String message,
                 String imageUrl,
                 Long replyOf,
                 Long retweetOf) {
        super(id, createdAt, lastModified);
        this.playerId = playerId;
        this.message = message;
        this.imageUrl = imageUrl;
        this.replyOf = replyOf;
        this.retweetOf = retweetOf;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
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

    public Long getReplyOf() {
        return replyOf;
    }

    public void setReplyOf(Long replyOf) {
        this.replyOf = replyOf;
    }

    public Long getRetweetOf() {
        return retweetOf;
    }

    public void setRetweetOf(Long retweetOf) {
        this.retweetOf = retweetOf;
    }
}
