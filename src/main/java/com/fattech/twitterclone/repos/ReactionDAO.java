package com.fattech.twitterclone.repos;

import com.fattech.twitterclone.models.Reaction;

import java.util.List;

public interface ReactionDAO {
    Reaction getPlayerTweetReaction(Long tweetId, Long playerId, String reactionType);

    List<Reaction> getByTweetIdsPlayerId(List<Long> tweetIds, Long playerId);
}
