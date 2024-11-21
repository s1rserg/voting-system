package com.example.votingsystem.repository;

import com.example.votingsystem.model.VoteDTO;

import java.util.List;
import java.util.Optional;

public interface VoteDAO {
    Long create(Long votingId, Long candidateId, Long userId);
    Optional<VoteDTO> getById(Long id);
    List<VoteDTO> getByVotingId(Long votingId);
    Optional<VoteDTO> getByVotingAndUserId(Long votingId, Long userId);
    VoteDTO update(VoteDTO vote);
    void deleteById(Long id);
}
