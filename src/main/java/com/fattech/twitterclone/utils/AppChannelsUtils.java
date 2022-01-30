package com.fattech.twitterclone.utils;

import com.fattech.twitterclone.constants.AppChannelPrefixes;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppChannelsUtils {
    public String getPlayerHomeChannel(Long playerId) {
        return String.format("%s@%d", AppChannelPrefixes.HOME_FEED_NTF.name(), playerId);
    }

    public List<String> getPlayerHomeChannels(List<Long> playerIds) {
        return playerIds
                .stream()
                .map(this::getPlayerHomeChannel)
                .collect(Collectors.toList());
    }
}
