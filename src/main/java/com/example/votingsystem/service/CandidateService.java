package com.example.votingsystem.service;

import com.example.votingsystem.model.CandidateDTO;

import java.util.List;
import java.util.Optional;

public interface CandidateService {
    CandidateDTO create(CandidateDTO candidate);
    Optional<CandidateDTO> getById(Long id);
    void incrementVotes(Long id);
    void decrementVotes(Long id);
    List<CandidateDTO> getByVotingId(Long votingId);
    void deleteById(Long id);
}
