package com.padel.draft.team;

import com.padel.draft.league.League;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "teams")
public class Team {
    @Id
    private UUID id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "league_id")
    private League league;
    private String name;
    private String ownerName;

    protected Team() { }
}
