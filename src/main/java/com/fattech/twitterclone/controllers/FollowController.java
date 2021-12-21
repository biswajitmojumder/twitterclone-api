package com.fattech.twitterclone.controllers;

import com.fattech.twitterclone.services.FollowService;
import com.fattech.twitterclone.utils.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/follow")
public class FollowController {
    private final FollowService followService;

    @Autowired
    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    @GetMapping("/start/{targetPlayerId}")
    public ResponseEntity<Map<String, Object>> follow(@PathVariable Long targetPlayerId,
                                                      @RequestHeader("TK") String token) {
        var res = followService.followPlayer(targetPlayerId, token);
        return ResponseHandler.wrapSuccessResponse(res);
    }

    @GetMapping("/end/{targetPlayerId}")
    public ResponseEntity<Map<String, Object>> unfollow(@PathVariable Long targetPlayerId,
                                                        @RequestHeader("TK") String token) {
        var res = followService.unfollowPlayer(targetPlayerId, token);
        return ResponseHandler.wrapSuccessResponse(res);
    }
}
