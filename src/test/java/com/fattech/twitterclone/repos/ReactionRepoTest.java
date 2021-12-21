package com.fattech.twitterclone.repos;

import com.fattech.twitterclone.models.Reaction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.Random;

@SpringBootTest
class ReactionRepoTest {
    private final ReactionRepo reactionRepo;

    @Autowired
    ReactionRepoTest(ReactionRepo reactionRepo) {
        this.reactionRepo = reactionRepo;
    }

    private Reaction createNewUniqueReaction() {
        final long time = new Date().getTime();
        final long randomNumber = new Random().nextInt(200);
        final long tweetId = time + 1000L + randomNumber;
        final long playerId = time + 2000L + randomNumber;
        Reaction reaction = new Reaction();
        reaction.setTweetId(tweetId);
        reaction.setPlayerId(playerId);
        reaction.setReactionType("LIKE");
        reaction.setCreatedAt(time);
        reaction.setLastModified(time);

        return reaction;
    }

    @Test
    void saveReactionWithNoId() {
        Reaction reaction = createNewUniqueReaction();
        Long generatedId = reactionRepo.save(reaction);
        Assertions.assertTrue(generatedId>0);
    }

    @Test
    void saveReactionWithId() {
        Reaction reaction = createNewUniqueReaction();
        Long intendedId = -23L;
        reaction.setId(intendedId);
        Long newId = reactionRepo.save(reaction);
        Assertions.assertNotEquals(newId, intendedId);
    }

    @Test
    void update() {
        Reaction reaction = createNewUniqueReaction();
        Long newId = reactionRepo.save(reaction);
        Reaction foundReaction = reactionRepo.getById(newId);
        String newReaction = "SHOCKED";
        foundReaction.setReactionType(newReaction);
        reactionRepo.update(foundReaction, newId);
        Reaction updatedReaction = reactionRepo.getById(newId);
        Assertions.assertEquals(updatedReaction.getReactionType(), newReaction);
    }

    @Test
    void getAllShouldReturnNotEmptyList() {
        Reaction reaction = createNewUniqueReaction();
        reactionRepo.save(reaction);
        var listOfReactions = reactionRepo.getAll();
        Assertions.assertTrue(listOfReactions.size()>0);
    }

    @Test
    void getByIdShouldReturnFound() {
        Reaction reaction = createNewUniqueReaction();
        Long newId = reactionRepo.save(reaction);
        Reaction foundReaction = reactionRepo.getById(newId);
        Assertions.assertEquals(foundReaction.getTweetId(), reaction.getTweetId());
    }

    @Test
    void getByIdShouldReturnNotFound() {
        Long impossibleId = -409L;
        Reaction reaction = reactionRepo.getById(impossibleId);
        Assertions.assertNull(reaction);
    }

    @Test
    void getPlayerTweetReactionShouldReturnFound() {
        Reaction reaction = createNewUniqueReaction();
        Long tweetId = reaction.getTweetId();
        Long playerId = reaction.getPlayerId();
        String reactionType = reaction.getReactionType();
        reactionRepo.save(reaction);
        Reaction foundReaction = reactionRepo.getPlayerTweetReaction(tweetId, playerId, reactionType);

        var tweetIdIsMatch = tweetId.equals(foundReaction.getTweetId());
        var playerIdIsMatch = playerId.equals(foundReaction.getPlayerId());
        var reactionTypeIsMatch = reactionType.equals(foundReaction.getReactionType());

        Assertions.assertTrue(tweetIdIsMatch && playerIdIsMatch && reactionTypeIsMatch);
    }
}