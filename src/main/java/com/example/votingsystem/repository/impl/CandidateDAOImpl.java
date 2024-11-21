package com.example.votingsystem.repository.impl;

import com.example.votingsystem.model.Candidate;
import com.example.votingsystem.repository.CandidateDAO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class CandidateDAOImpl implements CandidateDAO {
    private final JdbcTemplate jdbcTemplate;

    public CandidateDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Candidate> candidateRowMapper = (rs, rowNum) -> new Candidate(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getInt("votes"),
            rs.getLong("voting_id")
    );

    @Override
    public Long create(String name, int votes, Long votingId) {
        String sql = "INSERT INTO candidate (name, votes, voting_id) VALUES (?, ?, ?) RETURNING id";
        return jdbcTemplate.queryForObject(sql, Long.class, name, votes, votingId);
    }

    @Override
    public Optional<Candidate> getById(Long id) {
        String sql = "SELECT * FROM candidate WHERE id = ?";
        return jdbcTemplate.query(sql, candidateRowMapper, id).stream().findFirst();
    }

    @Override
    public List<Candidate> getByVotingId(Long votingId) {
        String sql = "SELECT * FROM candidate WHERE voting_id = ?";
        return jdbcTemplate.query(sql, candidateRowMapper, votingId);
    }

    @Override
    public Candidate update(Candidate candidate) {
        String sql = "UPDATE candidate SET name = ?, votes = ?, voting_id = ? WHERE id = ?";
        jdbcTemplate.update(sql, candidate.getName(), candidate.getVotes(), candidate.getVotingId(), candidate.getId());
        return getById(candidate.getId()).orElseThrow(() -> new RuntimeException("Failed to retrieve updated candidate record"));
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM candidate WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}

