package com.fattech.twitterclone.controllers;

import com.fattech.twitterclone.services.PageService;
import com.fattech.twitterclone.utils.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/explore")
public class ExploreController {
    private final PageService pageService;

    @Autowired
    public ExploreController(PageService pageService) {
        this.pageService = pageService;
    }

    @GetMapping("/getLatest")
    public ResponseEntity<Map<String, Object>> getLatestExplore(@RequestHeader("TK") String token) {
        var res = pageService.getExploreLatest(token);
        return ResponseHandler.wrapSuccessResponse(res);
    }
}
