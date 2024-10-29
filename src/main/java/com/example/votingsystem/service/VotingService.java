package com.example.votingsystem.service;

import com.example.votingsystem.model.Candidate;
import com.example.votingsystem.model.Vote;
import com.example.votingsystem.model.Voting;
import com.example.votingsystem.repository.VotingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class VotingService {
    private final VotingRepository votingRepository;
    private final ApplicationContext context;

    // Constructor Injection for VotingRepository
    @Autowired
    public VotingService(VotingRepository votingRepository, ApplicationContext context) {
        this.votingRepository = votingRepository;
        this.context = context;
    }

    public List<Voting> getAllVotings(String title, int page, int size) {
        // Filter votings based on title and description
        List<Voting> filteredVotings = votingRepository.findAll().stream()
                .filter(v -> (title == null || v.getTitle().contains(title)))
                .collect(Collectors.toList());

        // Calculate start and end indices for pagination
        int start = page * size;
        int end = Math.min(start + size, filteredVotings.size());

        // Handle cases where the page number exceeds the available data
        if (start >= filteredVotings.size()) {
            return List.of(); // Return an empty list if the start index is out of range
        }

        // Return the paginated list
        return filteredVotings.subList(start, end);
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

        votingRepository.save(voting);
        return voting;
    }

    public Voting castVote(Long votingId, Long candidateId, Long userId) {
        Voting voting = getVoting(votingId);

        if (!voting.isActive()) {
            throw new IllegalStateException("Voting is closed. You cannot cast a vote.");
        }

        if (voting.getVotes().stream().anyMatch(vote -> vote.getUserId().equals(userId))) {
            throw new IllegalStateException("User has already voted in this voting.");
        }

        Candidate candidate = voting.getCandidates().stream()
                .filter(c -> c.getId().equals(candidateId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Candidate not found"));

        candidate.incrementVotes();

        Vote vote = context.getBean(Vote.class); // demonstrating creating bean using prototype
        vote.setId(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);
        vote.setVotingId(votingId);
        vote.setCandidateId(candidateId);
        vote.setUserId(userId);

        voting.getVotes().add(vote);
        votingRepository.save(voting);
        return voting;
    }

    public boolean deleteVoting(Long votingId, Long userId) {
        Voting voting = getVoting(votingId);
        if (!voting.getCreatorUserId().equals(userId)) {
            throw new SecurityException("Only the creator can delete this voting.");
        }
        votingRepository.delete(votingId);
        return true;
    }

    public Voting updateVotingStatus(Long id, boolean active) {
        Voting voting = getVoting(id);
        voting.setActive(active);
        votingRepository.save(voting);
        return voting;
    }

}
