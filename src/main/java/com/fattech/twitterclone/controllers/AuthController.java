package com.fattech.twitterclone.controllers;

import com.fattech.twitterclone.models.dtos.PlayerLoginDto;
import com.fattech.twitterclone.models.dtos.PlayerSignupDto;
import com.fattech.twitterclone.models.dtos.PlayerUpdateDto;
import com.fattech.twitterclone.services.PlayerService;
import com.fattech.twitterclone.utils.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final PlayerService playerService;

    @Autowired
    public AuthController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PostMapping("/signup")
    public ResponseEntity<Map<String, Object>> signUp(@Valid @RequestBody PlayerSignupDto playerSignupDto) {
        var res = playerService.signUp(playerSignupDto);
        return ResponseHandler.wrapSuccessResponse(res);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody PlayerLoginDto playerLoginDto) {
        var res = playerService.login(playerLoginDto);
        return ResponseHandler.wrapSuccessResponse(res);
    }

    @PostMapping("/update")
    public ResponseEntity<Map<String, Object>> update(@Valid @RequestBody PlayerUpdateDto playerUpdateDto,
                                                      @RequestHeader("TK") String token) {
        var res = playerService.update(playerUpdateDto, token);
        return ResponseHandler.wrapSuccessResponse(res);
    }
}
