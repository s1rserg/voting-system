package com.example.votingsystem.service;

import com.example.votingsystem.model.Candidate;
import com.example.votingsystem.model.Vote;
import com.example.votingsystem.model.Voting;
import com.example.votingsystem.repository.VotingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class VotingService {
    private final VotingRepository votingRepository;
    private final ApplicationContext context;
    // Field Injection for NotificationService
    @Autowired
    private NotificationService notificationService;

    // Constructor Injection for VotingRepository
    @Autowired
    public VotingService(VotingRepository votingRepository, ApplicationContext context) {
        this.votingRepository = votingRepository;
        this.context = context;
    }

    public Collection<Voting> getAllVotings() {
        return votingRepository.findAll();
    }

    public Voting getVoting(Long votingId) {
        return votingRepository.findById(votingId)
                .orElseThrow(() -> new IllegalArgumentException("Voting not found"));
    }

    public Voting createVoting(String title, String description, Long creatorUserId, List<Candidate> candidates) {
        Voting voting = new Voting(
                UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE,
                title,
                description,
                true,
                creatorUserId,
                candidates
        );

        // Purely for example of field injection
        notificationService.notifyUser(voting.getCreatorUserId(), "New voting created!");

        votingRepository.save(voting);
        return voting;
    }

    public void castVote(Long votingId, Long candidateId, Long userId) {
        Voting voting = getVoting(votingId);

        if (!voting.isActive()) {
            throw new IllegalStateException("Voting is closed. You cannot cast a vote.");
        }

        if (voting.getVotes().stream().anyMatch(vote -> vote.getUserId().equals(userId))) {
            throw new IllegalStateException("User has already voted in this voting.");
        }

        voting.getCandidates().stream()
                .filter(c -> c.getId().equals(candidateId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Candidate not found"));

        Vote vote = context.getBean(Vote.class); // demonstrating creating bean using prototype
        vote.setId(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);
        vote.setVotingId(votingId);
        vote.setCandidateId(candidateId);
        vote.setUserId(userId);

        voting.getVotes().add(vote);
        votingRepository.save(voting);
    }

    public void deleteVoting(Long votingId, Long userId) {
        Voting voting = getVoting(votingId);
        if (!voting.getCreatorUserId().equals(userId)) {
            throw new SecurityException("Only the creator can delete this voting.");
        }
        votingRepository.delete(votingId);
    }

    public void updateVotingStatus(Long id, boolean active) {
        Voting voting = getVoting(id);
        voting.setActive(active);
        votingRepository.save(voting);
    }

    public Long getUserVote(Long votingId, Long userId) {
        Voting voting = getVoting(votingId);
        return voting.getVotes().stream()
                .filter(vote -> vote.getUserId().equals(userId))
                .map(Vote::getCandidateId)
                .findFirst()
                .orElse(null);
    }

    public Map<Long, Integer> getResults(Long votingId) {
        Voting voting = getVoting(votingId);
        Map<Long, Integer> resultsMap = new HashMap<>();

        for (Vote vote : voting.getVotes()) {
            Long candidateId = vote.getCandidateId();
            resultsMap.put(candidateId, resultsMap.getOrDefault(candidateId, 0) + 1);
        }

        return resultsMap;
    }

}
