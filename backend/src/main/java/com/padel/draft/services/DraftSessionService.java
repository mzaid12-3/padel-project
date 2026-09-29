package com.padel.draft.services;

import com.padel.draft.domain.draft.DraftSession;
import com.padel.draft.domain.league.League;
import com.padel.draft.repositories.DraftSessionRepository;
import com.padel.draft.repositories.LeagueRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DraftSessionService {
    private final LeagueService leagueService;
    private final DraftSessionRepository draftSessionRepository;


    public DraftSessionService(DraftSessionRepository draftSessionRepository,LeagueService leagueService) {
        this.draftSessionRepository = draftSessionRepository;

        this.leagueService = leagueService;
    }

    public DraftSession startDraft(long leagueId){
        League league = leagueService.getLeagueById(leagueId);

        Boolean activeDraftExits = draftSessionRepository.existsByLeagueIdAndCompletedAtIsNull(leagueId);
        if(activeDraftExits){
            throw new IllegalArgumentException("Draft session already exists");
        }

        DraftSession draftSession = new DraftSession(league);
        draftSession.setStartedAt(LocalDateTime.now());
        return draftSessionRepository.save(draftSession);
    }
    public DraftSession getActiveDraftForLeague(Long leagueId) {
        leagueService.getLeagueById(leagueId);

        DraftSession draftSession = draftSessionRepository
                .findFirstByLeagueIdOrderByStartedAtDesc(leagueId)
                .orElseThrow(() ->
                        new IllegalArgumentException("No draft session found for this league")
                );

        if (draftSession.getCompletedAt() != null) {
            throw new IllegalArgumentException("This league has no active draft");
        }

        return draftSession;
    }
}
