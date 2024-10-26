package com.example.votingsystem.config;

import com.example.votingsystem.model.Voting;
import com.example.votingsystem.repository.VotingRepository;
import com.example.votingsystem.service.VotingService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class AppConfig {

    @Bean
    public VotingRepository votingRepository() {
        return new VotingRepository();
    }

    @Bean
    @Scope("singleton")
    public VotingService votingService(VotingRepository votingRepository) {
        return new VotingService(votingRepository);
    }

    @Bean
    @Scope("prototype")
    public Voting voting() {
        return new Voting();
    }
}
