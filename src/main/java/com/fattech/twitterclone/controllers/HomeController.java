package com.fattech.twitterclone.controllers;

import com.fattech.twitterclone.services.PageService;
import com.fattech.twitterclone.utils.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/home")
public class HomeController {
    private final PageService pageService;

    @Autowired
    public HomeController(PageService pageService) {
        this.pageService = pageService;
    }

    @GetMapping("/getHomeLatest")
    public ResponseEntity<Map<String, Object>> getHomePayloadLatest(@RequestHeader("TK") String token) {
        var res = pageService.getLatestHomeData(token);
        return ResponseHandler.wrapSuccessResponse(res);
    }

    @GetMapping("/getHome/{time}")
    public ResponseEntity<Map<String, Object>> getHomePayload(@RequestHeader("TK") String token,
                                                              @PathVariable Long time) {
        var res = pageService.getHomeData(token, time);
        return ResponseHandler.wrapSuccessResponse(res);
    }
}
