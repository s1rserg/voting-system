package com.example.votingsystem.service.impl;

import com.example.votingsystem.model.CandidateDTO;
import com.example.votingsystem.repository.CandidateDAO;
import com.example.votingsystem.service.CandidateService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CandidateServiceImpl implements CandidateService {

    private final CandidateDAO candidateDAO;

    public CandidateServiceImpl(CandidateDAO candidateDAO) {
        this.candidateDAO = candidateDAO;
    }

    @Override
    public Long create(CandidateDTO candidate) {
        return candidateDAO.create(candidate.getName(), candidate.getVotes(), candidate.getVotingId());
    }

    @Override
    public Optional<CandidateDTO> getById(Long id) {
        return candidateDAO.getById(id);
    }

    @Override
    public void incrementVotes(Long id) {
        CandidateDTO candidate = candidateDAO.getById(id).orElseThrow(() -> new IllegalArgumentException("Candidate not found"));
        candidate.incrementVotes();
        candidateDAO.update(candidate);
    }

    @Override
    public void decrementVotes(Long id) {
        CandidateDTO candidate = candidateDAO.getById(id).orElseThrow(() -> new IllegalArgumentException("Candidate not found"));
        candidate.decrementVotes();
        candidateDAO.update(candidate);
    }

    @Override
    public List<CandidateDTO> getByVotingId(Long votingId) {
        return candidateDAO.getByVotingId(votingId);
    }

    @Override
    public void deleteById(Long id) {
        candidateDAO.deleteById(id);
    }
}
