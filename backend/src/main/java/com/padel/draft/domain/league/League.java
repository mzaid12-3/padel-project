package com.padel.draft.domain.league;

import com.padel.draft.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name = "leagues")
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class League {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;


    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal salaryCap;

    @Column(nullable= false)
    private int squadSize;


    private LocalDateTime createdAt;

    public League(User owner, String name, BigDecimal salaryCap, int squadSize) {
        this.owner = owner;
        this.name = name;
        this.salaryCap = salaryCap;
        this.squadSize = squadSize;
        this.createdAt = LocalDateTime.now();
    }

}
