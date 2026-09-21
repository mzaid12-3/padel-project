package com.padel.draft.draft;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DraftSessionRepository extends JpaRepository<DraftSession, UUID> {
    List<DraftSession> findByLeagueIdOrderByIdAsc(UUID leagueId);
}
