package com.padel.draft.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.padel.draft.draft.DraftSession;
import com.padel.draft.draft.DraftSessionRepository;
import com.padel.draft.draft.DraftStatus;
import com.padel.draft.league.League;
import com.padel.draft.league.LeagueRepository;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class DraftSessionControllerTest {
    @Mock LeagueRepository leagueRepository;
    @Mock DraftSessionRepository draftSessionRepository;

    @Test
    void createsAWaitingDraftSessionForTheLeague() {
        League league = new League("Friday League", new BigDecimal("1000.00"), 8);
        DraftSession session = new DraftSession(league);
        when(leagueRepository.findById(league.getId())).thenReturn(Optional.of(league));
        when(draftSessionRepository.save(any(DraftSession.class))).thenReturn(session);
        DraftSessionController controller = new DraftSessionController(leagueRepository, draftSessionRepository);

        var response = controller.create(league.getId());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().leagueId()).isEqualTo(league.getId());
        assertThat(response.getBody().status()).isEqualTo(DraftStatus.WAITING);
    }
}
