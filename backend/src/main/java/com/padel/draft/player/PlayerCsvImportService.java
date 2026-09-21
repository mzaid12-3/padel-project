package com.padel.draft.player;

import com.padel.draft.api.ImportResponse;
import com.padel.draft.api.ImportResponse.ImportError;
import com.padel.draft.api.NotFoundException;
import com.padel.draft.api.PlayerResponse;
import com.padel.draft.league.League;
import com.padel.draft.league.LeagueRepository;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PlayerCsvImportService {
    private static final Set<String> REQUIRED_HEADERS =
            Set.of("name", "rating", "preferredSide", "cost", "matchesPlayed");

    private final LeagueRepository leagueRepository;
    private final PlayerRepository playerRepository;

    public PlayerCsvImportService(LeagueRepository leagueRepository, PlayerRepository playerRepository) {
        this.leagueRepository = leagueRepository;
        this.playerRepository = playerRepository;
    }

    @Transactional
    public ImportResponse importPlayers(UUID leagueId, MultipartFile file) {
        if (file.isEmpty()) {
            return new ImportResponse(0, List.of(), List.of(new ImportError(0, "file", "CSV file is empty")));
        }
        String filename = file.getOriginalFilename();
        if (filename == null || !filename.toLowerCase(Locale.ROOT).endsWith(".csv")) {
            return new ImportResponse(0, List.of(), List.of(new ImportError(0, "file", "Only .csv files are supported")));
        }

        League league = leagueRepository.findById(leagueId)
                .orElseThrow(() -> new NotFoundException("League " + leagueId + " was not found"));
        List<ImportError> errors = new ArrayList<>();
        List<Player> players = new ArrayList<>();
        Set<String> namesInFile = new HashSet<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
             CSVParser parser = CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).setTrim(true).build().parse(reader)) {
            if (!parser.getHeaderMap().keySet().containsAll(REQUIRED_HEADERS)) {
                return new ImportResponse(0, List.of(), List.of(new ImportError(1, "header",
                        "Required headers: name,rating,preferredSide,cost,matchesPlayed")));
            }
            for (CSVRecord row : parser) {
                parseRow(league, row, namesInFile, players, errors);
            }
        } catch (IOException exception) {
            return new ImportResponse(0, List.of(), List.of(new ImportError(0, "file", "Unable to read CSV file")));
        }

        if (!errors.isEmpty()) {
            return new ImportResponse(0, List.of(), errors);
        }
        List<PlayerResponse> saved = playerRepository.saveAll(players).stream().map(PlayerResponse::from).toList();
        return new ImportResponse(saved.size(), saved, List.of());
    }

    private void parseRow(League league, CSVRecord row, Set<String> namesInFile,
                          List<Player> players, List<ImportError> errors) {
        long line = row.getRecordNumber() + 1;
        try {
            String name = required(row, "name");
            String normalizedName = name.toLowerCase(Locale.ROOT);
            if (!namesInFile.add(normalizedName)) {
                errors.add(new ImportError(line, "name", "Duplicate player name in file"));
                return;
            }
            if (playerRepository.existsByLeagueIdAndNameIgnoreCase(league.getId(), name)) {
                errors.add(new ImportError(line, "name", "Player already exists in this league"));
                return;
            }
            BigDecimal rating = decimal(row, "rating", BigDecimal.ZERO, new BigDecimal("10"));
            PreferredSide side = PreferredSide.valueOf(required(row, "preferredSide").toUpperCase(Locale.ROOT));
            BigDecimal cost = decimal(row, "cost", BigDecimal.ZERO, null);
            int matchesPlayed = integer(row, "matchesPlayed", 0);
            players.add(new Player(league, name, rating, side, cost, matchesPlayed));
        } catch (IllegalArgumentException exception) {
            errors.add(new ImportError(line, "row", exception.getMessage()));
        }
    }

    private String required(CSVRecord row, String column) {
        String value = row.get(column).trim();
        if (value.isEmpty()) {
            throw new IllegalArgumentException(column + " is required");
        }
        return value;
    }

    private BigDecimal decimal(CSVRecord row, String column, BigDecimal minimum, BigDecimal maximum) {
        try {
            BigDecimal value = new BigDecimal(required(row, column));
            if (value.compareTo(minimum) < 0 || (maximum != null && value.compareTo(maximum) > 0)) {
                throw new IllegalArgumentException(column + " is outside the allowed range");
            }
            return value;
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(column + " must be a number");
        }
    }

    private int integer(CSVRecord row, String column, int minimum) {
        try {
            int value = Integer.parseInt(required(row, column));
            if (value < minimum) {
                throw new IllegalArgumentException(column + " must be at least " + minimum);
            }
            return value;
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(column + " must be an integer");
        }
    }
}
