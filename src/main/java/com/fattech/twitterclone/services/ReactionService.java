package com.fattech.twitterclone.services;

import com.fattech.twitterclone.constants.ErrorCodes;
import com.fattech.twitterclone.constants.ReactionTypes;
import com.fattech.twitterclone.models.Reaction;
import com.fattech.twitterclone.models.Tweet;
import com.fattech.twitterclone.repos.ReactionRepo;
import com.fattech.twitterclone.repos.TweetRepo;
import com.fattech.twitterclone.utils.AccessTokenUtils;
import com.fattech.twitterclone.utils.AppException;
import com.fattech.twitterclone.utils.DateTimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ReactionService {
    private final ReactionRepo reactionRepo;
    private final TweetRepo tweetRepo;
    private final AccessTokenUtils accessTokenUtils;
    private final DateTimeUtils dateTimeUtils;

    Logger logger = LoggerFactory.getLogger(ReactionService.class);

    @Autowired
    public ReactionService(ReactionRepo reactionRepo,
                           TweetRepo tweetRepo,
                           AccessTokenUtils accessTokenUtils,
                           DateTimeUtils dateTimeUtils) {
        this.reactionRepo = reactionRepo;
        this.tweetRepo = tweetRepo;
        this.accessTokenUtils = accessTokenUtils;
        this.dateTimeUtils = dateTimeUtils;
    }

    public Reaction likeTweet(Long tweetId, String token) {
        return addReaction(tweetId, ReactionTypes.LIKE, token);
    }

    public Long removeReaction(Long reactionId, String token) {
        Reaction foundReaction = reactionRepo.getById(reactionId);
        var reactionExists = !Objects.isNull(foundReaction);
        if (reactionExists) {
            Long playerId = Long.valueOf(accessTokenUtils.extractPlayerId(token));
            var playerIsReactionOwner = foundReaction.getPlayerId().equals(playerId);
            if (playerIsReactionOwner) {
                return reactionRepo.delete(reactionId);
            }
            logger.warn("Player is not the owner of reaction, not authorized for deletion. Will throw bad request");
            throw new AppException(ErrorCodes.BAD_REQUEST);
        }
        logger.warn("Reaction doesn't exist, will throw bad request");
        throw new AppException(ErrorCodes.BAD_REQUEST);
    }

    public Reaction bookmarkTweet(Long tweetId, String token) {
        return addReaction(tweetId, ReactionTypes.BOOKMARK, token);
    }

    private Reaction addReaction(Long tweetId, ReactionTypes reactionType, String token) {
        Tweet foundTweet = tweetRepo.getById(tweetId);
        var tweetExists = !Objects.isNull(foundTweet);
        if (tweetExists) {
            Long playerId = Long.valueOf(accessTokenUtils.extractPlayerId(token));
            Reaction playerTweetReaction = reactionRepo.getPlayerTweetReaction(
                    tweetId,
                    playerId,
                    reactionType.name()
            );
            var reactionAlreadyExists = !Objects.isNull(playerTweetReaction);
            if (!reactionAlreadyExists) {
                Reaction reaction = new Reaction();
                reaction.setPlayerId(playerId);
                reaction.setTweetId(tweetId);
                reaction.setReactionType(reactionType.name());
                final Long now = dateTimeUtils.getUnixNow();
                reaction.setCreatedAt(now);
                reaction.setLastModified(now);

                Long newReactionId = reactionRepo.save(reaction);
                return reactionRepo.getById(newReactionId);
            } else {
                logger.warn("Tweet already reacted by player " + playerId + " , will throw bad req!");
                throw new AppException(ErrorCodes.BAD_REQUEST);
            }
        } else {
            logger.warn("Tweet doesn't exist, will throw bad request");
            throw new AppException(ErrorCodes.BAD_REQUEST);
        }
    }
}
