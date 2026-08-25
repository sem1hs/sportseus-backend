CREATE TABLE player.player_teams
(
    id                 UUID PRIMARY KEY,
    player_id          UUID    NOT NULL,
    team_id            UUID    NOT NULL,
    season             INTEGER NOT NULL,
    shirt_number       INTEGER,
    position           VARCHAR(255),
    created_by         VARCHAR(255),
    created_date       TIMESTAMP,
    last_modified_by   VARCHAR(255),
    last_modified_date TIMESTAMP,
    CONSTRAINT uq_player_team_season UNIQUE (player_id, team_id, season),
    CONSTRAINT fk_player_teams_player FOREIGN KEY (player_id) REFERENCES player.players (id) ON DELETE CASCADE,
    CONSTRAINT fk_player_teams_team FOREIGN KEY (team_id) REFERENCES team.teams (id)
);

CREATE INDEX idx_player_teams_team ON player.player_teams (team_id);