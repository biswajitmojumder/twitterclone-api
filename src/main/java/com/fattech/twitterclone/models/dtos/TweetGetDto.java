package com.fattech.twitterclone.models.dtos;

import com.fattech.twitterclone.models.Tweet;

import java.util.List;

public class TweetGetDto extends Tweet {
    private List<String> tags;

    public TweetGetDto() {
    }

    public TweetGetDto(Long id,
                       Long createdAt,
                       Long lastModified,
                       Long playerId,
                       String message,
                       String imageUrl,
                       Long replyOf,
                       Long retweetOf,
                       List<String> tags) {
        super(
                id,
                createdAt,
                lastModified,
                playerId,
                message,
                imageUrl,
                replyOf,
                retweetOf
        );
        this.tags = tags;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
