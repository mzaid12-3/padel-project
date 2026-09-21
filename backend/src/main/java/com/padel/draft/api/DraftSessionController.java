package com.padel.draft.api;

import com.padel.draft.draft.DraftSession;
import com.padel.draft.draft.DraftSessionRepository;
import com.padel.draft.league.League;
import com.padel.draft.league.LeagueRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/leagues/{leagueId}/draft-sessions")
public class DraftSessionController {
    private final LeagueRepository leagueRepository;
    private final DraftSessionRepository draftSessionRepository;

    public DraftSessionController(LeagueRepository leagueRepository, DraftSessionRepository draftSessionRepository) {
        this.leagueRepository = leagueRepository;
        this.draftSessionRepository = draftSessionRepository;
    }

    @PostMapping
    public ResponseEntity<DraftSessionResponse> create(@PathVariable UUID leagueId) {
        League league = requireLeague(leagueId);
        DraftSession session = draftSessionRepository.save(new DraftSession(league));
        return ResponseEntity.status(HttpStatus.CREATED).body(DraftSessionResponse.from(session));
    }

    @GetMapping
    public List<DraftSessionResponse> list(@PathVariable UUID leagueId) {
        requireLeague(leagueId);
        return draftSessionRepository.findByLeagueIdOrderByIdAsc(leagueId).stream()
                .map(DraftSessionResponse::from)
                .toList();
    }

    private League requireLeague(UUID leagueId) {
        return leagueRepository.findById(leagueId)
                .orElseThrow(() -> new NotFoundException("League " + leagueId + " was not found"));
    }
}
