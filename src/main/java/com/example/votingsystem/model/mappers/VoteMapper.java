package com.example.votingsystem.model.mappers;

import com.example.votingsystem.model.VoteDTO;
import com.example.votingsystem.model.entities.Candidate;
import com.example.votingsystem.model.entities.Vote;
import com.example.votingsystem.model.entities.Voting;
import org.springframework.stereotype.Component;

@Component
public class VoteMapper {

    public static VoteDTO toDto(Vote vote) {
        return new VoteDTO(
                vote.getId(),
                vote.getVoting().getId(),
                vote.getCandidate().getId(),
                vote.getUserId()
        );
    }

    public static Vote toEntity(VoteDTO voteDTO, Voting voting, Candidate candidate) {
        return new Vote(
                voting,
                candidate,
                voteDTO.getUserId()
        );
    }
}