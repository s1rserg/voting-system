package com.example.votingsystem.service;

import com.example.votingsystem.model.Candidate;

import java.util.List;
import java.util.Optional;

public interface CandidateService {
    Long create(Candidate candidate);
    Optional<Candidate> getById(Long id);
    void incrementVotes(Long id);
    void decrementVotes(Long id);
    List<Candidate> getByVotingId(Long votingId);
    void deleteById(Long id);
}
