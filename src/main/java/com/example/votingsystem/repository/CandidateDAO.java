package com.example.votingsystem.repository;

import com.example.votingsystem.model.CandidateDTO;

import java.util.List;
import java.util.Optional;

public interface CandidateDAO {
    Long create(String name, int votes, Long votingId);
    Optional<CandidateDTO> getById(Long id);
    List<CandidateDTO> getByVotingId(Long votingId);
    CandidateDTO update(CandidateDTO candidate);
    void deleteById(Long id);
}