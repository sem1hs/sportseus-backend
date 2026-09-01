CREATE TABLE lineup.lineup_players
(
    id                 UUID PRIMARY KEY,
    lineup_id          UUID    NOT NULL,

    player_external_id INTEGER NOT NULL,
    player_name        VARCHAR(150),
    shirt_number       INTEGER,
    position           VARCHAR(5),
    is_starter         BOOLEAN NOT NULL,

    created_by         VARCHAR(255),
    created_date       TIMESTAMP,
    last_modified_by   VARCHAR(255),
    last_modified_date TIMESTAMP,

    CONSTRAINT fk_lineup_player_lineup FOREIGN KEY (lineup_id) REFERENCES lineup.fixture_lineups (id) ON DELETE CASCADE
);

CREATE INDEX idx_lineup_player_lineup ON lineup.lineup_players (lineup_id);