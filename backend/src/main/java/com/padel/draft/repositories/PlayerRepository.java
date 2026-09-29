package com.padel.draft.repositories;

import com.padel.draft.domain.league.League;
import com.padel.draft.domain.player.Player;
import com.padel.draft.domain.player.PlayerStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    List<Player> findByLeagueId(Long leagueId);
    List<Player> findByLeagueAndStatus(League league, PlayerStatus status);
    boolean existsByLeagueIdAndNameIgnoreCase(Long leagueId, String name);
}
