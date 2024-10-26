package com.example.votingsystem.service;

import com.example.votingsystem.model.Candidate;
import com.example.votingsystem.model.Voting;
import com.example.votingsystem.repository.VotingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class VotingService {
    private final VotingRepository votingRepository;

    // Constructor Injection for VotingRepository
    @Autowired
    public VotingService(VotingRepository votingRepository) {
        this.votingRepository = votingRepository;
    }

    // Field Injection for NotificationService
    @Autowired
    private NotificationService notificationService;

    public Collection<Voting> getAllVotings() {
        return votingRepository.findAll();
    }

    public Voting getVoting(Long votingId) {
        return votingRepository.findById(votingId)
                .orElseThrow(() -> new IllegalArgumentException("Voting not found"));
    }

    public Voting createVoting(String title, String description, List<Candidate> candidates) {
        Voting voting = new Voting();
        voting.setId(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);
        voting.setTitle(title);
        voting.setDescription(description);
        voting.setCandidates(candidates);
        voting.setActive(true);
        voting.setResults(new HashMap<>());
        voting.setCreatorUserId(1L);
        votingRepository.save(voting);

        // Purely for example of field injection
        notificationService.notifyUser(voting.getCreatorUserId(), "New voting created!");

        return voting;
    }

    public void castVote(Long votingId, Long candidateId, Long userId) {
        Voting voting = getVoting(votingId);

        // Check if voting is active
        if (!voting.isActive()) {
            throw new IllegalStateException("Voting is closed. You cannot cast a vote.");
        }

        // Check if the user has already voted
        if (voting.getUserVotes().containsKey(userId)) {
            throw new IllegalStateException("User has already voted in this voting.");
        }

        // Find the candidate and add the vote
        Candidate candidate = voting.getCandidates().stream()
                .filter(c -> c.getId().equals(candidateId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Candidate not found"));

        voting.getResults().merge(candidate, 1, Integer::sum);
        voting.getUserVotes().put(userId, candidateId); // Record the user's vote
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
        return voting.getUserVotes().get(userId);
    }
}
