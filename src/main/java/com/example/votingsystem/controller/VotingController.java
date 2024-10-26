package com.example.votingsystem.controller;

import com.example.votingsystem.model.Candidate;
import com.example.votingsystem.model.Voting;
import com.example.votingsystem.service.VotingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/votings")
public class VotingController {
    private VotingService votingService;

    // Setter Injection for VotingService
    @Autowired
    public void setVotingService(VotingService votingService) {
        this.votingService = votingService;
    }

    // Retrieve all votings
    @GetMapping
    public String getAllVotings(Model model) {
        model.addAttribute("votings", votingService.getAllVotings());
        return "main";
    }

    // Show the form to create a new voting
    @GetMapping("/create")
    public String showCreateVotingForm(Model model) {
        Voting voting = new Voting();
        voting.getCandidates().add(new Candidate()); // Add an empty candidate for form input
        model.addAttribute("voting", voting);
        return "newpoll";
    }

    // Create a new voting
    @PostMapping("/create")
    public String createVoting(@ModelAttribute Voting voting) {
        if (voting.getCandidates().size() < 2) {
            throw new IllegalArgumentException("At least two candidates are required.");
        }
        Voting newVoting = votingService.createVoting(voting.getTitle(), voting.getDescription(), voting.getCandidates());
        Long newVotingId = newVoting.getId();
        return "redirect:/votings/" + newVotingId;
    }

    // Method to add more candidates dynamically
    @PostMapping("/addCandidate")
    public String addCandidate(@ModelAttribute Voting voting, Model model) {
        voting.getCandidates().add(new Candidate());
        model.addAttribute("voting", voting);
        return "newpoll";
    }

    // Retrieve a voting by ID
    @GetMapping("/{id}")
    public String getVoting(@PathVariable Long id, Model model) {
        Voting voting = votingService.getVoting(id);
        Long userId = 1L;

        // Retrieve the vote if the user has already voted
        Long vote = votingService.getUserVote(id, userId);

        model.addAttribute("voting", voting);
        model.addAttribute("userId", userId);
        model.addAttribute("vote", vote); // Add vote to the model (null if no vote)
        return "voting";
    }

    // Update voting status (open/close)
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateVotingStatus(@PathVariable Long id, @RequestBody Map<String, Boolean> request) {
        boolean active = request.get("active");
        votingService.updateVotingStatus(id, active);
        return ResponseEntity.ok().build();
    }

    // Cast a vote
    @PostMapping("/{id}/votes")
    public ResponseEntity<Void> castVote(@PathVariable Long id, @RequestBody Map<String, Long> request) {
        Long userId = 1L;
        votingService.castVote(id, request.get("candidateId"), userId);
        return ResponseEntity.ok().build();
    }

    // Retrieve results for a specific voting
    @GetMapping("/{id}/results")
    public String getVotingResults(@PathVariable Long id, Model model) {
        Voting voting = votingService.getVoting(id);
        model.addAttribute("voting", voting);
        model.addAttribute("results", voting.getResults());
        return "votingRes";
    }

    // Delete a voting
    @DeleteMapping("/{id}")
    public String deleteVoting(@PathVariable Long id) {
        Long userId = 1L;
        votingService.deleteVoting(id, userId);
        return "redirect:/votings";
    }
}
