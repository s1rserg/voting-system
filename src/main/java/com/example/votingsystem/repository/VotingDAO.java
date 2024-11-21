package com.example.votingsystem.repository;

import com.example.votingsystem.model.Voting;

import java.util.List;
import java.util.Optional;

public interface VotingDAO {
    Long create(String title, String description, Long creatorUserId);
    Optional<Voting> getById(Long id);
    List<Voting> getAll(String title, int page, int size);
    Voting update(Voting voting);
    void delete(Long id);
}
