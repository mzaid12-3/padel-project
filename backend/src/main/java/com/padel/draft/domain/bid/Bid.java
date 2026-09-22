package com.padel.draft.domain.bid;

import com.padel.draft.domain.draft.DraftSession;
import com.padel.draft.domain.league.League;
import com.padel.draft.domain.player.Player;
import com.padel.draft.domain.player.PlayerStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bids")
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Bid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "draft_session_id", nullable = false)
    private DraftSession draftSession;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private BigDecimal recommendationMaxBid;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BidRecommendation bidRecommendation;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public Bid(
            DraftSession draftSession,
            Player player,
            BigDecimal amount,
            BigDecimal recommendationMaxBid,
            BidRecommendation bidRecommendation
    ) {
        if (amount == null || amount.signum() < 0) {
            throw new IllegalArgumentException("Bid amount cannot be negative");
        }

        if (recommendationMaxBid == null || recommendationMaxBid.signum() < 0) {
            throw new IllegalArgumentException("Recommended maximum bid cannot be negative");
        }

        this.draftSession = draftSession;
        this.player = player;
        this.amount = amount;
        this.recommendationMaxBid = recommendationMaxBid;
        this.bidRecommendation = bidRecommendation;


    }
}
