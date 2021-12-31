package com.fattech.twitterclone.models;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Tag extends BaseModel {
    @NotNull
    private Long tweetId;

    @NotNull
    @NotEmpty
    @Size(max = 25)
    private String tagName;

    public Tag() {
    }

    public Tag(Long id,
               Long createdAt,
               Long lastModified,
               Long tweetId,
               String tagName) {
        super(id, createdAt, lastModified);
        this.tweetId = tweetId;
        this.tagName = tagName;
    }

    public Long getTweetId() {
        return tweetId;
    }

    public void setTweetId(Long tweetId) {
        this.tweetId = tweetId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}
