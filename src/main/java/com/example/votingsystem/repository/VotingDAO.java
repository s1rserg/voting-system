package com.example.votingsystem.repository;

import com.example.votingsystem.model.VotingDTO;

import java.util.List;
import java.util.Optional;

public interface VotingDAO {
    Long create(String title, String description, Long creatorUserId);
    Optional<VotingDTO> getById(Long id);
    List<VotingDTO> getAll(String title, int page, int size);
    VotingDTO update(VotingDTO voting);
    void delete(Long id);
}
