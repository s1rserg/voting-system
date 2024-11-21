package com.example.votingsystem.repository;

import com.example.votingsystem.model.Vote;

import java.util.List;
import java.util.Optional;

public interface VoteDAO {
    Long create(Long votingId, Long candidateId, Long userId);
    Optional<Vote> getById(Long id);
    List<Vote> getByVotingId(Long votingId);
    Optional<Vote> getByVotingAndUserId(Long votingId, Long userId);
    Vote update(Vote vote);
    void deleteById(Long id);
}
