package com.fattech.twitterclone.repos;

import com.fattech.twitterclone.models.Follow;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.Random;

@SpringBootTest
class FollowRepoTest {
    private final FollowRepo followRepo;

    @Autowired
    FollowRepoTest(FollowRepo followRepo) {
        this.followRepo = followRepo;
    }

    private Follow createNewUniqueFollow() {
        long time = new Date().getTime();
        Long dummyPlayerId = time + new Random().nextInt(200);
        Long dummyFollowerId = time + new Random().nextInt(200) + 200;
        Follow follow = new Follow();
        follow.setPlayerId(dummyPlayerId);
        follow.setFollowerId(dummyFollowerId);
        follow.setCreatedAt(time);
        follow.setLastModified(time);

        return follow;
    }

    @Test
    void saveFollowWithNoId() {
        Follow follow = createNewUniqueFollow();
        Long generatedId = followRepo.save(follow);
        Assertions.assertTrue(generatedId > 0);
    }

    @Test
    void saveFollowWithId() {
        Follow follow = createNewUniqueFollow();
        Long impossibleId = -7L;
        follow.setId(impossibleId);
        Long generatedId = followRepo.save(follow);
        Assertions.assertNotEquals(generatedId, impossibleId);
    }

    @Test
    void updateShouldHaveEffect() {
        Follow follow = createNewUniqueFollow();
        Long newId = followRepo.save(follow);
        Follow foundFollow = followRepo.getById(newId);
        Long time = new Date().getTime() + 2000L;
        foundFollow.setPlayerId(time);
        followRepo.update(foundFollow, newId);
        Follow updatedFollow = followRepo.getById(newId);
        Assertions.assertEquals(updatedFollow.getPlayerId(), foundFollow.getPlayerId());
    }

    @Test
    void getAllShouldReturnNonEmptyList() {
        Follow follow = createNewUniqueFollow();
        followRepo.save(follow);
        var listOfFollows = followRepo.getAll();
        Assertions.assertTrue(listOfFollows.size() > 0);
    }

    @Test
    void getByIdShouldReturnFound() {
        Follow follow = createNewUniqueFollow();
        Long newId = followRepo.save(follow);
        Follow foundFollow = followRepo.getById(newId);
        Assertions.assertNotNull(foundFollow);
    }

    @Test
    void getByIdShouldReturnNotFound() {
        Long impossibleId = -8L;
        Follow foundFollow = followRepo.getById(impossibleId);
        Assertions.assertNull(foundFollow);
    }

    @Test
    void getByPlayerFollowerIdShouldReturnFound() {
        final long time = new Date().getTime();
        Random random = new Random();
        final Long impossiblePlayerId = -1 * (time + random.nextInt(200));
        final Long impossibleFollowerId = -1 * (time + random.nextInt(400));

        Follow follow = createNewUniqueFollow();
        follow.setPlayerId(impossiblePlayerId);
        follow.setFollowerId(impossibleFollowerId);

        followRepo.save(follow);
        Follow foundFollow = followRepo.getByPlayerIdFollowerId(impossiblePlayerId, impossibleFollowerId);
        Assertions.assertTrue(
                foundFollow.getPlayerId().equals(follow.getPlayerId()) &&
                        foundFollow.getFollowerId().equals(follow.getFollowerId())
        );
    }
}