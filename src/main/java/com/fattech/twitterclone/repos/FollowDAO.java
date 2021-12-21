package com.fattech.twitterclone.repos;

import com.fattech.twitterclone.models.Follow;

public interface FollowDAO {
    Follow getByPlayerIdFollowerId(Long playerId, Long followerId);
}
