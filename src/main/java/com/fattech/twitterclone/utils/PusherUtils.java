package com.fattech.twitterclone.utils;

import com.fattech.twitterclone.constants.PusherEventNames;
import com.pusher.rest.Pusher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PusherUtils {
    Logger logger = LoggerFactory.getLogger(PusherUtils.class);

    private final Pusher pusherInstance;

    @Autowired
    public PusherUtils(Pusher pusherInstance) {
        this.pusherInstance = pusherInstance;
    }

    public void pushToMany(List<String> channels, PusherEventNames eventName, Object data) {
        pusherInstance.trigger(channels, eventName.name(), data);
        logger.info("Pusher activity!");
    }
}
