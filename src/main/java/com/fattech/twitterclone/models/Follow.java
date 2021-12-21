package com.fattech.twitterclone.models;

import javax.validation.constraints.NotNull;

public class Follow extends BaseModel {
    @NotNull
    private Long playerId;

    @NotNull
    private Long followerId;

    public Follow() {
    }

    public Follow(Long id,
                  Long createdAt,
                  Long lastModified,
                  Long playerId,
                  Long followerId) {
        super(id, createdAt, lastModified);
        this.playerId = playerId;
        this.followerId = followerId;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public Long getFollowerId() {
        return followerId;
    }

    public void setFollowerId(Long followerId) {
        this.followerId = followerId;
    }
}
