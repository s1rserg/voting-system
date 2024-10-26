package com.example.votingsystem.model;

import java.util.UUID;

public class Candidate {
    private Long id;
    private String name;

    public Candidate() {
        this.id = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}