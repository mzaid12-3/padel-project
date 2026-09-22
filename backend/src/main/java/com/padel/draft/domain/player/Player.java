package com.padel.draft.domain.player;

import com.padel.draft.domain.league.League;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "players")
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "league_id", nullable = false)
    private League league;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal rating;

    @Column(nullable = false)
    private String preferredSide;

    @Column(nullable = false)
    private BigDecimal basePrice;

    @Column(nullable = false)
    private int matchesPlayed;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PlayerStatus status;

    public Player(League league, String name, BigDecimal rating, String preferredSide,
                  BigDecimal basePrice, int matchesPlayed) {
        this.league = league;
        this.name = name;
        this.rating = rating;
        this.preferredSide = preferredSide;
        this.basePrice = basePrice;
        this.matchesPlayed = matchesPlayed;
        this.status = PlayerStatus.AVAILABLE;
    }
}
