package com.example.votingsystem.model;

import java.util.*;

public class Voting {
    private Long id;
    private String title;
    private String description;
    private boolean active;
    private Long creatorUserId;
    private List<Candidate> candidates = new ArrayList<>();
    private Map<Candidate, Integer> results = new HashMap<>();
    private Map<Long, Long> userVotes = new HashMap<>(); // Tracks each user's vote

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
        return title;
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

    public Map<Candidate, Integer> getResults() {
        return results;
    }

    public void setResults(Map<Candidate, Integer> results) {
        this.results = results;
    }

    public Map<Long, Long> getUserVotes() {
        return userVotes;
    }

    public void setUserVotes(Map<Long, Long> userVotes) {
        this.userVotes = userVotes;
    }
}
