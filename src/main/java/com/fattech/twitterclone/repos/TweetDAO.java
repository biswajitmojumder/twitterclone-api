package com.fattech.twitterclone.repos;

import com.fattech.twitterclone.models.Tweet;

import java.util.List;

public interface TweetDAO {
    List<Tweet> getLimitedByPlayerIdsOlderThan(Long limit, List<Long> playerIds, Long olderThan);

    List<Tweet> getByTweetIds(List<Long> tweetIds);

    List<Tweet> getLimitedLatest(Long limit, Long time);

    List<Tweet> getLimitedHomeTweets(Long limit, List<Long> playerIds, Long olderThan);

    List<Long> getTweetIdsLimitedHomeTweets(Long limit, List<Long> playerIds, Long olderThan);
}
