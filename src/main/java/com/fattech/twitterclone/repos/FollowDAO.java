package com.fattech.twitterclone.repos;

import com.fattech.twitterclone.models.Follow;

import java.util.List;

public interface FollowDAO {
    Follow getByPlayerIdFollowerId(Long playerId, Long followerId);

    List<Follow> getByFollowerId(Long followerId);

    List<Follow> getByPlayerIdsFollowerId(List<Long> playerIds, Long followerId);

    List<Long> getHomeFollowingPlayerIds(Long playerId);

    List<Long> getFollowerIdsByPlayerId(Long playerId);
}
