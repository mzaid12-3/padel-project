package com.padel.draft.league;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeagueRepository extends JpaRepository<League, UUID> { }
