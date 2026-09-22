package com.padel.draft.domain.draft;

import com.padel.draft.domain.league.League;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "draft_sessions")
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DraftSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "league_id", nullable = false)
    private League league;

    @Column(nullable = false)
    private BigDecimal startingBudget;

    @Column(nullable = false)
    private BigDecimal remainingBudget;

    @Column(nullable = false)
    private int squadSize;

    @Column(nullable = false)
    private int remainingSlots;

    private LocalDateTime startedAt;

    private LocalDateTime completedAt;

    public DraftSession(League league) {
        this.league = league;
        this.startingBudget = league.getSalaryCap();
        this.remainingBudget = league.getSalaryCap();
        this.squadSize = league.getSquadSize();
        this.remainingSlots = league.getSquadSize();
    }
}
