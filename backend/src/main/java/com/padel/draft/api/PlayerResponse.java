package com.padel.draft.api;

import com.padel.draft.player.Player;
import java.math.BigDecimal;
import java.util.UUID;

public record PlayerResponse(UUID id, String name, BigDecimal rating, String preferredSide,
                             BigDecimal cost, int matchesPlayed, boolean active) {
    public static PlayerResponse from(Player player) {
        return new PlayerResponse(player.getId(), player.getName(), player.getRating(),
                player.getPreferredSide().name(), player.getCost(), player.getMatchesPlayed(), player.isActive());
    }
}
