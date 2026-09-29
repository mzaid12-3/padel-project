package com.padel.draft.repositories;

import com.padel.draft.domain.league.League;
import com.padel.draft.domain.player.PlayerStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeagueRepository extends JpaRepository<League, Long> {
    List<League> findByOwnerId(Long ownerId);

}
