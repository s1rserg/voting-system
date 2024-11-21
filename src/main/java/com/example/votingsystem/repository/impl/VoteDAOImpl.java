package com.example.votingsystem.repository.impl;

import com.example.votingsystem.model.VoteDTO;
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

    private final RowMapper<VoteDTO> voteRowMapper = (rs, rowNum) -> new VoteDTO(
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
    public Optional<VoteDTO> getById(Long id) {
        String sql = "SELECT * FROM vote WHERE id = ?";
        return jdbcTemplate.query(sql, voteRowMapper, id).stream().findFirst();
    }

    @Override
    public List<VoteDTO> getByVotingId(Long votingId) {
        String sql = "SELECT * FROM vote WHERE voting_id = ?";
        return jdbcTemplate.query(sql, voteRowMapper, votingId);
    }

    @Override
    public Optional<VoteDTO> getByVotingAndUserId(Long votingId, Long userId) {
        String sql = "SELECT * FROM vote WHERE voting_id = ? AND user_id = ?";
        return jdbcTemplate.query(sql, voteRowMapper, votingId, userId).stream().findFirst();
    }

    @Override
    public VoteDTO update(VoteDTO vote) {
        String sql = "UPDATE vote SET voting_id = ?, candidate_id = ?, user_id = ? WHERE id = ?";
        jdbcTemplate.update(sql, vote.getVotingId(), vote.getCandidateId(), vote.getUserId(), vote.getId());
        return getById(vote.getId()).orElseThrow(() -> new RuntimeException("Failed to retrieve updated vote record"));
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM vote WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
