package com.example.votingsystem.model.mappers;

import com.example.votingsystem.model.VotingDTO;
import com.example.votingsystem.model.entities.Candidate;
import com.example.votingsystem.model.entities.Vote;
import com.example.votingsystem.model.entities.Voting;

import java.util.List;
import java.util.stream.Collectors;

public class VotingMapper {

    public static VotingDTO toDto(Voting voting) {
        VotingDTO votingDTO = new VotingDTO(
                voting.getId(),
                voting.getTitle(),
                voting.getDescription(),
                voting.isActive(),
                voting.getCreatorUserId()
        );
        votingDTO.setCandidates(
                voting.getCandidates().stream()
                        .map(CandidateMapper::toDto)
                        .toList()
        );
        votingDTO.setVotes(
                voting.getVotes().stream()
                        .map(VoteMapper::toDto)
                        .toList()
        );
        return votingDTO;
    }

    public static Voting toEntity(VotingDTO votingDTO) {
        Voting voting = new Voting(
                votingDTO.getTitle(),
                votingDTO.getDescription(),
                votingDTO.isActive(),
                votingDTO.getCreatorUserId()
        );

        List<Candidate> candidates = votingDTO.getCandidates().stream()
                .map(candidateDTO -> CandidateMapper.toEntity(candidateDTO, voting))
                .collect(Collectors.toList());
        voting.setCandidates(candidates);

        List<Vote> votes = votingDTO.getVotes().stream()
                .map(voteDTO -> {
                    Candidate candidate = candidates.stream()
                            .filter(c -> c.getId().equals(voteDTO.getCandidateId()))
                            .findFirst()
                            .orElseThrow(() -> new IllegalArgumentException("Candidate not found"));

                    return VoteMapper.toEntity(voteDTO, voting, candidate);
                })
                .collect(Collectors.toList());
        voting.setVotes(votes);

        return voting;
    }
}