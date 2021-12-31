package com.fattech.twitterclone.repos;

import com.fattech.twitterclone.models.Tag;
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
public class TagRepo implements EntityDAO<Tag>, TagDAO {
    private final JdbcTemplate jdbcTemplate;

    Logger logger = LoggerFactory.getLogger(TagRepo.class);

    @Autowired
    public TagRepo(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long save(Tag entity) {
        String INSERT_QUERY = "" +
                "INSERT INTO tbl_tags(" +
                "tweetId, " +
                "tagName, " +
                "createdAt, " +
                "lastModified) " +
                "VALUES(?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, entity.getTweetId());
            ps.setString(2, entity.getTagName());
            ps.setLong(3, entity.getCreatedAt());
            ps.setLong(4, entity.getLastModified());

            return ps;
        }, keyHolder);

        var key = (BigInteger) Objects.requireNonNull(keyHolder.getKeys()).get("GENERATED_KEY");
        return key.longValue();
    }

    @Override
    public Long update(Tag entity, Long id) {
        String UPDATE_QUERY = "" +
                "UPDATE tbl_tags " +
                "SET tweetId=?, " +
                "tagName=?, " +
                "createdAt=?, " +
                "lastModified=? WHERE id=?";
        jdbcTemplate.update(
                UPDATE_QUERY,
                entity.getTweetId(),
                entity.getTagName(),
                entity.getCreatedAt(),
                entity.getLastModified(),
                id
        );
        return id;
    }

    @Override
    public Long delete(Long id) {
        String DELETE_QUERY = "DELETE FROM tbl_tags WHERE id=?";
        jdbcTemplate.update(DELETE_QUERY, id);
        return id;
    }

    @Override
    public List<Tag> getAll() {
        String SELECT_ALL = "SELECT * FROM tbl_tags";
        return jdbcTemplate.query(SELECT_ALL, new BeanPropertyRowMapper<>(Tag.class));
    }

    @Override
    public Tag getById(Long id) {
        String FIND_QUERY = "SELECT * FROM tbl_tags WHERE id=?";
        try {
            return jdbcTemplate.queryForObject(FIND_QUERY, new BeanPropertyRowMapper<>(Tag.class), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Tag> getByTweetId(Long tweetId) {
        String QUERY = String.format("SELECT * FROM tbl_tags WHERE tweetId=%d", tweetId);
        return jdbcTemplate.query(
                QUERY,
                new BeanPropertyRowMapper<>(Tag.class)
        );
    }

    @Override
    public List<Tag> getByTweetIds(List<Long> tweetIds) {
        if (tweetIds.isEmpty()) {
            logger.info("tweetIds is empty, will return Collections.emptyList!");
            return Collections.emptyList();
        }
        String IN_SQL = String.join(",", Collections.nCopies(tweetIds.size(), "?"));
        return jdbcTemplate.query(
                String.format("SELECT * FROM tbl_tags WHERE tweetId IN (%s)", IN_SQL),
                new BeanPropertyRowMapper<>(Tag.class),
                tweetIds.toArray()
        );
    }

    @Override
    public List<String> getTagNamesByTweetId(Long tweetId) {
        String QUERY = String.format("SELECT tagName FROM tbl_tags WHERE tweetId=%d", tweetId);
        return jdbcTemplate.queryForList(QUERY, String.class);
    }

    @Override
    public void deleteByTagNamesTweetId(List<String> tagNames, Long tweetId) {
        if(tagNames.isEmpty()) {
            logger.info("tagNames is empty, deletion of nothing will do nothing");
        } else {
            String IN_SQL = String.join(",", Collections.nCopies(tagNames.size(), "?"));
            jdbcTemplate.update(
                    String.format("DELETE FROM tbl_tags WHERE tagName IN (%s) AND tweetId=%d", IN_SQL, tweetId),
                    tagNames.toArray()
            );
        }
    }

    @Override
    public void saveAll(List<Tag> tags, Long tweetId) {
        if(tags.isEmpty()) {
            logger.info("tagNames is empty, addition of nothing will do nothing");
        } else {
            tags.forEach(this::save);
        }
    }
}
