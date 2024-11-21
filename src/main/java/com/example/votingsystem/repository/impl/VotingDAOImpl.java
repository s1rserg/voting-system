package com.example.votingsystem.repository.impl;

import com.example.votingsystem.model.Voting;
import com.example.votingsystem.repository.VotingDAO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class VotingDAOImpl implements VotingDAO {
    private final JdbcTemplate jdbcTemplate;

    public VotingDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Voting> votingRowMapper = (rs, rowNum) -> new Voting(
            rs.getLong("id"),
            rs.getString("title"),
            rs.getString("description"),
            rs.getBoolean("active"),
            rs.getLong("creator_user_id")
    );

    @Override
    public Long create(String title, String description, Long creatorUserId) {
        String sql = "INSERT INTO voting (title, description, active, creator_user_id) VALUES (?, ?, ?, ?) RETURNING id";
        return jdbcTemplate.queryForObject(sql, Long.class, title, description, true, creatorUserId);
    }

    @Override
    public Optional<Voting> getById(Long id) {
        String sql = "SELECT * FROM voting WHERE id = ?";
        return jdbcTemplate.query(sql, votingRowMapper, id).stream().findFirst();
    }

    @Override
    public List<Voting> getAll(String title, int page, int size) {
        StringBuilder sql = new StringBuilder("SELECT * FROM voting");
        if (title != null && !title.isEmpty()) {
            sql.append(" WHERE title LIKE ?");
        }
        sql.append(" LIMIT ? OFFSET ?");
        int offset = (page - 1) * size;

        if (title != null && !title.isEmpty()) {
            return jdbcTemplate.query(sql.toString(), votingRowMapper, "%" + title + "%", size, offset);
        } else {
            return jdbcTemplate.query(sql.toString(), votingRowMapper, size, offset);
        }
    }

    @Override
    public Voting update(Voting voting) {
        String sql = "UPDATE voting SET title = ?, description = ?, active = ?, creator_user_id = ? WHERE id = ?";
        jdbcTemplate.update(sql, voting.getTitle(), voting.getDescription(), voting.isActive(), voting.getCreatorUserId(), voting.getId());
        return getById(voting.getId()).orElseThrow(() -> new RuntimeException("Failed to retrieve updated voting record"));
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM voting WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
