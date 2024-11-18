package com.example.votingsystem.repository.impl;

import com.example.votingsystem.model.Vote;
import com.example.votingsystem.repository.VoteDAO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class VoteDAOImpl implements VoteDAO {
    private final JdbcTemplate jdbcTemplate;

    public VoteDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Vote> voteRowMapper = (rs, rowNum) -> new Vote(
            rs.getLong("id"),
            rs.getLong("voting_id"),
            rs.getLong("candidate_id"),
            rs.getLong("user_id")
    );

    @Override
    public Long create(Long votingId, Long candidateId, Long userId) {
        String sql = "INSERT INTO vote (voting_id, candidate_id, user_id) VALUES (?, ?, ?) RETURNING id";
        return jdbcTemplate.queryForObject(sql, Long.class, votingId, candidateId, userId);
    }

    @Override
    public Optional<Vote> getById(Long id) {
        String sql = "SELECT * FROM vote WHERE id = ?";
        return jdbcTemplate.query(sql, voteRowMapper, id).stream().findFirst();
    }

    @Override
    public List<Vote> getByVotingId(Long votingId) {
        String sql = "SELECT * FROM vote WHERE voting_id = ?";
        return jdbcTemplate.query(sql, voteRowMapper, votingId);
    }

    @Override
    public Optional<Vote> getByVotingAndUserId(Long votingId, Long userId) {
        String sql = "SELECT * FROM vote WHERE voting_id = ? AND user_id = ?";
        return jdbcTemplate.query(sql, voteRowMapper, votingId, userId).stream().findFirst();
    }

    @Override
    public Vote update(Vote vote) {
        String sql = "UPDATE vote SET voting_id = ?, candidate_id = ?, user_id = ? WHERE id = ?";
        jdbcTemplate.update(sql, vote.getVotingId(), vote.getCandidateId(), vote.getUserId(), vote.getId());
        return getById(vote.getId()).orElseThrow(() -> new RuntimeException("Failed to retrieve updated vote record"));
    }

    @Override
    public void deleteByVotingAndUserId(Long votingId, Long userId) {
        String sql = "DELETE FROM vote WHERE voting_id = ? AND id = ?";
        jdbcTemplate.update(sql, votingId, userId);
    }
}
