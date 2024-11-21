package com.example.votingsystem.model;

import java.util.ArrayList;
import java.util.List;

public class VotingDTO {
    private Long id;
    private String title;
    private String description;
    private boolean active;
    private Long creatorUserId;
    private List<CandidateDTO> candidates;
    private List<VoteDTO> votes;

    public VotingDTO(Long id, String title, String description, boolean active, Long creatorUserId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.active = active;
        this.creatorUserId = creatorUserId;
        this.candidates = new ArrayList<>();
        this.votes = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Long getCreatorUserId() {
        return creatorUserId;
    }

    public void setCreatorUserId(Long creatorUserId) {
        this.creatorUserId = creatorUserId;
    }

    public void setCandidates(List<CandidateDTO> candidates) {
        this.candidates = candidates;
    }

    public void setVotes(List<VoteDTO> votes) {
        this.votes = votes;
    }

    public List<CandidateDTO> getCandidates() {
        return candidates;
    }

    public List<VoteDTO> getVotes() {
        return votes;
    }
}
