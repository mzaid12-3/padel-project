CREATE TABLE leagues (
    id UUID PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    salary_cap NUMERIC(12, 2) NOT NULL CHECK (salary_cap > 0),
    roster_size INTEGER NOT NULL CHECK (roster_size BETWEEN 1 AND 30),
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE teams (
    id UUID PRIMARY KEY,
    league_id UUID NOT NULL REFERENCES leagues(id) ON DELETE CASCADE,
    name VARCHAR(120) NOT NULL,
    owner_name VARCHAR(120) NOT NULL,
    CONSTRAINT uq_team_name_per_league UNIQUE (league_id, name)
);

CREATE TABLE players (
    id UUID PRIMARY KEY,
    league_id UUID NOT NULL REFERENCES leagues(id) ON DELETE CASCADE,
    name VARCHAR(120) NOT NULL,
    rating NUMERIC(4, 2) NOT NULL CHECK (rating >= 0 AND rating <= 10),
    preferred_side VARCHAR(10) NOT NULL CHECK (preferred_side IN ('LEFT', 'RIGHT', 'BOTH')),
    cost NUMERIC(12, 2) NOT NULL CHECK (cost >= 0),
    matches_played INTEGER NOT NULL DEFAULT 0 CHECK (matches_played >= 0),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT uq_player_name_per_league UNIQUE (league_id, name)
);
