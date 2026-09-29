package com.padel.draft.services;

import com.padel.draft.domain.league.League;
import com.padel.draft.domain.user.User;
import com.padel.draft.repositories.LeagueRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class LeagueService {
    private final LeagueRepository leagueRepository;
    private final UserService userService;
    public LeagueService(LeagueRepository leagueRepository,UserService userService) {
        this.leagueRepository = leagueRepository;
        this.userService = userService;
    }
    public League createLeague(Long ownerId, String name, BigDecimal salaryCap, int squadSize){
        if(salaryCap == null || salaryCap.signum()<=0){
            throw new IllegalArgumentException("salaryCap must be greater than 0");
        }
        if(squadSize<=0){
            throw new IllegalArgumentException("squadSize must be greater than 0");
        }
        User owner = userService.getUserById(ownerId);

        League league = new League(owner, name, salaryCap, squadSize);

        return leagueRepository.save(league);
    }
    public List<League> getLeaguesForOwner(Long ownerId) {
        userService.getUserById(ownerId);

        return leagueRepository.findByOwnerId(ownerId);
    }

    public League getLeagueById(Long leagueId){
        return leagueRepository.findById(leagueId)
                .orElseThrow(() -> new IllegalArgumentException("League not found"));
    }
}
