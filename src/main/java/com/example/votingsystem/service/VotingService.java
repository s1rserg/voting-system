package com.example.votingsystem.service;

import com.example.votingsystem.model.CandidateDTO;
import com.example.votingsystem.model.VotingDTO;

import java.util.List;

public interface VotingService {
    VotingDTO create(String title, String description, Long creatorUserId, List<CandidateDTO> candidates);
    List<VotingDTO> getAll(String title, int page, int size);
    VotingDTO getById(Long id);
    VotingDTO updateStatus(Long id, Long userId, boolean active);
    boolean deleteById(Long id, Long userId);
    VotingDTO castVote(Long votingId, Long candidateId, Long userId);
    VotingDTO updateVote(Long votingId, Long candidateId, Long userId);
}
