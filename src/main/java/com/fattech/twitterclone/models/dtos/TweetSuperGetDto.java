package com.fattech.twitterclone.models.dtos;

import java.util.List;

public class TweetSuperGetDto extends TweetSuperDto {
    private List<String> tags;

    public TweetSuperGetDto() {
    }

//    public TweetSuperGetDto(TweetSuperDto tweetSuperDto, List<String> tags) {
//        super(
//                tweetSuperDto.getId(),
//                tweetSuperDto.getCreatedAt(),
//                tweetSuperDto.getLastModified(),
//                tweetSuperDto.getPlayerId(),
//                tweetSuperDto.getMessage(),
//                tweetSuperDto.getImageUrl(),
//                tweetSuperDto.getReplyOf(),
//                tweetSuperDto.getRetweetOf(),
//                tweetSuperDto.getNumOfReplies(),
//                tweetSuperDto.getNumOfRetweets(),
//                tweetSuperDto.getNumOfLikes()
//        );
//        this.tags = tags;
//    }

    public TweetSuperGetDto(Long id, Long createdAt, Long lastModified, Long playerId, String message, String imageUrl, Long replyOf, Long retweetOf, Long numOfReplies, Long numOfRetweets, Long numOfLikes, List<String> tags) {
        super(id, createdAt, lastModified, playerId, message, imageUrl, replyOf, retweetOf, numOfReplies, numOfRetweets, numOfLikes);
        this.tags = tags;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
