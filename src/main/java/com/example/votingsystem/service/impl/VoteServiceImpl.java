package com.example.votingsystem.service.impl;

import com.example.votingsystem.model.VoteDTO;
import com.example.votingsystem.model.entities.Candidate;
import com.example.votingsystem.model.entities.Vote;
import com.example.votingsystem.model.entities.Voting;
import com.example.votingsystem.model.mappers.VoteMapper;
import com.example.votingsystem.repository.CandidateRepository;
import com.example.votingsystem.repository.VoteRepository;
import com.example.votingsystem.repository.VotingRepository;
import com.example.votingsystem.service.CandidateService;
import com.example.votingsystem.service.VoteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VoteServiceImpl implements VoteService {

    private final VoteRepository voteRepository;
    private final VotingRepository votingRepository;
    private final CandidateRepository candidateRepository;
    private final CandidateService candidateService;

    public VoteServiceImpl(VoteRepository voteRepository,
                           VotingRepository votingRepository,
                           CandidateRepository candidateRepository,
                           CandidateService candidateService) {
        this.voteRepository = voteRepository;
        this.votingRepository = votingRepository;
        this.candidateRepository = candidateRepository;
        this.candidateService = candidateService;
    }

    @Override
    @Transactional
    public VoteDTO create(Long votingId, Long candidateId, Long userId) {
        Voting voting = votingRepository.findById(votingId)
                .orElseThrow(() -> new IllegalArgumentException("Voting not found"));

        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new IllegalArgumentException("Candidate not found"));

        if (voteRepository.findByVotingAndUserId(votingId, userId).isPresent()) {
            throw new IllegalStateException("User has already voted in this voting");
        }

        Vote vote = new Vote(voting, candidate, userId);
        voteRepository.save(vote);

        return VoteMapper.toDto(vote);
    }

    @Override
    @Transactional(readOnly = true)
    public VoteDTO getById(Long voteId) {
        Vote vote = voteRepository.findById(voteId)
                .orElseThrow(() -> new IllegalArgumentException("Vote not found"));
        return VoteMapper.toDto(vote);
    }

    @Override
    @Transactional
    public VoteDTO update(Long votingId, Long candidateId, Long userId) {
        Vote vote = voteRepository.findByVotingAndUserId(votingId, userId)
                .orElseThrow(() -> new IllegalArgumentException("No vote found for the given user in this voting"));

        Candidate newCandidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new IllegalArgumentException("Candidate not found"));

        candidateService.decrementVotes(vote.getCandidate().getId());

        vote.setCandidate(newCandidate);
        voteRepository.save(vote);

        return VoteMapper.toDto(vote);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VoteDTO> getByVotingId(Long votingId) {
        return voteRepository.findByVotingId(votingId).stream()
                .map(VoteMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean retract(Long votingId, Long userId) {
        Vote vote = voteRepository.findByVotingAndUserId(votingId, userId)
                .orElseThrow(() -> new IllegalArgumentException("No vote found for the given user in this voting"));

        candidateService.decrementVotes(vote.getCandidate().getId());

        voteRepository.delete(vote);

        return true;
    }
}