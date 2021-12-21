package com.fattech.twitterclone.controllers;

import com.fattech.twitterclone.models.dtos.TweetDraftDto;
import com.fattech.twitterclone.services.ReactionService;
import com.fattech.twitterclone.services.TweetService;
import com.fattech.twitterclone.utils.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/tweet")
public class TweetController {
    private final TweetService tweetService;
    private final ReactionService reactionService;

    @Autowired
    public TweetController(TweetService tweetService,
                           ReactionService reactionService) {
        this.tweetService = tweetService;
        this.reactionService = reactionService;
    }

    @PostMapping("/post")
    public ResponseEntity<Map<String, Object>> postTweet(@Valid @RequestBody TweetDraftDto tweetDraftDto,
                                                         @RequestHeader("TK") String token) {
        var res = tweetService.postNewTweet(tweetDraftDto, token);
        return ResponseHandler.wrapSuccessResponse(res);
    }

    @PostMapping("/edit/{tweetId}")
    public ResponseEntity<Map<String, Object>> editTweet(@Valid @RequestBody TweetDraftDto tweetDraftDto,
                                                         @PathVariable Long tweetId,
                                                         @RequestHeader("TK") String token) {
        var res = tweetService.editTweet(tweetDraftDto, tweetId, token);
        return ResponseHandler.wrapSuccessResponse(res);
    }

    @PostMapping("/reply/{tweetId}")
    public ResponseEntity<Map<String, Object>> replyTweet(@Valid @RequestBody TweetDraftDto tweetDraftDto,
                                                          @RequestHeader("TK") String token,
                                                          @PathVariable Long tweetId) {
        var res = tweetService.replyTweet(tweetDraftDto, token, tweetId);
        return ResponseHandler.wrapSuccessResponse(res);
    }

    @GetMapping("/retweet/{tweetId}")
    public ResponseEntity<Map<String, Object>> retweetTweet(@PathVariable Long tweetId,
                                                            @RequestHeader("TK") String token) {
        var res = tweetService.retweetTweet(tweetId, token);
        return ResponseHandler.wrapSuccessResponse(res);
    }

    @GetMapping("/like/{tweetId}")
    public ResponseEntity<Map<String, Object>> likeTweet(@PathVariable Long tweetId,
                                                         @RequestHeader("TK") String token) {
        var res = reactionService.likeTweet(tweetId, token);
        return ResponseHandler.wrapSuccessResponse(res);
    }

    @GetMapping("/bookmark/{tweetId}")
    public ResponseEntity<Map<String, Object>> bookmarkTweet(@PathVariable Long tweetId,
                                                             @RequestHeader("TK") String token) {
        var res = reactionService.bookmarkTweet(tweetId, token);
        return ResponseHandler.wrapSuccessResponse(res);
    }
}
