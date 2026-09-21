package com.padel.draft.api;

import com.padel.draft.league.League;
import java.math.BigDecimal;
import java.util.UUID;

public record LeagueResponse(UUID id, String name, BigDecimal salaryCap, int rosterSize) {
    public static LeagueResponse from(League league) {
        return new LeagueResponse(league.getId(), league.getName(), league.getSalaryCap(), league.getRosterSize());
    }
}
