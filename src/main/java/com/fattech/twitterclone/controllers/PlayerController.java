package com.fattech.twitterclone.controllers;

import com.fattech.twitterclone.models.Player;
import com.fattech.twitterclone.repos.PlayerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/players")
public class PlayerController {
    private final PlayerRepo playerRepo;

    @Autowired
    public PlayerController(PlayerRepo playerRepo) {
        this.playerRepo = playerRepo;
    }

    @GetMapping("/")
    public List<Player> getAll(){
        return playerRepo.getAll();
    }

    @PostMapping("/")
    public Long add(){
        Player player = new Player();
        player.setUserName("ultraadmin");
        player.setFullName("new admin super");
        player.setEmail("newadmin@tw.com");
        player.setPassword("pwd123");
        player.setImageUrl("link");
        player.setCreatedAt(3L);
        player.setLastModified(4L);
        return playerRepo.save(player);
    }

    @PutMapping("/")
    public Long update(){
        Player player = new Player();
        player.setUserName("extraadmin");
        player.setFullName("new admin super");
        player.setEmail("newadmin@tw.com");
        player.setPassword("pwd123");
        player.setImageUrl("link");
        player.setCreatedAt(3L);
        player.setLastModified(4L);
        return playerRepo.update(player, 9L);
    }

    @DeleteMapping("/")
    public Long delete(){
        return playerRepo.delete(1L);
    }

    @GetMapping("/{id}")
    public Player getById(@PathVariable Long id){
        return playerRepo.getById(id);
    }
}
