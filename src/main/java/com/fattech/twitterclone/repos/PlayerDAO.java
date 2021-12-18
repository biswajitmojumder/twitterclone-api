package com.fattech.twitterclone.repos;

import com.fattech.twitterclone.models.Player;

public interface PlayerDAO {
    Player getByUserName(String userName);
}
