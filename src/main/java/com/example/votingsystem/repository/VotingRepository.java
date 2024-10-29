package com.example.votingsystem.repository;

import com.example.votingsystem.model.Voting;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class VotingRepository {
    private final Map<Long, Voting> votingStorage = new HashMap<>();

    public Optional<Voting> findById(Long id) {
        return Optional.ofNullable(votingStorage.get(id));
    }

    public Collection<Voting> findAll() {
        return votingStorage.values();
    }

    public void delete(Long id) {
        votingStorage.remove(id);
    }

    public void save(Voting voting) {
        votingStorage.put(voting.getId(), voting);
    }
}
