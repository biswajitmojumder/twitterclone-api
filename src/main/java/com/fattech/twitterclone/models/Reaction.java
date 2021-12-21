package com.fattech.twitterclone.models;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class Reaction extends BaseModel {
    @NotNull
    private Long tweetId;

    @NotNull
    private Long playerId;

    @NotNull
    @NotEmpty
    private String reactionType;

    public Reaction() {
    }

    public Reaction(Long id,
                    Long createdAt,
                    Long lastModified,
                    Long tweetId,
                    Long playerId,
                    String reactionType) {
        super(id, createdAt, lastModified);
        this.tweetId = tweetId;
        this.playerId = playerId;
        this.reactionType = reactionType;
    }

    public Long getTweetId() {
        return tweetId;
    }

    public void setTweetId(Long tweetId) {
        this.tweetId = tweetId;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public String getReactionType() {
        return reactionType;
    }

    public void setReactionType(String reactionType) {
        this.reactionType = reactionType;
    }
}
