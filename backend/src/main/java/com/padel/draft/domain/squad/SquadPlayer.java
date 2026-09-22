package com.padel.draft.domain.squad;

import com.padel.draft.domain.draft.DraftSession;
import com.padel.draft.domain.player.Player;
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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "squad_players")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class
SquadPlayer {

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
    private BigDecimal purchasePrice;

    @Column(nullable = false)
    private LocalDateTime purchasedAt;

    public SquadPlayer(DraftSession draftSession, Player player, BigDecimal purchasePrice) {
        this.draftSession = draftSession;
        this.player = player;
        this.purchasePrice = purchasePrice;
        this.purchasedAt = LocalDateTime.now();
    }
}
