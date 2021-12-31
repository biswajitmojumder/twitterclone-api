package com.fattech.twitterclone.repos;

import com.fattech.twitterclone.models.Tag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
class TagRepoTest {
    private final TagRepo tagRepo;

    @Autowired
    TagRepoTest(TagRepo tagRepo) {
        this.tagRepo = tagRepo;
    }

    private Tag createNewUniqueTag() {
        long time = new Date().getTime();
        Long dummyTweetId = -8L;
        String testTagName = "TESTTAG-"+time;
        Tag tag = new Tag();
        tag.setTweetId(dummyTweetId);
        tag.setTagName(testTagName);
        tag.setCreatedAt(time);
        tag.setLastModified(time);

        return tag;
    }

    @Test
    void saveTagWithNoId() {
        Tag tag = createNewUniqueTag();
        Long generatedId = tagRepo.save(tag);
        Assertions.assertTrue(generatedId>0);
    }

    @Test
    void saveTagWithId() {
        Tag tag = createNewUniqueTag();
        Long impossibleId = -99L;
        tag.setId(impossibleId);
        Long generatedId = tagRepo.save(tag);
        Assertions.assertNotEquals(generatedId, impossibleId);
    }

    @Test
    void updateMustBeVisible() {
        Tag tag = createNewUniqueTag();
        Long newId = tagRepo.save(tag);
        Tag foundTag = tagRepo.getById(newId);
        String updatedTagName = "TAG NAME UPDATED";
        foundTag.setTagName(updatedTagName);
        tagRepo.update(foundTag, newId);
        Tag updatedTag = tagRepo.getById(newId);
        Assertions.assertEquals(updatedTag.getTagName(), updatedTagName);
    }

    @Test
    void deleteShouldWork() {
        Tag tag = createNewUniqueTag();
        Long newId = tagRepo.save(tag);
        tagRepo.delete(newId);
        Tag foundTag = tagRepo.getById(newId);
        Assertions.assertNull(foundTag);
    }

    @Test
    void getAll() {
        Tag tag = createNewUniqueTag();
        tagRepo.save(tag);
        var listOfTags = tagRepo.getAll();
        Assertions.assertTrue(listOfTags.size()>0);
    }

    @Test
    void getByIdShouldReturnFound() {
        Tag tag = createNewUniqueTag();
        Long generatedId = tagRepo.save(tag);
        Tag foundTag = tagRepo.getById(generatedId);
        Assertions.assertNotNull(foundTag);
    }

    @Test
    void getByIdShouldReturnNotFound() {
        Long impossibleId = -67L;
        Tag foundTag = tagRepo.getById(impossibleId);
        Assertions.assertNull(foundTag);
    }
}