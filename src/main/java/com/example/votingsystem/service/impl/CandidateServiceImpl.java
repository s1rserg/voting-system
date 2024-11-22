package com.example.votingsystem.service.impl;

import com.example.votingsystem.model.CandidateDTO;
import com.example.votingsystem.model.entities.Candidate;
import com.example.votingsystem.model.entities.Voting;
import com.example.votingsystem.model.mappers.CandidateMapper;
import com.example.votingsystem.repository.CandidateRepository;
import com.example.votingsystem.repository.VotingRepository;
import com.example.votingsystem.service.CandidateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CandidateServiceImpl implements CandidateService {

    private final CandidateRepository candidateRepository;
    private final VotingRepository votingRepository;

    public CandidateServiceImpl(CandidateRepository candidateRepository,
                                VotingRepository votingRepository) {
        this.candidateRepository = candidateRepository;
        this.votingRepository = votingRepository;
    }

    @Override
    @Transactional
    public CandidateDTO create(CandidateDTO candidateDTO) {
        Voting voting = votingRepository.findById(candidateDTO.getVotingId())
                .orElseThrow(() -> new IllegalArgumentException("Voting not found"));

        Candidate candidate = CandidateMapper.toEntity(candidateDTO, voting);
        Candidate savedCandidate = candidateRepository.save(candidate);
        return CandidateMapper.toDto(savedCandidate);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CandidateDTO> getById(Long id) {
        return candidateRepository.findById(id)
                .map(CandidateMapper::toDto);
    }

    @Override
    @Transactional
    public void incrementVotes(Long id) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Candidate not found"));
        candidate.incrementVotes();
        candidateRepository.save(candidate);
    }

    @Override
    @Transactional
    public void decrementVotes(Long id) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Candidate not found"));
        candidate.decrementVotes();
        candidateRepository.save(candidate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CandidateDTO> getByVotingId(Long votingId) {
        return candidateRepository.findByVotingId(votingId)
                .stream()
                .map(CandidateMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        candidateRepository.deleteById(id);
    }
}
