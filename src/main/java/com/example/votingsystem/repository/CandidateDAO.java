package com.example.votingsystem.repository;

import com.example.votingsystem.model.Candidate;

import java.util.List;
import java.util.Optional;

public interface CandidateDAO {
    Long create(String name, int votes, Long votingId);
    Optional<Candidate> getById(Long id);
    List<Candidate> getByVotingId(Long votingId);
    Candidate update(Candidate candidate);
    void deleteById(Long id);
}