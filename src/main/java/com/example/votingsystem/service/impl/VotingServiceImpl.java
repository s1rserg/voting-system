package com.example.votingsystem.service.impl;

import com.example.votingsystem.model.Candidate;
import com.example.votingsystem.model.Vote;
import com.example.votingsystem.model.Voting;
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
    public Voting create(String title, String description, Long creatorUserId, List<Candidate> candidates) {
        Long votingId = votingDAO.create(title, description, creatorUserId);
        for (Candidate candidate : candidates) {
            candidate.setVotingId(votingId);
            candidateService.create(candidate);
        }

        return getById(votingId);
    }

    @Override
    public Voting getById(Long id) {
        Voting voting = votingDAO.getById(id).orElseThrow(() -> new IllegalArgumentException("Voting not found"));
        List<Candidate> candidates = candidateService.getByVotingId(id);
        List<Vote> votes = voteService.getByVotingId(id);
        voting.setCandidates(candidates);
        voting.setVotes(votes);

        return voting;
    }

    @Override
    public List<Voting> getAll(String title, int page, int size) {
        List<Voting> votings = votingDAO.getAll(title, page, size);
        for (Voting voting : votings) {
            List<Candidate> candidates = candidateService.getByVotingId(voting.getId());
            List<Vote> votes = voteService.getByVotingId(voting.getId());

            voting.setCandidates(candidates);
            voting.setVotes(votes);
        }

        return votings;
    }

    @Override
    public Voting updateStatus(Long id, Long userId, boolean active) {
        Voting voting = votingDAO.getById(id).orElseThrow(() -> new IllegalArgumentException("Voting not found"));
        if (!voting.getCreatorUserId().equals(userId)) {
            throw new SecurityException("Only the creator can update this voting.");
        }
        voting.setActive(active);
        return votingDAO.update(voting);
    }

    @Override
    public boolean deleteById(Long id, Long userId) {
        Voting voting = votingDAO.getById(id).orElseThrow(() -> new IllegalArgumentException("Voting not found"));
        if (!voting.getCreatorUserId().equals(userId)) {
            throw new SecurityException("Only the creator can delete this voting.");
        }
        votingDAO.delete(id);
        return true;
    }

    @Override
    @Transactional
    public Voting castVote(Long votingId, Long candidateId, Long userId) {
        Voting voting = votingDAO.getById(votingId).orElseThrow(() -> new IllegalArgumentException("Voting not found"));
        if (!voting.isActive()) {
            throw new IllegalStateException("Voting is closed. You cannot cast a vote.");
        }

        if (voteService.getByVotingId(votingId).stream().anyMatch(vote -> vote.getUserId().equals(userId))) {
            throw new IllegalStateException("User has already voted in this voting.");
        }
        Candidate candidate = candidateService.getById(candidateId).orElseThrow(() -> new IllegalArgumentException("Candidate not found"));
        if (!candidate.getVotingId().equals(votingId)) {
            throw new IllegalStateException("There is no such candidate in this voting.");
        }
        voteService.create(votingId, candidateId, userId);
        candidateService.incrementVotes(candidateId);
        return getById(votingId);
    }

    @Override
    @Transactional
    public Voting updateVote(Long votingId, Long candidateId, Long userId) {
        Voting voting = votingDAO.getById(votingId).orElseThrow(() -> new IllegalArgumentException("Voting not found"));
        if (!voting.isActive()) {
            throw new IllegalStateException("Voting is closed. You cannot update a vote.");
        }

        if (voteService.getByVotingId(votingId).stream().noneMatch(vote -> vote.getUserId().equals(userId))) {
            throw new IllegalStateException("You did not vote in this voting.");
        }

        Candidate candidate = candidateService.getById(candidateId).orElseThrow(() -> new IllegalArgumentException("Candidate not found"));

        if (!candidate.getVotingId().equals(votingId)) {
            throw new IllegalStateException("There is no such candidate in this voting.");
        }

        voteService.update(votingId, candidateId, userId);
        candidateService.incrementVotes(candidateId);
        return getById(votingId);
    }
}
