package com.example.votingsystem.model;

import java.util.UUID;

public class Candidate {
    private Long id;
    private String name;
    private int votes;

    public Candidate() {
        this.id = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
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
}