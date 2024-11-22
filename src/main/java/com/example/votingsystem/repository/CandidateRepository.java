package com.example.votingsystem.repository;

import com.example.votingsystem.model.entities.Candidate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandidateRepository extends CrudRepository<Candidate, Long> {

    @Query("SELECT c FROM Candidate c WHERE c.voting.id = :votingId")
    List<Candidate> findByVotingId(@Param("votingId") Long votingId);
}