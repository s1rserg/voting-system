package com.example.votingsystem.service.impl;

import com.example.votingsystem.model.CandidateDTO;
import com.example.votingsystem.model.VoteDTO;
import com.example.votingsystem.model.VotingDTO;
import com.example.votingsystem.repository.VotingDAO;
import com.example.votingsystem.service.CandidateService;
import com.example.votingsystem.service.VoteService;
import com.example.votingsystem.service.VotingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class VotingServiceImpl implements VotingService {
    private final VotingDAO votingDAO;
    private final CandidateService candidateService;
    private final VoteService voteService;

    public VotingServiceImpl(VotingDAO votingDAO, CandidateService candidateService, VoteService voteService) {
        this.votingDAO = votingDAO;
        this.candidateService = candidateService;
        this.voteService = voteService;
    }

    @Override
    @Transactional
    public VotingDTO create(String title, String description, Long creatorUserId, List<CandidateDTO> candidates) {
        Long votingId = votingDAO.create(title, description, creatorUserId);
        for (CandidateDTO candidate : candidates) {
            candidate.setVotingId(votingId);
            candidateService.create(candidate);
        }

        return getById(votingId);
    }

    @Override
    public VotingDTO getById(Long id) {
        VotingDTO voting = votingDAO.getById(id).orElseThrow(() -> new IllegalArgumentException("Voting not found"));
        List<CandidateDTO> candidates = candidateService.getByVotingId(id);
        List<VoteDTO> votes = voteService.getByVotingId(id);
        voting.setCandidates(candidates);
        voting.setVotes(votes);

        return voting;
    }

    @Override
    public List<VotingDTO> getAll(String title, int page, int size) {
        List<VotingDTO> votings = votingDAO.getAll(title, page, size);
        for (VotingDTO voting : votings) {
            List<CandidateDTO> candidates = candidateService.getByVotingId(voting.getId());
            List<VoteDTO> votes = voteService.getByVotingId(voting.getId());

            voting.setCandidates(candidates);
            voting.setVotes(votes);
        }

        return votings;
    }

    @Override
    public VotingDTO updateStatus(Long id, Long userId, boolean active) {
        VotingDTO voting = votingDAO.getById(id).orElseThrow(() -> new IllegalArgumentException("Voting not found"));
        if (!voting.getCreatorUserId().equals(userId)) {
            throw new SecurityException("Only the creator can update this voting.");
        }
        voting.setActive(active);
        return votingDAO.update(voting);
    }

    @Override
    public boolean deleteById(Long id, Long userId) {
        VotingDTO voting = votingDAO.getById(id).orElseThrow(() -> new IllegalArgumentException("Voting not found"));
        if (!voting.getCreatorUserId().equals(userId)) {
            throw new SecurityException("Only the creator can delete this voting.");
        }
        votingDAO.delete(id);
        return true;
    }

    @Override
    @Transactional
    public VotingDTO castVote(Long votingId, Long candidateId, Long userId) {
        VotingDTO voting = votingDAO.getById(votingId).orElseThrow(() -> new IllegalArgumentException("Voting not found"));
        if (!voting.isActive()) {
            throw new IllegalStateException("Voting is closed. You cannot cast a vote.");
        }

        if (voteService.getByVotingId(votingId).stream().anyMatch(vote -> vote.getUserId().equals(userId))) {
            throw new IllegalStateException("User has already voted in this voting.");
        }
        CandidateDTO candidate = candidateService.getById(candidateId).orElseThrow(() -> new IllegalArgumentException("Candidate not found"));
        if (!candidate.getVotingId().equals(votingId)) {
            throw new IllegalStateException("There is no such candidate in this voting.");
        }
        voteService.create(votingId, candidateId, userId);
        candidateService.incrementVotes(candidateId);
        return getById(votingId);
    }

    @Override
    @Transactional
    public VotingDTO updateVote(Long votingId, Long candidateId, Long userId) {
        VotingDTO voting = votingDAO.getById(votingId).orElseThrow(() -> new IllegalArgumentException("Voting not found"));
        if (!voting.isActive()) {
            throw new IllegalStateException("Voting is closed. You cannot update a vote.");
        }

        if (voteService.getByVotingId(votingId).stream().noneMatch(vote -> vote.getUserId().equals(userId))) {
            throw new IllegalStateException("You did not vote in this voting.");
        }

        CandidateDTO candidate = candidateService.getById(candidateId).orElseThrow(() -> new IllegalArgumentException("Candidate not found"));

        if (!candidate.getVotingId().equals(votingId)) {
            throw new IllegalStateException("There is no such candidate in this voting.");
        }

        voteService.update(votingId, candidateId, userId);
        candidateService.incrementVotes(candidateId);
        return getById(votingId);
    }
}
