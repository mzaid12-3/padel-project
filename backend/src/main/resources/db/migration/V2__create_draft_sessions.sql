CREATE TABLE draft_sessions (
    id UUID PRIMARY KEY,
    league_id UUID NOT NULL REFERENCES leagues(id) ON DELETE CASCADE,
    status VARCHAR(20) NOT NULL CHECK (status IN ('WAITING', 'ACTIVE', 'PAUSED', 'COMPLETED')),
    current_round INTEGER NOT NULL CHECK (current_round >= 1),
    current_pick_number INTEGER NOT NULL CHECK (current_pick_number >= 1),
    started_at TIMESTAMPTZ,
    completed_at TIMESTAMPTZ
);
