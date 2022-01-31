package com.fattech.twitterclone.repos;

import com.fattech.twitterclone.models.Player;
import com.fattech.twitterclone.models.dtos.PlayerGetDto;

import java.util.List;

public interface PlayerDAO {
    Player getByUserName(String userName);

    List<PlayerGetDto> getByPlayerIds(List<Long> playerIds);

    List<PlayerGetDto> getRecommendedPlayers(Long playerId);
}
