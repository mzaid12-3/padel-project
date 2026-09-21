package com.padel.draft.draft;

import com.padel.draft.league.League;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "draft_sessions")
public class DraftSession {
    @Id
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "league_id", nullable = false)
    private League league;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DraftStatus status;

    @Column(name = "current_round", nullable = false)
    private int currentRound;

    @Column(name = "current_pick_number", nullable = false)
    private int currentPickNumber;

    @Column(name = "started_at")
    private OffsetDateTime startedAt;

    @Column(name = "completed_at")
    private OffsetDateTime completedAt;

    protected DraftSession() { }

    public DraftSession(League league) {
        this.id = UUID.randomUUID();
        this.league = league;
        this.status = DraftStatus.WAITING;
        this.currentRound = 1;
        this.currentPickNumber = 1;
    }

    public UUID getId() { return id; }
    public League getLeague() { return league; }
    public DraftStatus getStatus() { return status; }
    public int getCurrentRound() { return currentRound; }
    public int getCurrentPickNumber() { return currentPickNumber; }
    public OffsetDateTime getStartedAt() { return startedAt; }
    public OffsetDateTime getCompletedAt() { return completedAt; }
}
