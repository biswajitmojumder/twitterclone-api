package com.fattech.twitterclone.repos;

import com.fattech.twitterclone.models.Tweet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
class TweetRepoTest {

    private final TweetRepo tweetRepo;

    @Autowired
    TweetRepoTest(TweetRepo tweetRepo) {
        this.tweetRepo = tweetRepo;
    }

    private Tweet createNewUniqueTweet() {
        Long time = new Date().getTime();
        Long dummyPlayerId = -4L;
        Long dummyReplyOf = -7L;
        Long dummyRetweetOf = -14L;
        String testMessage = "TESTMSG-" + time;
        String testImgUrl = "TESTIMGURL-" + time;
        Tweet tweet = new Tweet();
        tweet.setPlayerId(dummyPlayerId);
        tweet.setMessage(testMessage);
        tweet.setImageUrl(testImgUrl);
        tweet.setReplyOf(dummyReplyOf);
        tweet.setRetweetOf(dummyRetweetOf);
        tweet.setCreatedAt(time);
        tweet.setLastModified(time);

        return tweet;
    }

    @Test
    void saveTweetWithNoId() {
        Tweet tweet = createNewUniqueTweet();
        Long generatedId = tweetRepo.save(tweet);
        Assertions.assertTrue(generatedId>0);
    }

    @Test
    void saveTweetWithId() {
        Tweet tweet = createNewUniqueTweet();
        Long impossibleId = -4L;
        tweet.setId(impossibleId);
        Long generatedId = tweetRepo.save(tweet);
        Assertions.assertNotEquals(generatedId, impossibleId);
    }

    @Test
    void update() {
        Tweet tweet = createNewUniqueTweet();
        Long newId = tweetRepo.save(tweet);
        Tweet foundTweet = tweetRepo.getById(newId);
        String updatedMessage = "UPDATED MESSAGE";
        foundTweet.setMessage(updatedMessage);
        tweetRepo.update(foundTweet, newId);
        Tweet updatedTweet = tweetRepo.getById(newId);
        Assertions.assertEquals(updatedTweet.getMessage(), updatedMessage);
    }

    @Test
    void delete() {
        Tweet tweet = createNewUniqueTweet();
        Long newTweetId = tweetRepo.save(tweet);
        tweetRepo.delete(newTweetId);
        Tweet foundTweet = tweetRepo.getById(newTweetId);
        Assertions.assertNull(foundTweet);
    }

    @Test
    void getAll() {
        Tweet tweet = createNewUniqueTweet();
        tweetRepo.save(tweet);
        var listOfTweets = tweetRepo.getAll();
        Assertions.assertTrue(listOfTweets.size()>0);
    }

    @Test
    void getByIdShouldReturnFound() {
        Tweet tweet = createNewUniqueTweet();
        Long generatedId = tweetRepo.save(tweet);
        Tweet foundTweet = tweetRepo.getById(generatedId);
        Assertions.assertNotNull(foundTweet);
    }

    @Test
    void getByIdShouldReturnNotFound() {
        Long impossibeId = -9L;
        Tweet foundTweet = tweetRepo.getById(impossibeId);
        Assertions.assertNull(foundTweet);
    }
}