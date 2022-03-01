package com.fattech.twitterclone.repos;

import com.fattech.twitterclone.models.Player;
import com.fattech.twitterclone.models.dtos.PlayerGetDto;
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
public class PlayerRepo implements EntityDAO<Player>, PlayerDAO {

    private final JdbcTemplate jdbcTemplate;

    Logger logger = LoggerFactory.getLogger(PlayerRepo.class);

    @Autowired
    public PlayerRepo(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long save(Player entity) {
        String INSERT_QUERY = "" +
                "INSERT INTO tbl_players(" +
                "userName, " +
                "fullName, " +
                "email, " +
                "password, " +
                "imageUrl, " +
                "createdAt, " +
                "lastModified) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, entity.getUserName());
            ps.setString(2, entity.getFullName());
            ps.setString(3, entity.getEmail());
            ps.setString(4, entity.getPassword());
            ps.setString(5, entity.getImageUrl());
            ps.setLong(6, entity.getCreatedAt());
            ps.setLong(7, entity.getLastModified());

            return ps;
        }, keyHolder);

        var key = (BigInteger) Objects.requireNonNull(keyHolder.getKeys()).get("GENERATED_KEY");
        return key.longValue();
    }

    @Override
    public Long update(Player entity, Long id) {
        String UPDATE_QUERY = "" +
                "UPDATE tbl_players " +
                "SET userName=?, " +
                "fullName=?, " +
                "email=?, " +
                "password=?, " +
                "imageUrl=?, " +
                "createdAt=?, " +
                "lastModified=? WHERE id=?";
        jdbcTemplate.update(
                UPDATE_QUERY,
                entity.getUserName(),
                entity.getFullName(),
                entity.getEmail(),
                entity.getPassword(),
                entity.getImageUrl(),
                entity.getCreatedAt(),
                entity.getLastModified(),
                id);
        return id;
    }

    @Override
    public Long delete(Long id) {
        String DELETE_QUERY = "DELETE FROM tbl_players WHERE id=?";
        jdbcTemplate.update(DELETE_QUERY, id);
        return id;
    }

    @Override
    public List<Player> getAll() {
        String SELECT_ALL_QUERY = "SELECT * FROM tbl_players";
        return jdbcTemplate.query(SELECT_ALL_QUERY, new BeanPropertyRowMapper<>(Player.class));
    }

    @Override
    public Player getById(Long id) {
        String SELECT_ID_QUERY = "SELECT * FROM tbl_players WHERE id=?";
        try {
            return jdbcTemplate.queryForObject(SELECT_ID_QUERY, new BeanPropertyRowMapper<>(Player.class), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Player getByUserName(String userName) {
        String FIND_QUERY = "SELECT * FROM tbl_players WHERE userName=?";
        try {
            return jdbcTemplate.queryForObject(FIND_QUERY, new BeanPropertyRowMapper<>(Player.class), userName);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<PlayerGetDto> getByPlayerIds(List<Long> playerIds) {
        if (playerIds.isEmpty()) {
            logger.info("PlayerIds is empty, will return Collections.emptyList!");
            return Collections.emptyList();
        }
        String In_PLAYERIDS_SQL = playerIds.toString().substring(1, playerIds.toString().length()-1);
        return jdbcTemplate.query(
                String.format("SELECT * FROM tbl_players WHERE id IN (%s)", In_PLAYERIDS_SQL),
                new BeanPropertyRowMapper<>(PlayerGetDto.class)
        );
    }

    @Override
    public List<PlayerGetDto> getRecommendedPlayers(Long playerId) {
        String SQL = String.format("(WITH recommendedPlayerIds AS (\n" +
                "\tWITH followingIds AS (SELECT DISTINCT playerId FROM tbl_follows WHERE followerId IN (%d)),\n" +
                "\tfollowerIds AS (SELECT DISTINCT followerId FROM tbl_follows WHERE playerId IN (%d))\n" +
                "    (SELECT playerId FROM tbl_follows WHERE followerId IN (SELECT playerId FROM followingIds) AND playerId NOT IN (SELECT playerId FROM followingIds)) UNION \n" +
                "    (SELECT DISTINCT playerId FROM tbl_follows WHERE followerId IN (SELECT followerId FROM followerIds) AND playerId NOT IN (SELECT playerId FROM followingIds)) \n" +
                ") SELECT id, userName, fullName, email, imageUrl FROM tbl_players WHERE id IN (SELECT DISTINCT playerId FROM recommendedPlayerIds) AND id != %d LIMIT 20) UNION\n" +
                "(SELECT id, userName, fullName, email, imageUrl FROM tbl_players " +
                "WHERE id NOT IN (SELECT DISTINCT playerId FROM tbl_follows WHERE followerId = %d) AND id != %d ORDER BY RAND() LIMIT 20)", playerId, playerId, playerId, playerId, playerId);
        return jdbcTemplate.query(
                SQL,
                new BeanPropertyRowMapper<>(PlayerGetDto.class)
        );
    }
}
