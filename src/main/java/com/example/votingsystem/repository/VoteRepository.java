package com.example.votingsystem.repository;

import com.example.votingsystem.model.entities.Vote;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoteRepository extends CrudRepository<Vote, Long> {
    // Use of automatically generated method
    List<Vote> findByVotingId(Long votingId);

    @Query("SELECT v FROM Vote v WHERE v.voting.id = :votingId AND v.userId = :userId")
    Optional<Vote> findByVotingAndUserId(@Param("votingId") Long votingId, @Param("userId") Long userId);
}
