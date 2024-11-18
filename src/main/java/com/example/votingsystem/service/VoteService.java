package com.example.votingsystem.service;

import com.example.votingsystem.model.Vote;

import java.util.List;

public interface VoteService {
    Vote create(Long votingId, Long candidateId, Long userId);
    Vote getById(Long voteId);
    Vote update(Long votingId, Long candidateId, Long userId);
    List<Vote> getByVotingId(Long votingId);
    boolean retract(Long votingId, Long userId);
}
