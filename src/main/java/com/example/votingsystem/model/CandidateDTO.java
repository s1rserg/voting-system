package com.example.votingsystem.model;

public class CandidateDTO {
    private Long id;
    private String name;
    private int votes;
    private Long votingId;

    public CandidateDTO(Long id, String name, int votes, Long votingId) {
        this.id = id;
        this.name = name;
        this.votes = votes;
        this.votingId = votingId;

    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVotes() {
        return votes;
    }

    public void incrementVotes() {
        this.votes++;
    }

    public Long getVotingId() {
        return votingId;
    }

    public void setVotingId(Long votingId) {
        this.votingId = votingId;
    }

    public void decrementVotes() {this.votes--;}

}