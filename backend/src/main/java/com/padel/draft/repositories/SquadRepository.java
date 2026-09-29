package com.padel.draft.repositories;

import com.padel.draft.domain.squad.SquadPlayer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SquadRepository extends JpaRepository<SquadPlayer, Long> {

    List<SquadPlayer> findByDraftSessionIdOrderByPurchasedAtAsc(Long draftSessionId);

    boolean existsByDraftSessionIdAndPlayerId(Long draftSessionId, Long playerId);
}
