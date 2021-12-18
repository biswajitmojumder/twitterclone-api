package com.fattech.twitterclone.repos;

import com.fattech.twitterclone.models.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.Random;

@SpringBootTest
class PlayerRepoTest {

    private final PlayerRepo playerRepo;

    @Autowired
    PlayerRepoTest(PlayerRepo playerRepo) {
        this.playerRepo = playerRepo;
    }

    private Player createNewUniquePlayer() {
        Long time = new Date().getTime();
        int randomNumber = new Random().nextInt(200);
        String userName = "TestUserName-"+time+"-"+randomNumber; // unique username
        Player player = new Player();
        player.setUserName(userName);
        player.setFullName("Test User Name");
        player.setImageUrl("someLink");
        player.setEmail("testUserName@twitterclone.com");
        player.setPassword("testPassword");
        player.setCreatedAt(time);
        player.setLastModified(time);

        return player;
    }

    @Test
    void savePlayerWithNoId() {
        Player player = createNewUniquePlayer();
        Long generatedId = playerRepo.save(player);
        Assertions.assertTrue(generatedId>0);
    }

    @Test
    void savePlayerWithId() {
        Player player = createNewUniquePlayer();
        Long intendedId = -3L;
        player.setId(intendedId);
        Long newId = playerRepo.save(player);
        Assertions.assertNotEquals(newId, intendedId);
    }

    @Test
    void update() {
        Player player = createNewUniquePlayer();
        Long newId = playerRepo.save(player);
        Player foundPlayer = playerRepo.getById(newId);
        String updatedFullName = "Updated Full Name";
        foundPlayer.setFullName(updatedFullName);
        playerRepo.update(foundPlayer, newId);
        Player updatedPlayer = playerRepo.getById(newId);
        Assertions.assertEquals(updatedPlayer.getFullName(), updatedFullName);
    }

    @Test
    void delete() {
        Player player = createNewUniquePlayer();
        Long newPlayerId = playerRepo.save(player);
        playerRepo.delete(newPlayerId);
        Player foundPlayer = playerRepo.getById(newPlayerId);
        Assertions.assertNull(foundPlayer);
    }

    @Test
    void getAll() {
        Player player = createNewUniquePlayer();
        playerRepo.save(player);
        var listOfPlayers = playerRepo.getAll();
        Assertions.assertTrue(listOfPlayers.size()>0);
    }

    @Test
    void getByIdShouldReturnFound() {
        Player player = createNewUniquePlayer();
        Long newId = playerRepo.save(player);
        Player foundPlayer = playerRepo.getById(newId);
        Assertions.assertEquals(foundPlayer.getUserName(), player.getUserName());
    }

    @Test
    void getByIdShouldReturnNotFound() {
        Long impossibleId = -1L;
        Player foundPlayer = playerRepo.getById(impossibleId);
        Assertions.assertNull(foundPlayer);
    }

    @Test
    void getByUserNameShouldReturnFound() {
        Player player = createNewUniquePlayer();
        playerRepo.save(player);
        Player foundPlayer = playerRepo.getByUserName(player.getUserName());
        Assertions.assertNotNull(foundPlayer);
    }
}