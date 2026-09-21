package com.padel.draft.api;

import com.padel.draft.league.League;
import com.padel.draft.league.LeagueRepository;
import com.padel.draft.player.PlayerRepository;
import com.padel.draft.player.PlayerCsvImportService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/leagues")
public class LeagueController {
    private final LeagueRepository leagueRepository;
    private final PlayerRepository playerRepository;
    private final PlayerCsvImportService playerCsvImportService;

    public LeagueController(LeagueRepository leagueRepository, PlayerRepository playerRepository,
                            PlayerCsvImportService playerCsvImportService) {
        this.leagueRepository = leagueRepository;
        this.playerRepository = playerRepository;
        this.playerCsvImportService = playerCsvImportService;
    }

    @PostMapping
    ResponseEntity<LeagueResponse> create(@Valid @RequestBody CreateLeagueRequest request) {
        League league = leagueRepository.save(new League(request.name().trim(), request.salaryCap(), request.rosterSize()));
        return ResponseEntity.status(HttpStatus.CREATED).body(LeagueResponse.from(league));
    }

    @GetMapping("/{leagueId}/players")
    List<PlayerResponse> listPlayers(@PathVariable UUID leagueId) {
        requireLeague(leagueId);
        return playerRepository.findByLeagueIdOrderByNameAsc(leagueId).stream().map(PlayerResponse::from).toList();
    }

    @PostMapping(value = "/{leagueId}/players/import", consumes = "multipart/form-data")
    ResponseEntity<ImportResponse> importPlayers(@PathVariable UUID leagueId, @RequestPart("file") MultipartFile file) {
        requireLeague(leagueId);
        ImportResponse response = playerCsvImportService.importPlayers(leagueId, file);
        return ResponseEntity.status(response.errors().isEmpty() ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST).body(response);
    }

    private League requireLeague(UUID leagueId) {
        return leagueRepository.findById(leagueId)
                .orElseThrow(() -> new NotFoundException("League " + leagueId + " was not found"));
    }
}
