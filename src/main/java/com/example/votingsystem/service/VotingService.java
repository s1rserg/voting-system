package com.example.votingsystem.service;

import com.example.votingsystem.model.Candidate;
import com.example.votingsystem.model.Voting;

import java.util.List;

public interface VotingService {
    Voting create(String title, String description, Long creatorUserId, List<Candidate> candidates);
    List<Voting> getAll(String title, int page, int size);
    Voting getById(Long id);
    Voting updateStatus(Long id, Long userId, boolean active);
    boolean deleteById(Long id, Long userId);
    Voting castVote(Long votingId, Long candidateId, Long userId);
    Voting updateVote(Long votingId, Long candidateId, Long userId);
}
