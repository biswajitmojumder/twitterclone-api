package com.fattech.twitterclone.repos;

import com.fattech.twitterclone.models.Tweet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Repository
public class TweetRepo implements EntityDAO<Tweet>, TweetDAO {
    private final JdbcTemplate jdbcTemplate;

    Logger logger = LoggerFactory.getLogger(TweetRepo.class);

    @Autowired
    public TweetRepo(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long save(Tweet entity) {
        String INSERT_QUERY = "" +
                "INSERT INTO tbl_tweets(" +
                "playerId, " +
                "message, " +
                "imageUrl, " +
                "replyOf, " +
                "retweetOf, " +
                "createdAt, " +
                "lastModified) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, entity.getPlayerId());
            ps.setString(2, entity.getMessage());
            ps.setString(3, entity.getImageUrl());
            ps.setLong(4, entity.getReplyOf());
            ps.setLong(5, entity.getRetweetOf());
            ps.setLong(6, entity.getCreatedAt());
            ps.setLong(7, entity.getLastModified());

            return ps;
        }, keyHolder);

        var key = (BigInteger) Objects.requireNonNull(keyHolder.getKeys()).get("GENERATED_KEY");
        return key.longValue();
    }

    @Override
    public Long update(Tweet entity, Long id) {
        String UPDATE_QUERY = "" +
                "UPDATE tbl_tweets " +
                "SET playerId=?, " +
                "message=?, " +
                "imageUrl=?, " +
                "replyOf=?, " +
                "retweetOf=?, " +
                "createdAt=?, " +
                "lastModified=? WHERE id=?";
        jdbcTemplate.update(
                UPDATE_QUERY,
                entity.getPlayerId(),
                entity.getMessage(),
                entity.getImageUrl(),
                entity.getReplyOf(),
                entity.getRetweetOf(),
                entity.getCreatedAt(),
                entity.getLastModified(),
                id
        );
        return id;
    }

    @Override
    public Long delete(Long id) {
        String DELETE_QUERY = "DELETE FROM tbl_tweets WHERE id=?";
        jdbcTemplate.update(DELETE_QUERY, id);
        return id;
    }

    @Override
    public List<Tweet> getAll() {
        String SELECT_ALL_QUERY = "SELECT * FROM tbl_tweets";
        return jdbcTemplate.query(SELECT_ALL_QUERY, new BeanPropertyRowMapper<>(Tweet.class));
    }

    @Override
    public Tweet getById(Long id) {
        String FIND_QUERY = "SELECT * FROM tbl_tweets WHERE id=?";
        try {
            return jdbcTemplate.queryForObject(FIND_QUERY, new BeanPropertyRowMapper<>(Tweet.class), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Tweet> getLimitedByPlayerIdsOlderThan(Long limit,
                                                      List<Long> playerIds,
                                                      Long olderThan) {
        String IN_SQL = String.join(",", Collections.nCopies(playerIds.size(), "?"));
        return jdbcTemplate.query(
                String.format("SELECT * FROM tbl_tweets WHERE playerId IN (%s) AND createdAt<=%d ORDER BY createdAt DESC LIMIT %d", IN_SQL, olderThan, limit),
                new BeanPropertyRowMapper<>(Tweet.class),
                playerIds.toArray()
        );
    }

    @Override
    public List<Tweet> getByTweetIds(List<Long> tweetIds) {
        if (tweetIds.isEmpty()) {
            logger.info("tweetIds is empty, will return Collections.emptyList!");
            return Collections.emptyList();
        }
        String IN_SQL = String.join(",", Collections.nCopies(tweetIds.size(), "?"));
        return jdbcTemplate.query(
                String.format("SELECT * FROM tbl_tweets WHERE id IN (%s)", IN_SQL),
                new BeanPropertyRowMapper<>(Tweet.class),
                tweetIds.toArray()
        );
    }

    @Override
    public List<Tweet> getLimitedLatest(Long limit, Long time) {
        String QUERY = String.format("SELECT * FROM tbl_tweets WHERE createdAt<=%d ORDER BY createdAt DESC LIMIT %d", time, limit);
        return jdbcTemplate.query(
                QUERY,
                new BeanPropertyRowMapper<>(Tweet.class)
        );
    }

    @Override
    public List<Tweet> getLimitedHomeTweets(Long limit, List<Long> playerIds, Long olderThan) {
        if (playerIds.isEmpty()) {
            logger.info("playerIds is empty, will return Collections.emptyList!");
            return Collections.emptyList();
        }
        String IN_PLAYERIDS_SQL = playerIds.toString().substring(1, playerIds.toString().length() - 1);
        String QUERY = String.format("WITH repliedRetweetedTweetIds  AS (" +
                        "WITH latestFollowingTweets  AS (" +
                        "SELECT replyOf, retweetOf FROM tbl_tweets WHERE playerId IN (%s) AND createdAt<=%d ORDER BY createdAt DESC LIMIT %d" +
                        ") SELECT replyOf AS id FROM latestFollowingTweets WHERE replyOf != 0 UNION SELECT retweetOf AS id FROM latestFollowingTweets WHERE retweetOf != 0" +
                        ") SELECT * FROM tbl_tweets WHERE tbl_tweets.id IN (SELECT id FROM repliedRetweetedTweetIds) UNION " +
                        "(SELECT * FROM tbl_tweets WHERE playerId IN (%s) AND createdAt<=%d ORDER BY createdAt DESC LIMIT %d) ORDER BY createdAt DESC;",
                IN_PLAYERIDS_SQL, olderThan, limit, IN_PLAYERIDS_SQL, olderThan, limit);
        return jdbcTemplate.query(
                QUERY,
                new BeanPropertyRowMapper<>(Tweet.class)
        );
    }

    @Override
    public List<Long> getTweetIdsLimitedHomeTweets(Long limit, List<Long> playerIds, Long olderThan) {
        if (playerIds.isEmpty()) {
            logger.info("playerIds is empty, will return Collections.emptyList!");
            return Collections.emptyList();
        }
        String IN_PLAYERIDS_SQL = playerIds.toString().substring(1, playerIds.toString().length()-1);
        String QUERY = String.format("SELECT id FROM tbl_tweets " +
                "WHERE playerId IN (%s) AND createdAt<=%d " +
                "ORDER BY createdAt DESC LIMIT %d",
                IN_PLAYERIDS_SQL, olderThan, limit);
        return jdbcTemplate.queryForList(QUERY, Long.class);
    }
}
