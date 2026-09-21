package com.padel.draft.api;

import com.padel.draft.draft.DraftSession;
import com.padel.draft.draft.DraftStatus;
import java.time.OffsetDateTime;
import java.util.UUID;

public record DraftSessionResponse(UUID id, UUID leagueId, DraftStatus status, int currentRound,
                                   int currentPickNumber, OffsetDateTime startedAt, OffsetDateTime completedAt) {
    static DraftSessionResponse from(DraftSession session) {
        return new DraftSessionResponse(session.getId(), session.getLeague().getId(), session.getStatus(),
                session.getCurrentRound(), session.getCurrentPickNumber(), session.getStartedAt(), session.getCompletedAt());
    }
}
