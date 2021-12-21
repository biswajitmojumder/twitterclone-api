package com.fattech.twitterclone.repos;

import com.fattech.twitterclone.models.Follow;
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
import java.util.List;
import java.util.Objects;

@Repository
public class FollowRepo implements EntityDAO<Follow>, FollowDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FollowRepo(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long save(Follow entity) {
        String INSERT_QUERY = "" +
                "INSERT INTO tbl_follows(" +
                "playerId, " +
                "followerId, " +
                "createdAt, " +
                "lastModified) " +
                "VALUES(?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, entity.getPlayerId());
            ps.setLong(2, entity.getFollowerId());
            ps.setLong(3, entity.getCreatedAt());
            ps.setLong(4, entity.getLastModified());

            return ps;
        }, keyHolder);

        var key = (BigInteger) Objects.requireNonNull(keyHolder.getKeys()).get("GENERATED_KEY");
        return key.longValue();
    }

    @Override
    public Long update(Follow entity, Long id) {
        String UPDATE_QUERY = "" +
                "UPDATE tbl_follows " +
                "SET playerId=?, " +
                "followerId=?, " +
                "createdAt=?, " +
                "lastModified=? WHERE id=?";
        jdbcTemplate.update(
                UPDATE_QUERY,
                entity.getPlayerId(),
                entity.getFollowerId(),
                entity.getCreatedAt(),
                entity.getLastModified(),
                id
        );
        return id;
    }

    @Override
    public Long delete(Long id) {
        String DELETE_QUERY = "DELETE FROM tbl_follows WHERE id=?";
        jdbcTemplate.update(DELETE_QUERY, id);
        return id;
    }

    @Override
    public List<Follow> getAll() {
        String GET_ALL_QUERY = "SELECT * FROM tbl_follows";
        return jdbcTemplate.query(GET_ALL_QUERY, new BeanPropertyRowMapper<>(Follow.class));
    }

    @Override
    public Follow getById(Long id) {
        String GET_QUERY = "SELECT * FROM tbl_follows WHERE id=?";
        try {
            return jdbcTemplate.queryForObject(GET_QUERY, new BeanPropertyRowMapper<>(Follow.class), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Follow getByPlayerIdFollowerId(Long playerId, Long followerId) {
        String GET_QUERY = "SELECT * FROM tbl_follows WHERE playerId=? AND followerId=?";
        try {
            return jdbcTemplate.queryForObject(GET_QUERY, new BeanPropertyRowMapper<>(Follow.class), playerId, followerId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
