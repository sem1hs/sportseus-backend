CREATE TABLE lineup.fixture_lineups
(
    id                 UUID PRIMARY KEY,
    fixture_id         UUID    NOT NULL,
    team_id            UUID    NOT NULL,
    formation          VARCHAR(20),
    manually_edited    BOOLEAN NOT NULL DEFAULT FALSE,

    -- coach snapshot (FK yok)
    coach_external_id  INTEGER,
    coach_name         VARCHAR(150),
    coach_photo        TEXT,

    created_by         VARCHAR(255),
    created_date       TIMESTAMP,
    last_modified_by   VARCHAR(255),
    last_modified_date TIMESTAMP,

    CONSTRAINT uq_lineup_fixture_team UNIQUE (fixture_id, team_id),
    CONSTRAINT fk_lineup_fixture FOREIGN KEY (fixture_id) REFERENCES fixture.fixtures (id) ON DELETE CASCADE,
    CONSTRAINT fk_lineup_team FOREIGN KEY (team_id) REFERENCES team.teams (id)
);

CREATE INDEX idx_lineup_fixture ON lineup.fixture_lineups (fixture_id);
CREATE INDEX idx_lineup_team ON lineup.fixture_lineups (team_id);