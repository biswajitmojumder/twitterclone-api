package com.fattech.twitterclone.repos;

import com.fattech.twitterclone.models.Reaction;

public interface ReactionDAO {
    Reaction getPlayerTweetReaction(Long tweetId, Long playerId, String reactionType);
}
