package com.fattech.twitterclone.repos;

import com.fattech.twitterclone.models.Reaction;
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
public class ReactionRepo implements EntityDAO<Reaction>, ReactionDAO {
    private final JdbcTemplate jdbcTemplate;

    Logger logger = LoggerFactory.getLogger(ReactionRepo.class);

    @Autowired
    public ReactionRepo(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long save(Reaction entity) {
        String INSERT_QUERY = "" +
                "INSERT INTO tbl_reactions(" +
                "tweetId, " +
                "playerId, " +
                "reactionType, " +
                "createdAt, " +
                "lastModified) " +
                "VALUES(?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, entity.getTweetId());
            ps.setLong(2, entity.getPlayerId());
            ps.setString(3, entity.getReactionType());
            ps.setLong(4, entity.getCreatedAt());
            ps.setLong(5, entity.getLastModified());

            return ps;
        }, keyHolder);

        var key = (BigInteger) Objects.requireNonNull(keyHolder.getKeys()).get("GENERATED_KEY");
        return key.longValue();
    }

    @Override
    public Long update(Reaction entity, Long id) {
        String UPDATE_QUERY = "" +
                "UPDATE tbl_reactions " +
                "SET tweetId=?, " +
                "playerId=?, " +
                "reactionType=?, " +
                "createdAt=?, " +
                "lastModified=? WHERE id=?";
        jdbcTemplate.update(
                UPDATE_QUERY,
                entity.getTweetId(),
                entity.getPlayerId(),
                entity.getReactionType(),
                entity.getCreatedAt(),
                entity.getLastModified(),
                id);
        return id;
    }

    @Override
    public Long delete(Long id) {
        String DELETE_QUERY = "DELETE FROM tbl_reactions WHERE id=?";
        jdbcTemplate.update(DELETE_QUERY, id);
        return id;
    }

    @Override
    public List<Reaction> getAll() {
        String GET_ALL_QUERY = "SELECT * FROM tbl_reactions";
        return jdbcTemplate.query(GET_ALL_QUERY, new BeanPropertyRowMapper<>(Reaction.class));
    }

    @Override
    public Reaction getById(Long id) {
        String GET_QUERY = "SELECT * FROM tbl_reactions WHERE id=?";
        try {
            return jdbcTemplate.queryForObject(GET_QUERY, new BeanPropertyRowMapper<>(Reaction.class), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Reaction getPlayerTweetReaction(Long tweetId, Long playerId, String reactionType) {
        String GET_QUERY = "SELECT * FROM tbl_reactions " +
                "WHERE tweetId=? AND playerId=? AND reactionType=?";
        try {
            return jdbcTemplate.queryForObject(
                    GET_QUERY,
                    new BeanPropertyRowMapper<>(Reaction.class),
                    tweetId,
                    playerId,
                    reactionType
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Reaction> getByTweetIdsPlayerId(List<Long> tweetIds, Long playerId) {
        if (tweetIds.isEmpty()) {
            logger.info("TweetIds is empty, will return Collections.emptyList!");
            return Collections.emptyList();
        }
        String IN_SQL = String.join(",", Collections.nCopies(tweetIds.size(), "?"));
        return jdbcTemplate.query(
                String.format("SELECT * FROM tbl_reactions WHERE tweetId IN (%s) AND playerId=%d", IN_SQL, playerId),
                new BeanPropertyRowMapper<>(Reaction.class),
                tweetIds.toArray()
        );
    }
}
