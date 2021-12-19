package com.fattech.twitterclone.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fattech.twitterclone.constants.ErrorCodes;
import com.fattech.twitterclone.models.Tweet;
import com.fattech.twitterclone.models.dtos.TweetDraftDto;
import com.fattech.twitterclone.repos.TweetRepo;
import com.fattech.twitterclone.utils.AccessTokenUtils;
import com.fattech.twitterclone.utils.AppException;
import com.fattech.twitterclone.utils.DateTimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class TweetService {
    private final TweetRepo tweetRepo;
    private final ObjectMapper objectMapper;
    private final DateTimeUtils dateTimeUtils;
    private final AccessTokenUtils accessTokenUtils;

    Logger logger = LoggerFactory.getLogger(TweetService.class);

    @Autowired
    public TweetService(TweetRepo tweetRepo,
                        ObjectMapper objectMapper,
                        DateTimeUtils dateTimeUtils,
                        AccessTokenUtils accessTokenUtils) {
        this.tweetRepo = tweetRepo;
        this.objectMapper = objectMapper;
        this.dateTimeUtils = dateTimeUtils;
        this.accessTokenUtils = accessTokenUtils;
    }

    public Tweet postNewTweet(TweetDraftDto tweetDraftDto, String token) {
        Long playerId = Long.valueOf(accessTokenUtils.extractPlayerId(token));
        Tweet tweetDraft = objectMapper.convertValue(tweetDraftDto, Tweet.class);
        tweetDraft.setPlayerId(playerId);
        tweetDraft.setReplyOf(0L);
        tweetDraft.setRetweetOf(0L);
        final Long now = dateTimeUtils.getUnixNow();
        tweetDraft.setCreatedAt(now);
        tweetDraft.setLastModified(now);
        Long generatedId = tweetRepo.save(tweetDraft);
        return tweetRepo.getById(generatedId);
    }

    public Tweet editTweet(TweetDraftDto tweetDraftDto, Long tweetId, String token) {
        Long playerId = Long.valueOf(accessTokenUtils.extractPlayerId(token));
        Tweet foundTweet = tweetRepo.getById(tweetId);

        if (!Objects.isNull(foundTweet)) {
            var editorIsTweetOwner = (foundTweet.getPlayerId().equals(playerId));
            if (editorIsTweetOwner) {
                foundTweet.setMessage(tweetDraftDto.getMessage());
                foundTweet.setImageUrl(tweetDraftDto.getImageUrl());
                tweetRepo.update(foundTweet, tweetId);
                return foundTweet;
            }
            logger.warn("Editor is not owner of tweet " + tweetId + " , will throw bad request");
            throw new AppException(ErrorCodes.BAD_REQUEST);
        }
        logger.warn("Tweet with " + tweetId + " Not Found, will throw bad request exception!");
        throw new AppException(ErrorCodes.BAD_REQUEST);
    }

    public Map<Long, Tweet> replyTweet(TweetDraftDto tweetDraftDto,
                                       String token,
                                       Long repliedTweetId) {
        Tweet foundTweet = tweetRepo.getById(repliedTweetId);
        if (!Objects.isNull(foundTweet)) {
            Map<Long, Tweet> tweetsList = new HashMap<>();

            Long playerId = Long.valueOf(accessTokenUtils.extractPlayerId(token));
            Tweet tweetDraft = objectMapper.convertValue(tweetDraftDto, Tweet.class);
            tweetDraft.setPlayerId(playerId);
            tweetDraft.setReplyOf(repliedTweetId);
            tweetDraft.setRetweetOf(0L);
            final Long now = dateTimeUtils.getUnixNow();
            tweetDraft.setCreatedAt(now);
            tweetDraft.setLastModified(now);
            Long generatedId = tweetRepo.save(tweetDraft);
            Tweet reply = tweetRepo.getById(generatedId);

            tweetsList.put(generatedId, reply);
            tweetsList.put(repliedTweetId, foundTweet);

            return tweetsList;
        }
        logger.warn("Target tweet for reply doesn't exist. Will throw Bad Request Exception");
        throw new AppException(ErrorCodes.BAD_REQUEST);
    }

    public Map<Long, Tweet> retweetTweet(Long retweetedTweetId, String token) {
        Tweet foundTweet = tweetRepo.getById(retweetedTweetId);
        if (!Objects.isNull(foundTweet)) {
            Map<Long, Tweet> tweetsList = new HashMap<>();

            Long playerId = Long.valueOf(accessTokenUtils.extractPlayerId(token));
            Tweet tweetDraft = new Tweet();
            tweetDraft.setPlayerId(playerId);
            tweetDraft.setMessage("");
            tweetDraft.setImageUrl("");
            tweetDraft.setReplyOf(0L);
            tweetDraft.setRetweetOf(retweetedTweetId);
            final Long now = dateTimeUtils.getUnixNow();
            tweetDraft.setCreatedAt(now);
            tweetDraft.setLastModified(now);
            Long generatedId = tweetRepo.save(tweetDraft);
            Tweet retweet = tweetRepo.getById(generatedId);

            tweetsList.put(retweetedTweetId, foundTweet);
            tweetsList.put(generatedId, retweet);

            return tweetsList;
        }
        logger.warn("Target tweet to be retweeted doesn't exist. Will throw Bad Request Exception");
        throw new AppException(ErrorCodes.BAD_REQUEST);
    }
}
