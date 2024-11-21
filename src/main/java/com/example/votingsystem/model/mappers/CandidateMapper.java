package com.example.votingsystem.model.mappers;

import com.example.votingsystem.model.CandidateDTO;
import com.example.votingsystem.model.entities.Candidate;
import com.example.votingsystem.model.entities.Voting;

public class CandidateMapper {

    public static CandidateDTO toDto(Candidate candidate) {
        return new CandidateDTO(
                candidate.getId(),
                candidate.getName(),
                candidate.getVotes(),
                candidate.getVoting().getId()
        );
    }

    public static Candidate toEntity(CandidateDTO candidateDTO, Voting voting) {
        return new Candidate(
                candidateDTO.getName(),
                candidateDTO.getVotes(),
                voting
        );
    }
}