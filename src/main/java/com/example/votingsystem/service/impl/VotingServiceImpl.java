package com.example.votingsystem.service.impl;

import com.example.votingsystem.model.CandidateDTO;
import com.example.votingsystem.model.VotingDTO;
import com.example.votingsystem.model.entities.Voting;
import com.example.votingsystem.model.entities.Candidate;
import com.example.votingsystem.model.mappers.VotingMapper;
import com.example.votingsystem.repository.VotingRepository;
import com.example.votingsystem.service.CandidateService;
import com.example.votingsystem.service.VoteService;
import com.example.votingsystem.service.VotingService;
import io.micrometer.common.util.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VotingServiceImpl implements VotingService {
    private final VotingRepository votingRepository;
    private final CandidateService candidateService;
    private final VoteService voteService;

    public VotingServiceImpl(VotingRepository votingRepository,
                             CandidateService candidateService,
                             VoteService voteService) {
        this.votingRepository = votingRepository;
        this.candidateService = candidateService;
        this.voteService = voteService;
    }

    @Override
    @Transactional
    public VotingDTO create(String title, String description, Long creatorUserId, List<CandidateDTO> candidates) {
        Voting voting = new Voting(title, description, true, creatorUserId);

        for (CandidateDTO candidate : candidates) {
            voting.getCandidates().add(new Candidate(candidate.getName(), 0, voting));
        }

        votingRepository.save(voting);

        return VotingMapper.toDto(voting);
    }

    @Override
    @Transactional(readOnly = true)
    public VotingDTO getById(Long id) {
        Voting voting = votingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Voting not found"));
        return VotingMapper.toDto(voting);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VotingDTO> getAll(String title, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);

        if (StringUtils.isBlank(title)) {

            List<Voting> votings = votingRepository.findAllVotings(pageRequest);

            List<VotingDTO> dtos = votings.stream()
                    .map(VotingMapper::toDto)
                    .collect(Collectors.toList());

            return dtos;
        }

        return votingRepository.findAllByTitleContaining(title, pageRequest)
                .stream()
                .map(VotingMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public VotingDTO updateStatus(Long id, Long userId, boolean active) {
        Voting voting = votingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Voting not found"));

        if (!voting.getCreatorUserId().equals(userId)) {
            throw new SecurityException("Only the creator can update this voting.");
        }

        voting.setActive(active);
        votingRepository.save(voting);
        return VotingMapper.toDto(voting);
    }

    @Override
    @Transactional
    public boolean deleteById(Long id, Long userId) {
        Voting voting = votingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Voting not found"));

        if (!voting.getCreatorUserId().equals(userId)) {
            throw new SecurityException("Only the creator can delete this voting.");
        }

        votingRepository.delete(voting);
        return true;
    }

    @Override
    @Transactional
    public VotingDTO castVote(Long votingId, Long candidateId, Long userId) {
        Voting voting = votingRepository.findById(votingId)
                .orElseThrow(() -> new IllegalArgumentException("Voting not found"));

        if (!voting.isActive()) {
            throw new IllegalStateException("Voting is closed. You cannot cast a vote.");
        }

        if (voteService.getByVotingId(votingId).stream()
                .anyMatch(vote -> vote.getUserId().equals(userId))) {
            throw new IllegalStateException("User has already voted in this voting.");
        }

        voteService.create(votingId, candidateId, userId);
        candidateService.incrementVotes(candidateId);

        return VotingMapper.toDto(voting);
    }

    @Override
    @Transactional
    public VotingDTO updateVote(Long votingId, Long candidateId, Long userId) {
        Voting voting = votingRepository.findById(votingId)
                .orElseThrow(() -> new IllegalArgumentException("Voting not found"));

        if (!voting.isActive()) {
            throw new IllegalStateException("Voting is closed. You cannot update a vote.");
        }

        voteService.update(votingId, candidateId, userId);
        candidateService.incrementVotes(candidateId);
        return VotingMapper.toDto(voting);
    }
}
