package com.fattech.twitterclone.models;

import com.fattech.twitterclone.models.dtos.PlayerGetDto;
import com.fattech.twitterclone.models.dtos.TweetGetDto;

import java.util.Map;

public class Payload {
    private Map<Long, TweetGetDto> tweets;
    private Map<Long, Reaction> reactions;
    private Map<Long, PlayerGetDto> players;
    private Map<Long, Follow> follows;

    public Payload() {
    }

    public Payload(Map<Long, TweetGetDto> tweets,
                   Map<Long, Reaction> reactions,
                   Map<Long, PlayerGetDto> players,
                   Map<Long, Follow> follows) {
        this.tweets = tweets;
        this.reactions = reactions;
        this.players = players;
        this.follows = follows;
    }

    public Map<Long, Follow> getFollows() {
        return follows;
    }

    public void setFollows(Map<Long, Follow> follows) {
        this.follows = follows;
    }

    public Map<Long, TweetGetDto> getTweets() {
        return tweets;
    }

    public void setTweets(Map<Long, TweetGetDto> tweets) {
        this.tweets = tweets;
    }

    public Map<Long, Reaction> getReactions() {
        return reactions;
    }

    public void setReactions(Map<Long, Reaction> reactions) {
        this.reactions = reactions;
    }

    public Map<Long, PlayerGetDto> getPlayers() {
        return players;
    }

    public void setPlayers(Map<Long, PlayerGetDto> players) {
        this.players = players;
    }
}
