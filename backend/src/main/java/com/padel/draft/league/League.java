package com.padel.draft.league;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "leagues")
public class League {
    @Id
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(name = "salary_cap", nullable = false)
    private BigDecimal salaryCap;

    @Column(name = "roster_size", nullable = false)
    private int rosterSize;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    protected League() { }

    public League(String name, BigDecimal salaryCap, int rosterSize) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.salaryCap = salaryCap;
        this.rosterSize = rosterSize;
        this.createdAt = OffsetDateTime.now();
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public BigDecimal getSalaryCap() { return salaryCap; }
    public int getRosterSize() { return rosterSize; }
}
