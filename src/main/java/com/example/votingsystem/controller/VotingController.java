package com.example.votingsystem.controller;

import com.example.votingsystem.model.Voting;
import com.example.votingsystem.service.VotingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/votings")
public class VotingController {

    private VotingService votingService;

    // Setter Injection for VotingService
    @Autowired
    public void setVotingService(VotingService votingService) {
        this.votingService = votingService;
    }

    // Retrieve all votings with optional filtering and pagination
    @GetMapping
    public ResponseEntity<?> getAllVotings(
            @RequestParam(required = false) String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        List<Voting> votings = votingService.getAllVotings(title, page, size);
        return ResponseEntity.ok(votings);
    }

    // Create a new voting
    @PostMapping()
    public ResponseEntity<?> createVoting(@RequestBody Voting voting) {
        if (voting.getCandidates().size() < 2) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("At least two candidates are required.");
        }
        Long userId = 1L;
        Voting newVoting = votingService.createVoting(voting.getTitle(), voting.getDescription(), userId, voting.getCandidates());
        return ResponseEntity.status(HttpStatus.CREATED).body(newVoting);
    }

    // Retrieve a voting by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getVoting(@PathVariable Long id) {
        try {
            Voting voting = votingService.getVoting(id);
            return ResponseEntity.ok(voting);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Update voting status (open/close)
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateVotingStatus(@PathVariable Long id, @RequestBody Map<String, Boolean> request) {
        boolean active = request.getOrDefault("active", false);
        try {
            Voting voting = votingService.updateVotingStatus(id, active);
            return ResponseEntity.ok(voting);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Cast a vote
    @PostMapping("/{id}/votes")
    public ResponseEntity<?> castVote(@PathVariable Long id, @RequestBody Map<String, Long> request) {
        Long userId = 1L;
        Long candidateId = request.get("candidateId");

        if (candidateId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Candidate ID is required.");
        }

        try {
            Voting voting = votingService.castVote(id, candidateId, userId);
            return ResponseEntity.ok(voting);
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Retrieve results for a specific voting
    @GetMapping("/{id}/votes")
    public ResponseEntity<?> getVotingResults(@PathVariable Long id) {
        try {
            Voting voting = votingService.getVoting(id);
            return ResponseEntity.ok(voting);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Delete a voting
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteVoting(@PathVariable Long id) {
        Long userId = 1L;
        try {
            boolean isDeleted = votingService.deleteVoting(id, userId);
            return ResponseEntity.ok(isDeleted);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
