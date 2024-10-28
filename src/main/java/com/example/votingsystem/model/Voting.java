package com.example.votingsystem.model;

import java.util.ArrayList;
import java.util.List;

public class Voting {
    private Long id;
    private String title;
    private String description;
    private boolean active;
    private Long creatorUserId;
    private List<Candidate> candidates;
    private List<Vote> votes;

    public Voting(Long id, String title, String description, boolean active, Long creatorUserId, List<Candidate> candidates) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.active = active;
        this.creatorUserId = creatorUserId;
        this.candidates = candidates;
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

    public List<Candidate> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<Candidate> candidates) {
        this.candidates = candidates;
    }

    public List<Vote> getVotes() {
        return votes;
    }

    public void setVotes(List<Vote> votes) {
        this.votes = votes;
    }
}
