package com.fattech.twitterclone.repos;

import com.fattech.twitterclone.models.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

@Repository
public class PlayerRepo implements EntityDAO<Player> {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PlayerRepo(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long save(Player entity) {
        String INSERT_QUERY = "" +
                "INSERT INTO tbl_players(\"userName\", \"fullName\", \"email\", \"password\", \"imageUrl\", \"createdAt\", \"lastModified\") " +
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

        return (long) Objects.requireNonNull(keyHolder.getKeys()).get("id");
    }

    @Override
    public Long update(Player entity, Long id) {
        String UPDATE_QUERY = "" +
                "UPDATE tbl_players " +
                "SET \"userName\"=?, " +
                "\"fullName\"=?, " +
                "\"email\"=?, " +
                "\"password\"=?, " +
                "\"imageUrl\"=?, " +
                "\"createdAt\"=?, " +
                "\"lastModified\"=? WHERE id=?";
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
        return jdbcTemplate.query("SELECT * FROM tbl_players", new BeanPropertyRowMapper<Player>(Player.class));
    }

    @Override
    public Player getById(Long id) {
        return jdbcTemplate.queryForObject("SELECT * FROM tbl_players WHERE id=?", new BeanPropertyRowMapper<Player>(Player.class), id);
    }
}
