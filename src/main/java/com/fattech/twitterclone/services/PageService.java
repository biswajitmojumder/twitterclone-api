package com.fattech.twitterclone.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fattech.twitterclone.models.*;
import com.fattech.twitterclone.models.dtos.PlayerGetDto;
import com.fattech.twitterclone.models.dtos.TweetGetDto;
import com.fattech.twitterclone.repos.*;
import com.fattech.twitterclone.utils.AccessTokenUtils;
import com.fattech.twitterclone.utils.DateTimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PageService {
    private final FollowRepo followRepo;
    private final AccessTokenUtils accessTokenUtils;
    private final TweetRepo tweetRepo;
    private final DateTimeUtils dateTimeUtils;
    private final PlayerRepo playerRepo;
    private final ReactionRepo reactionRepo;
    private final ObjectMapper objectMapper;
    private final TagRepo tagRepo;

    Logger logger = LoggerFactory.getLogger(PageService.class);

    @Autowired
    public PageService(FollowRepo followRepo,
                       AccessTokenUtils accessTokenUtils,
                       TweetRepo tweetRepo,
                       DateTimeUtils dateTimeUtils,
                       PlayerRepo playerRepo,
                       ReactionRepo reactionRepo,
                       ObjectMapper objectMapper,
                       TagRepo tagRepo) {
        this.followRepo = followRepo;
        this.accessTokenUtils = accessTokenUtils;
        this.tweetRepo = tweetRepo;
        this.dateTimeUtils = dateTimeUtils;
        this.playerRepo = playerRepo;
        this.reactionRepo = reactionRepo;
        this.objectMapper = objectMapper;
        this.tagRepo = tagRepo;
    }

    public Payload getExploreLatest(String token) {
        final Long now = dateTimeUtils.getUnixNow();
        return getExplore(now, token);
    }

    public Payload getExplore(Long time, String token) {
        return null;
    }

    public Payload getLatestHomeData(String token) {
        final Long now = dateTimeUtils.getUnixNow();
        return getHomeData(token, now);
    }

    public Payload getHomeData(String token, Long time) {
        // get requester playerId
        Long playerId = Long.valueOf(accessTokenUtils.extractPlayerId(token));

        // get followings + self playerId
        List<Long> followingIds = followRepo.getHomeFollowingPlayerIds(playerId);

        // get tweet latest
        Long resultLimit = 20L;
        List<Tweet> homeTweets = tweetRepo.getLimitedHomeTweets(resultLimit, followingIds, time);

        List<TweetGetDto> returnTweets = homeTweets.stream().map(tweet -> {
            TweetGetDto tweetGetDto = objectMapper.convertValue(tweet, TweetGetDto.class);
            List<String> tweetTags = tagRepo.getTagNamesByTweetId(tweet.getId());
            tweetGetDto.setTags(tweetTags);
            return tweetGetDto;
        }).collect(Collectors.toList());
        Map<Long, TweetGetDto> tweets = returnTweets.stream().collect(Collectors.toMap(
                BaseModel::getId,
                Function.identity(),
                (t1, t2) -> t1
        ));

        // get all Reactions
        List<Long> tweetIds = returnTweets
                .stream()
                .map(BaseModel::getId)
                .collect(Collectors.toList());
        List<Reaction> reactionsList = reactionRepo.getByTweetIdsPlayerId(tweetIds, playerId);
        Map<Long, Reaction> reactions = reactionsList.stream().collect(Collectors.toMap(
                Reaction::getTweetId,
                Function.identity(),
                (r1, r2) -> r1
        ));

        // get players from all tweets
        List<Long> tweetsPlayerIds = homeTweets.stream().map(Tweet::getPlayerId).collect(Collectors.toList());
        tweetsPlayerIds.add(playerId);
        List<PlayerGetDto> playersFromTweets = playerRepo.getByPlayerIds(tweetsPlayerIds);
        Map<Long, PlayerGetDto> players = playersFromTweets.stream().collect(Collectors.toMap(
                PlayerGetDto::getId,
                Function.identity(),
                (p1, p2) -> p1
        ));

        // get follows from all returned tweets
        List<Follow> followsFromTweets = followRepo.getByPlayerIdsFollowerId(tweetsPlayerIds, playerId);
        Map<Long, Follow> follows = followsFromTweets.stream()
                .collect(Collectors.toMap(
                        Follow::getPlayerId,
                        Function.identity(),
                        (f1, f2) -> f1
                ));

        return new Payload(tweets, reactions, players, follows);
    }
}
