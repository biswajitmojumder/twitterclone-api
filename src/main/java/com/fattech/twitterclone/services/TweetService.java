package com.fattech.twitterclone.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fattech.twitterclone.constants.ErrorCodes;
import com.fattech.twitterclone.constants.PusherEventNames;
import com.fattech.twitterclone.models.Tag;
import com.fattech.twitterclone.models.Tweet;
import com.fattech.twitterclone.models.dtos.TweetDraftDto;
import com.fattech.twitterclone.models.dtos.TweetGetDto;
import com.fattech.twitterclone.repos.TagRepo;
import com.fattech.twitterclone.repos.TweetRepo;
import com.fattech.twitterclone.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TweetService {
    private final TweetRepo tweetRepo;
    private final TagRepo tagRepo;
    private final ObjectMapper objectMapper;
    private final DateTimeUtils dateTimeUtils;
    private final AccessTokenUtils accessTokenUtils;
    private final PusherUtils pusherUtils;
    private final AppChannelsUtils appChannelsUtils;
    private final CacheableQueryService cacheableQueryService;

    Logger logger = LoggerFactory.getLogger(TweetService.class);

    @Autowired
    public TweetService(TweetRepo tweetRepo,
                        TagRepo tagRepo,
                        ObjectMapper objectMapper,
                        DateTimeUtils dateTimeUtils,
                        AccessTokenUtils accessTokenUtils,
                        PusherUtils pusherUtils,
                        AppChannelsUtils appChannelsUtils,
                        CacheableQueryService cacheableQueryService) {
        this.tweetRepo = tweetRepo;
        this.tagRepo = tagRepo;
        this.objectMapper = objectMapper;
        this.dateTimeUtils = dateTimeUtils;
        this.accessTokenUtils = accessTokenUtils;
        this.pusherUtils = pusherUtils;
        this.appChannelsUtils = appChannelsUtils;
        this.cacheableQueryService = cacheableQueryService;
    }

    public TweetGetDto postNewTweet(TweetDraftDto tweetDraftDto, String token) {
        Long playerId = Long.valueOf(accessTokenUtils.extractPlayerId(token));
        Tweet tweetDraft = objectMapper.convertValue(tweetDraftDto, Tweet.class);
        tweetDraft.setPlayerId(playerId);
        tweetDraft.setReplyOf(0L);
        tweetDraft.setRetweetOf(0L);
        final Long now = dateTimeUtils.getUnixNow();
        tweetDraft.setCreatedAt(now);
        tweetDraft.setLastModified(now);
        Long generatedId = tweetRepo.save(tweetDraft);
        List<String> tags = saveTags(tweetDraftDto.getTags(), generatedId);

        var savedTweet = tweetRepo.getById(generatedId);
        var returnTweet = objectMapper.convertValue(savedTweet, TweetGetDto.class);
        returnTweet.setTags(tags);

        incrementFollowersHomePageFeedNtf(playerId);

        return returnTweet;
    }

    private List<String> saveTags(List<String> tags, Long tweetId) {
        tags.forEach(tagName -> {
            Tag tag = new Tag();
            Long now = dateTimeUtils.getUnixNow();
            tag.setTweetId(tweetId);
            tag.setTagName(tagName);
            tag.setCreatedAt(now);
            tag.setLastModified(now);

            tagRepo.save(tag);
        });
        return tagRepo.getTagNamesByTweetId(tweetId);
    }

    public TweetGetDto editTweet(TweetDraftDto tweetDraftDto, Long tweetId, String token) {
        Long playerId = Long.valueOf(accessTokenUtils.extractPlayerId(token));
        Tweet foundTweet = tweetRepo.getById(tweetId);

        if (!Objects.isNull(foundTweet)) {
            var editorIsTweetOwner = (foundTweet.getPlayerId().equals(playerId));
            if (editorIsTweetOwner) {
                foundTweet.setMessage(tweetDraftDto.getMessage());
                foundTweet.setImageUrl(tweetDraftDto.getImageUrl());
                tweetRepo.update(foundTweet, tweetId);

                var initTags = tagRepo.getTagNamesByTweetId(tweetId);
                var tagNamesForDeletion = initTags
                        .stream()
                        .filter(tagName -> !tweetDraftDto.getTags().contains(tagName))
                        .collect(Collectors.toList());
                tagRepo.deleteByTagNamesTweetId(tagNamesForDeletion, tweetId);

                var tagNamesForAddition = tweetDraftDto
                        .getTags()
                        .stream()
                        .filter(tagName -> !initTags.contains(tagName))
                        .collect(Collectors.toList());
                var list = tagNamesForAddition.stream().map(tagName -> {
                    Tag tag = new Tag();
                    tag.setTweetId(tweetId);
                    tag.setTagName(tagName);
                    Long now = dateTimeUtils.getUnixNow();
                    tag.setCreatedAt(now);
                    tag.setLastModified(now);

                    return tag;
                }).collect(Collectors.toList());
                tagRepo.saveAll(list, tweetId);

                TweetGetDto returnTweet = objectMapper.convertValue(foundTweet, TweetGetDto.class);
                returnTweet.setTags(tweetDraftDto.getTags());

                return returnTweet;
            }
            logger.warn("Editor is not owner of tweet " + tweetId + " , will throw bad request");
            throw new AppException(ErrorCodes.BAD_REQUEST);
        }
        logger.warn("Tweet with " + tweetId + " Not Found, will throw bad request exception!");
        throw new AppException(ErrorCodes.BAD_REQUEST);
    }

    public Map<Long, TweetGetDto> replyTweet(TweetDraftDto tweetDraftDto,
                                       String token,
                                       Long repliedTweetId) {
        Tweet foundTweet = tweetRepo.getById(repliedTweetId);
        if (!Objects.isNull(foundTweet)) {
            Map<Long, TweetGetDto> tweetsList = new HashMap<>();

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

            var replyTags = saveTags(tweetDraftDto.getTags(), generatedId);

            TweetGetDto returnReply = objectMapper.convertValue(reply, TweetGetDto.class);
            returnReply.setTags(replyTags);

            TweetGetDto targetTweet = objectMapper.convertValue(foundTweet, TweetGetDto.class);
            var targetTags = tagRepo.getTagNamesByTweetId(repliedTweetId);
            targetTweet.setTags(targetTags);

            tweetsList.put(generatedId, returnReply);
            tweetsList.put(repliedTweetId, targetTweet);

            incrementFollowersHomePageFeedNtf(playerId);

            return tweetsList;
        }
        logger.warn("Target tweet for reply doesn't exist. Will throw Bad Request Exception");
        throw new AppException(ErrorCodes.BAD_REQUEST);
    }

    public Map<Long, TweetGetDto> retweetTweet(Long retweetedTweetId, String token) {
        Tweet foundTweet = tweetRepo.getById(retweetedTweetId);
        if (!Objects.isNull(foundTweet)) {
            Map<Long, TweetGetDto> tweetsList = new HashMap<>();

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

            TweetGetDto targetTweet = objectMapper.convertValue(foundTweet, TweetGetDto.class);
            var targetTags = tagRepo.getTagNamesByTweetId(retweetedTweetId);
            targetTweet.setTags(targetTags);

            TweetGetDto returnRetweet = objectMapper.convertValue(retweet, TweetGetDto.class);
            returnRetweet.setTags(Collections.emptyList());

            tweetsList.put(retweetedTweetId, targetTweet);
            tweetsList.put(generatedId, returnRetweet);

            incrementFollowersHomePageFeedNtf(playerId);

            return tweetsList;
        }
        logger.warn("Target tweet to be retweeted doesn't exist. Will throw Bad Request Exception");
        throw new AppException(ErrorCodes.BAD_REQUEST);
    }

    private void incrementFollowersHomePageFeedNtf(Long playerId) {
        List<Long> followerIds = cacheableQueryService.getPlayerFollowerIds(playerId);
        var followerChannels = appChannelsUtils.getPlayerHomeChannels(followerIds);
        final Long recordedTime = dateTimeUtils.getUnixNow();
        pusherUtils.pushToMany(followerChannels, PusherEventNames.INC_HOME_FEED_NTF, recordedTime);
    }
}
