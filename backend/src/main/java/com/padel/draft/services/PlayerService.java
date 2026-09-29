package com.padel.draft.services;


import com.padel.draft.domain.league.League;
import com.padel.draft.domain.player.Player;
import com.padel.draft.domain.player.PlayerStatus;
import com.padel.draft.repositories.PlayerRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final LeagueService leagueService;


    public PlayerService(PlayerRepository playerRepository, LeagueService leagueService) {
        this.playerRepository = playerRepository;
        this.leagueService = leagueService;

    }

    public Player getPlayerById(Long playerId){
        return playerRepository.findById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found"));
    }

    public Player addPlayer(Long leagueId,
                            String name,
                            BigDecimal rating,
                            String preferredSide,
                            BigDecimal basePrice,
                            int matchesPlayed){
        if (name == null || name.isBlank()){
            throw new IllegalArgumentException("Player name is required");
        }
        if(rating == null || rating.signum() <= 0){
            throw new IllegalArgumentException("Player rating is required");
        }
        if(preferredSide == null || preferredSide.isBlank()){
            throw new IllegalArgumentException("Player preferredSide is required");
        }
        if(basePrice == null || basePrice.signum() < 0){
            throw new IllegalArgumentException("Player basePrice is required");
        }
        if(matchesPlayed < 0){
            throw new IllegalArgumentException("Player matchesPlayed is required");
        }

        League league = leagueService.getLeagueById(leagueId);

        if(playerRepository.existsByLeagueIdAndNameIgnoreCase(leagueId, name)){
            throw new IllegalArgumentException("Player already exists");
        }

        Player player = new Player(
                league,
                name,
                rating,
                preferredSide,
                basePrice,
                matchesPlayed
        );
        return playerRepository.save(player);

    }

    public List<Player> getPlayersForLeague(Long leagueId){
        leagueService.getLeagueById(leagueId);
        return playerRepository.findByLeagueId(leagueId);
    }

    public List<Player> getAvailablePlayersForLeague(Long leagueId){
        League league = leagueService.getLeagueById(leagueId);

        return playerRepository.findByLeagueAndStatus(league, PlayerStatus.AVAILABLE);
    }

    public Player updatePlayerStatus(Long playerId, PlayerStatus playerStatus){
        if(playerStatus == null){
            throw new IllegalArgumentException("Player status is required");
        }
        Player player = getPlayerById(playerId);

        player.setStatus(playerStatus);
        return playerRepository.save(player);

    }

}
