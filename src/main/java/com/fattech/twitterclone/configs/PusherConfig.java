package com.fattech.twitterclone.configs;

import com.pusher.rest.Pusher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PusherConfig {
    @Value("${twitterclone.pusher.appId}")
    private String pusherAppId;

    @Value("${twitterclone.pusher.key}")
    private String pusherKey;

    @Value("${twitterclone.pusher.secret}")
    private String pusherSecret;

    @Value("${twitterclone.pusher.cluster}")
    private String pusherCluster;

    @Bean
    public Pusher pusherInstance() {
        Pusher pusher = new Pusher(pusherAppId, pusherKey, pusherSecret);
        pusher.setCluster(pusherCluster);
        pusher.setEncrypted(false);
        return pusher;
    }
}
