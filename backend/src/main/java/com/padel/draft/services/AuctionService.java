package com.padel.draft.services;

import com.padel.draft.domain.draft.DraftSession;
import com.padel.draft.domain.player.Player;
import com.padel.draft.domain.player.PlayerStatus;
import com.padel.draft.domain.squad.SquadPlayer;
import com.padel.draft.repositories.DraftSessionRepository;
import com.padel.draft.repositories.SquadRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@Transactional
public class AuctionService {

    private final DraftSessionService draftSessionService;
    private final PlayerService playerService;
    private final SquadRepository squadRepository;
    private final DraftSessionRepository draftSessionRepository;

    public AuctionService(DraftSessionService draftSessionService, PlayerService playerService, SquadRepository squadRepository,  DraftSessionRepository draftSessionRepository) {
        this.draftSessionService = draftSessionService;
        this.playerService = playerService;
        this.squadRepository = squadRepository;
        this.draftSessionRepository = draftSessionRepository;

    }

    public SquadPlayer buyPlayer(
            Long leagueId,
            Long playerId,
            BigDecimal purchasePrice
    ) {
        if (purchasePrice == null || purchasePrice.signum() < 0) {
            throw new IllegalArgumentException("Invalid purchase price");
        }

        DraftSession draftSession = draftSessionService.getActiveDraftForLeague(leagueId);

        Player player = playerService.getPlayerById(playerId);

        if (!player.getLeague().getId().equals(leagueId)) {
            throw new IllegalArgumentException("player does not belong to this league");
        }

        if (draftSession.getRemainingSlots() <= 0) {
            throw new IllegalArgumentException("No squad slots remain");
        }

        if (purchasePrice.compareTo(draftSession.getRemainingBudget()) > 0) {
            throw new IllegalArgumentException("Purchase price exceeds remaining budget");
        }

        if (player.getStatus() != PlayerStatus.AVAILABLE) {
            throw new IllegalArgumentException("Player is no longer available");
        }

        if (squadRepository.existsByDraftSessionIdAndPlayerId(
                draftSession.getId(),
                playerId
        )) {
            throw new IllegalArgumentException("Player is already in this squad");
        }

        draftSession.setRemainingBudget(
                draftSession.getRemainingBudget().subtract(purchasePrice)
        );

        draftSession.setRemainingSlots(
                draftSession.getRemainingSlots() - 1
        );

        if (draftSession.getRemainingSlots() == 0) {
            draftSession.setCompletedAt(LocalDateTime.now());
        }

        SquadPlayer squadPlayer = new SquadPlayer(
                draftSession,
                player,
                purchasePrice
        );

        SquadPlayer savedSquadPlayer = squadRepository.save(squadPlayer);

        playerService.updatePlayerStatus(playerId, PlayerStatus.PURCHASED);

        draftSessionRepository.save(draftSession);

        return savedSquadPlayer;

    }
}
