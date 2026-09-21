package com.padel.draft.player;

import com.padel.draft.league.League;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "players")
public class Player {
    @Id
    private UUID id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "league_id")
    private League league;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private BigDecimal rating;
    @Enumerated(EnumType.STRING)
    @Column(name = "preferred_side", nullable = false)
    private PreferredSide preferredSide;
    @Column(nullable = false)
    private BigDecimal cost;
    @Column(name = "matches_played", nullable = false)
    private int matchesPlayed;
    @Column(nullable = false)
    private boolean active;

    protected Player() { }

    public Player(League league, String name, BigDecimal rating, PreferredSide preferredSide,
                  BigDecimal cost, int matchesPlayed) {
        this.id = UUID.randomUUID();
        this.league = league;
        this.name = name;
        this.rating = rating;
        this.preferredSide = preferredSide;
        this.cost = cost;
        this.matchesPlayed = matchesPlayed;
        this.active = true;
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public BigDecimal getRating() { return rating; }
    public PreferredSide getPreferredSide() { return preferredSide; }
    public BigDecimal getCost() { return cost; }
    public int getMatchesPlayed() { return matchesPlayed; }
    public boolean isActive() { return active; }
}
