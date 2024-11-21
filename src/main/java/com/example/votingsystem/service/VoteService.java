package com.example.votingsystem.service;

import com.example.votingsystem.model.VoteDTO;

import java.util.List;

public interface VoteService {
    VoteDTO create(Long votingId, Long candidateId, Long userId);
    VoteDTO getById(Long voteId);
    VoteDTO update(Long votingId, Long candidateId, Long userId);
    List<VoteDTO> getByVotingId(Long votingId);
    boolean retract(Long votingId, Long userId);
}
