//package com.padel.draft.player;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//import com.padel.draft.api.ImportResponse;
//import com.padel.draft.league.League;
//import com.padel.draft.league.LeagueRepository;
//import java.math.BigDecimal;
//import java.nio.charset.StandardCharsets;
//import java.util.UUID;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.ArgumentCaptor;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.mock.web.MockMultipartFile;
//
//@ExtendWith(MockitoExtension.class)
//class PlayerCsvImportServiceTest {
//    @Mock LeagueRepository leagueRepository;
//    @Mock PlayerRepository playerRepository;
//
//    @Test
//    void rejectsFilesWithMissingRequiredHeader() {
//        PlayerCsvImportService service = new PlayerCsvImportService(leagueRepository, playerRepository);
//        MockMultipartFile file = new MockMultipartFile("file", "players.csv", "text/csv",
//                "name,rating\nAna,4.5\n".getBytes(StandardCharsets.UTF_8));
//
//        ImportResponse response = service.importPlayers(UUID.randomUUID(), file);
//
//        assertThat(response.importedCount()).isZero();
//        assertThat(response.errors()).singleElement().extracting(ImportResponse.ImportError::field).isEqualTo("header");
//    }
//}
