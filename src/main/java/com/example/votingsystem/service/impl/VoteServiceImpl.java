package com.example.votingsystem.service.impl;

import com.example.votingsystem.model.Vote;
import com.example.votingsystem.repository.VoteDAO;
import com.example.votingsystem.service.CandidateService;
import com.example.votingsystem.service.VoteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VoteServiceImpl implements VoteService {

    private final VoteDAO voteDAO;
    private final CandidateService candidateService;

    public VoteServiceImpl(VoteDAO voteDAO, CandidateService candidateService) {
        this.voteDAO = voteDAO;
        this.candidateService = candidateService;
    }

    @Override
    public Vote create(Long votingId, Long candidateId, Long userId) {
        Long voteId = voteDAO.create(votingId, candidateId, userId);
        return voteDAO.getById(voteId).orElseThrow(() -> new IllegalArgumentException("Error in creating vote"));
    }

    @Override
    public Vote getById(Long voteId) {
        return voteDAO.getById(voteId).orElseThrow(() -> new IllegalArgumentException("Vote not found"));
    }

    @Override
    @Transactional
    public Vote update(Long votingId, Long candidateId, Long userId) {
        Vote vote = voteDAO.getByVotingAndUserId(votingId, userId).orElseThrow(() -> new IllegalArgumentException("No vote found for the given user in this voting"));
        candidateService.decrementVotes(vote.getCandidateId());
        vote.setCandidateId(candidateId);
        return voteDAO.update(vote);
    }

    @Override
    public List<Vote> getByVotingId(Long votingId) {
        return voteDAO.getByVotingId(votingId);
    }

    @Override
    @Transactional
    public boolean retract(Long votingId, Long userId) {
        Vote vote = voteDAO.getByVotingAndUserId(votingId, userId).orElseThrow(() -> new IllegalArgumentException("No vote found for the given user in this voting"));
        voteDAO.deleteById(vote.getId());
        candidateService.decrementVotes(vote.getCandidateId());
        return true;
    }

}
