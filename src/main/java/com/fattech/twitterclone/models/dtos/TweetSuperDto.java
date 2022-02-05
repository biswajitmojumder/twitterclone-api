package com.fattech.twitterclone.models.dtos;

import com.fattech.twitterclone.models.Tweet;

public class TweetSuperDto extends Tweet {
    private Long numOfReplies;
    private Long numOfRetweets;
    private Long numOfLikes;

    public TweetSuperDto() {
    }

    public TweetSuperDto(Long id,
                         Long createdAt,
                         Long lastModified,
                         Long playerId,
                         String message,
                         String imageUrl,
                         Long replyOf,
                         Long retweetOf,
                         Long numOfReplies,
                         Long numOfRetweets,
                         Long numOfLikes) {
        super(id, createdAt, lastModified, playerId, message, imageUrl, replyOf, retweetOf);
        this.numOfReplies = numOfReplies;
        this.numOfRetweets = numOfRetweets;
        this.numOfLikes = numOfLikes;
    }

    public Long getNumOfReplies() {
        return numOfReplies;
    }

    public void setNumOfReplies(Long numOfReplies) {
        this.numOfReplies = numOfReplies;
    }

    public Long getNumOfRetweets() {
        return numOfRetweets;
    }

    public void setNumOfRetweets(Long numOfRetweets) {
        this.numOfRetweets = numOfRetweets;
    }

    public Long getNumOfLikes() {
        return numOfLikes;
    }

    public void setNumOfLikes(Long numOfLikes) {
        this.numOfLikes = numOfLikes;
    }
}
