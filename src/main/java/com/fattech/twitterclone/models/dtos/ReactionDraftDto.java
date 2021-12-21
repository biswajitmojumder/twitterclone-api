package com.fattech.twitterclone.models.dtos;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ReactionDraftDto {
    @NotNull
    private Long tweetId;

    @NotNull
    @NotEmpty
    private String reactionType;

    public ReactionDraftDto() {
    }

    public ReactionDraftDto(Long tweetId, String reactionType) {
        this.tweetId = tweetId;
        this.reactionType = reactionType;
    }

    public Long getTweetId() {
        return tweetId;
    }

    public void setTweetId(Long tweetId) {
        this.tweetId = tweetId;
    }

    public String getReactionType() {
        return reactionType;
    }

    public void setReactionType(String reactionType) {
        this.reactionType = reactionType;
    }
}
