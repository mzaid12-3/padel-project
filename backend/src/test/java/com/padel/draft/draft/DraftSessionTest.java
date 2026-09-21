package com.padel.draft.draft;

import static org.assertj.core.api.Assertions.assertThat;

import com.padel.draft.league.League;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class DraftSessionTest {
    @Test
    void startsWaitingAtTheFirstRoundAndPick() {
        League league = new League("Friday League", new BigDecimal("1000.00"), 8);

        DraftSession session = new DraftSession(league);

        assertThat(session.getId()).isNotNull();
        assertThat(session.getLeague()).isSameAs(league);
        assertThat(session.getStatus()).isEqualTo(DraftStatus.WAITING);
        assertThat(session.getCurrentRound()).isEqualTo(1);
        assertThat(session.getCurrentPickNumber()).isEqualTo(1);
        assertThat(session.getStartedAt()).isNull();
        assertThat(session.getCompletedAt()).isNull();
    }
}
