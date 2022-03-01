package com.fattech.twitterclone.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fattech.twitterclone.constants.ErrorCodes;
import com.fattech.twitterclone.models.*;
import com.fattech.twitterclone.models.dtos.PlayerGetDto;
import com.fattech.twitterclone.models.dtos.TweetGetDto;
import com.fattech.twitterclone.models.dtos.TweetSuperDto;
import com.fattech.twitterclone.models.dtos.TweetSuperGetDto;
import com.fattech.twitterclone.repos.*;
import com.fattech.twitterclone.utils.AccessTokenUtils;
import com.fattech.twitterclone.utils.AppException;
import com.fattech.twitterclone.utils.DateTimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
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
    private final CacheableQueryService cacheableQueryService;

    Logger logger = LoggerFactory.getLogger(PageService.class);

    @Autowired
    public PageService(FollowRepo followRepo,
                       AccessTokenUtils accessTokenUtils,
                       TweetRepo tweetRepo,
                       DateTimeUtils dateTimeUtils,
                       PlayerRepo playerRepo,
                       ReactionRepo reactionRepo,
                       ObjectMapper objectMapper,
                       TagRepo tagRepo,
                       CacheableQueryService cacheableQueryService) {
        this.followRepo = followRepo;
        this.accessTokenUtils = accessTokenUtils;
        this.tweetRepo = tweetRepo;
        this.dateTimeUtils = dateTimeUtils;
        this.playerRepo = playerRepo;
        this.reactionRepo = reactionRepo;
        this.objectMapper = objectMapper;
        this.tagRepo = tagRepo;
        this.cacheableQueryService = cacheableQueryService;
    }

    public ResponsePayload getExploreLatest(String token) {
        final Long now = dateTimeUtils.getUnixNow();
        return getExplore(now, token);
    }

    public ResponsePayload getExplore(Long time, String token) {
        return null;
    }

    public ResponsePayload getLatestHomeData(String token) {
        final Long now = dateTimeUtils.getUnixNow();
        return getHomeData(token, now);
    }

    public ResponsePayload getHomeData(String token, Long time) {
        // get requester playerId
        Long playerId = Long.valueOf(accessTokenUtils.extractPlayerId(token));

        // get followings + self playerId
        List<Long> followingIds = cacheableQueryService.getHomeFollowingPlayerIds(playerId);

        // get tweet latest
        Long resultLimit = 20L;
        List<Tweet> homeTweets = tweetRepo.getLimitedHomeTweets(resultLimit, followingIds, time);

        // get home feed tweetIds
        List<Long> feedTweetIds = tweetRepo.getTweetIdsLimitedHomeTweets(resultLimit, followingIds, time);
        Map<Long, Boolean> feedTwtIds = feedTweetIds.stream().collect(Collectors.toMap(
                Function.identity(),
                s -> true
        ));

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
                Reaction::getId,
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

        return new ResponsePayload(tweets, reactions, players, follows, feedTwtIds);
    }

    public ResponsePayload getRecommendedPlayers(String token) {
        Long playerId = Long.valueOf(accessTokenUtils.extractPlayerId(token));
        List<PlayerGetDto> recommendedPlayers = playerRepo.getRecommendedPlayers(playerId);
        Map<Long, PlayerGetDto> playersMap = recommendedPlayers.stream().collect(Collectors.toMap(
                PlayerGetDto::getId,
                Function.identity()
        ));
        var result = new ResponsePayload();
        result.setPlayers(playersMap);
        return result;
    }

    public ResponsePayload getSuperTweet(Long tweetId, String token) {
        Long playerId = Long.valueOf(accessTokenUtils.extractPlayerId(token));
        Tweet foundTweet = tweetRepo.getById(tweetId);

        if (!Objects.isNull(foundTweet)) {
            TweetSuperDto foundSuperTweet = tweetRepo.getSuperTweet(tweetId);
            List<String> tags = tagRepo.getTagNamesByTweetId(tweetId);
            TweetSuperGetDto foundSuperGetDto = objectMapper.convertValue(foundSuperTweet, TweetSuperGetDto.class);
            TweetGetDto foundGetDto = objectMapper.convertValue(foundTweet, TweetGetDto.class);
            foundSuperGetDto.setTags(tags);
            foundGetDto.setTags(tags);

            var result = new ResponsePayload();
            Map<Long, TweetSuperGetDto> superTweets = new HashMap<>();
            Map<Long, TweetGetDto> tweets = new HashMap<>();
            tweets.put(tweetId, foundGetDto);
            superTweets.put(tweetId, foundSuperGetDto);
            result.setSuperTweets(superTweets);
            result.setTweets(tweets);

            List<Tweet> listOfReplies = tweetRepo.getReplies(tweetId);
            List<TweetGetDto> listOfRepliesDto = listOfReplies.stream().map((reply) -> {
                TweetGetDto replyDto = objectMapper.convertValue(reply, TweetGetDto.class);
                List<String> replyTags = tagRepo.getTagNamesByTweetId(reply.getId());
                replyDto.setTags(replyTags);
                return replyDto;
            }).collect(Collectors.toList());
            listOfRepliesDto.forEach((replyDto) -> {
                tweets.put(replyDto.getId(), replyDto);
            });

            List<Long> playerIdsFromReplies = listOfReplies
                    .stream().map(Tweet::getPlayerId)
                    .collect(Collectors.toList());
            playerIdsFromReplies.add(foundTweet.getPlayerId());
            List<PlayerGetDto> listOfPlayersFromRepo = playerRepo.getByPlayerIds(playerIdsFromReplies);
            Map<Long, PlayerGetDto> players = listOfPlayersFromRepo
                    .stream().collect(Collectors.toMap(PlayerGetDto::getId, Function.identity()));
            List<Follow> followList = followRepo.getByPlayerIdsFollowerId(playerIdsFromReplies, playerId);
            Map<Long, Follow> follows = followList
                    .stream().collect(Collectors.toMap(Follow::getPlayerId, Function.identity()));

            List<Long> tweetIds = new ArrayList<>(tweets.keySet());
            List<Reaction> listOfReactions = reactionRepo.getByTweetIdsPlayerId(tweetIds, playerId);
            Map<Long, Reaction> reactions = listOfReactions
                    .stream().collect(Collectors.toMap(Reaction::getTweetId, Function.identity()));

            result.setPlayers(players);
            result.setReactions(reactions);
            result.setFollows(follows);
            result.setFeedIds(Collections.EMPTY_MAP);

            return result;
        }
        logger.warn(String.format("Tweet with id %d doesn't exist, will throw badRequest error!", tweetId));
        throw new AppException(ErrorCodes.BAD_REQUEST);
    }
}
