package com.example.votingsystem.controller;

import com.example.votingsystem.model.Voting;
import com.example.votingsystem.service.VotingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/votings")
public class VotingController {
    private VotingService votingService;

    @Autowired
    public void setVotingService(VotingService votingService) {
        this.votingService = votingService;
    }

    @GetMapping
    public ResponseEntity<String> getAllVotings(Model model) {
        try {
            model.addAttribute("votings", votingService.getAllVotings());
            return ResponseEntity.ok("main");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving votings.");
        }
    }

    @GetMapping("/create")
    public String showCreateVotingForm() {
        return "newpoll";
    }

    @PostMapping("/create")
    public ResponseEntity<String> createVoting(@ModelAttribute Voting voting) {
        try {
            if (voting.getCandidates().size() < 2) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("At least two candidates are required.");
            }
            Long userId = 1L;
            Voting newVoting = votingService.createVoting(voting.getTitle(), voting.getDescription(), userId, voting.getCandidates());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("redirect:/votings/" + newVoting.getId());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating voting.");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getVoting(@PathVariable Long id, Model model) {
        try {
            Voting voting = votingService.getVoting(id);
            Long userId = 1L;
            Long vote = votingService.getUserVote(id, userId);

            model.addAttribute("voting", voting);
            model.addAttribute("userId", userId);
            model.addAttribute("vote", vote);
            return ResponseEntity.ok("voting");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Voting not found.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving voting.");
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateVotingStatus(@PathVariable Long id, @RequestBody Map<String, Boolean> request) {
        try {
            boolean active = request.get("active");
            votingService.updateVotingStatus(id, active);
            return ResponseEntity.ok().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/{id}/votes")
    public ResponseEntity<Void> castVote(@PathVariable Long id, @RequestBody Map<String, Long> request) {
        try {
            Long userId = 1L;
            votingService.castVote(id, request.get("candidateId"), userId);
            return ResponseEntity.ok().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}/results")
    public ResponseEntity<String> getVotingResults(@PathVariable Long id, Model model) {
        try {
            Voting voting = votingService.getVoting(id);
            model.addAttribute("voting", voting);
            model.addAttribute("results", votingService.getResults(voting.getId()));
            return ResponseEntity.ok("votingRes");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Voting not found.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving results.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteVoting(@PathVariable Long id) {
        try {
            Long userId = 1L;
            votingService.deleteVoting(id, userId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Voting deleted successfully.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Voting not found.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting voting.");
        }
    }
}
