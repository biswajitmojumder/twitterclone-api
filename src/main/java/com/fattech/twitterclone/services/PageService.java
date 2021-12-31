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

import java.util.ArrayList;
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
//        Long playerId = Long.valueOf(accessTokenUtils.extractPlayerId(token));
//
//        Long limit = 20L;
//        List<Tweet> exploreTweet = tweetRepo.getLimitedLatest(limit, time);
//        List<Long> tweetIds = exploreTweet.stream().map(BaseModel::getId).collect(Collectors.toList());
//
//        List<Long> repliedRetweetIds = exploreTweet
//                .stream()
//                .filter(s -> (!s.getReplyOf().equals(0L) || !s.getRetweetOf().equals(0L)))
//                .map(s -> {
//                    if (!s.getReplyOf().equals(0L)) {
//                        return s.getReplyOf();
//                    } else {
//                        return s.getRetweetOf();
//                    }
//                }).collect(Collectors.toList());
//
//        List<Tweet> repliesRetweets = tweetRepo.getByTweetIds(repliedRetweetIds);
//        exploreTweet.addAll(repliesRetweets);
//
//        tweetIds.addAll(repliedRetweetIds);
//
//        List<Long> playerIds = exploreTweet.stream().map(Tweet::getPlayerId).collect(Collectors.toList());
//        List<Player> players = playerRepo.getByPlayerIds(playerIds);
//
//        List<Long> repliesRetweetsPlayers = repliesRetweets
//                .stream()
//                .map(Tweet::getPlayerId)
//                .collect(Collectors.toList());
//
//        List<Player> repliesPlayers = playerRepo.getByPlayerIds(repliesRetweetsPlayers);
//        players.addAll(repliesPlayers);
//
//        List<Reaction> tweetReactions = reactionRepo.getByTweetIdsPlayerId(tweetIds, playerId);
//
//        Payload payload = new Payload();
//        payload.setFollows(null);
//        payload.setReactions(tweetReactions.stream().collect(Collectors.toMap(Reaction::getTweetId, Function.identity())));
//        payload.setTweets(exploreTweet.stream().collect(Collectors.toMap(
//                Tweet::getId,
//                Function.identity(),
//                (t1, t2) -> t1
//        )));
//        payload.setPlayers(players.stream().collect(Collectors.toMap(
//                Player::getId,
//                s -> objectMapper.convertValue(s, PlayerGetDto.class),
//                (p1, p2) -> p1
//        )));

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
        List<Follow> followings = followRepo.getByFollowerId(playerId);
        List<Long> followingIds = followings.stream().map(Follow::getPlayerId).collect(Collectors.toList());
        followingIds.add(playerId);

        // get tweet latest
        Long resultLimit = 20L;
        // get latest tweets
        List<Tweet> latestFollowingTweets = tweetRepo.getLimitedByPlayerIdsOlderThan(resultLimit, followingIds, time);
        // get latest's replies/retweets
        List<Long> repliedRetweetedIds = latestFollowingTweets
                .stream()
                .filter(s -> (!s.getReplyOf().equals(0L) || !s.getRetweetOf().equals(0L)))
                .map(s -> {
                    if (!s.getReplyOf().equals(0L)) {
                        return s.getReplyOf();
                    } else {
                        return s.getRetweetOf();
                    }
                }).collect(Collectors.toList());
        List<Tweet> repliesRetweets = tweetRepo.getByTweetIds(repliedRetweetedIds);
        List<Tweet> tweetsPlaceHolder = new ArrayList<>(latestFollowingTweets);
        tweetsPlaceHolder.addAll(repliesRetweets);
        List<TweetGetDto> returnTweets = tweetsPlaceHolder.stream().map(tweet -> {
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
        List<Long> tweetsPlayerIds = tweetsPlaceHolder.stream().map(Tweet::getPlayerId).collect(Collectors.toList());
        List<PlayerGetDto> playersFromTweets = playerRepo
                .getByPlayerIds(tweetsPlayerIds)
                .stream()
                .map(s-> objectMapper.convertValue(s, PlayerGetDto.class))
                .collect(Collectors.toList());
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
