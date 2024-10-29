package com.example.votingsystem.config;

import com.example.votingsystem.model.Vote;
import com.example.votingsystem.repository.VotingRepository;
import com.example.votingsystem.service.VotingService;
import org.springframework.context.ApplicationContext;
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
    public VotingService votingService(VotingRepository votingRepository, ApplicationContext context) {
        return new VotingService(votingRepository, context);
    }

    @Bean
    @Scope("prototype")
    public Vote vote() {
        return new Vote();
    }
}
