package com.example.votingsystem.repository;

import com.example.votingsystem.model.entities.Voting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VotingRepository extends CrudRepository<Voting, Long> {

    @Query("SELECT v FROM Voting v WHERE LOWER(v.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    Page<Voting> findAllByTitleContaining(@Param("title") String title, Pageable pageable);

    // Using NamedQuery
    @Query(name = "Voting.findAllVotings")
    List<Voting> findAllVotings(Pageable pageable);
}
