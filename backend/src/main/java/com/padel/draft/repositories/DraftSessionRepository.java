package com.padel.draft.repositories;

import com.padel.draft.domain.draft.DraftSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DraftSessionRepository extends JpaRepository<DraftSession, Long> {

    Optional<DraftSession> findFirstByLeagueIdOrderByStartedAtDesc(Long leagueId);
    Boolean existsByLeagueIdAndCompletedAtIsNull(Long leagueId);
}
