package com.fattech.twitterclone.repos;

import com.fattech.twitterclone.models.Player;

import java.util.List;

public interface PlayerDAO {
    Player getByUserName(String userName);

    List<Player> getByPlayerIds(List<Long> playerIds);
}
