package com.fattech.twitterclone.repos;

import com.fattech.twitterclone.models.Tag;

import java.util.List;

public interface TagDAO {
    List<Tag> getByTweetId(Long tweetId);

    List<Tag> getByTweetIds(List<Long> tweetIds);

    List<String> getTagNamesByTweetId(Long tweetId);

    void deleteByTagNamesTweetId(List<String> tagNames, Long tweetId);

    void saveAll(List<Tag> tags, Long tweetId);
}
