CREATE TABLE player.player_statistics
(
    id                 UUID PRIMARY KEY,
    player_id          UUID    NOT NULL,
    team_id            UUID    NOT NULL,
    league_id          UUID    NOT NULL,
    season             INTEGER NOT NULL,
    stats              JSONB   NOT NULL,
    manually_edited    BOOLEAN NOT NULL DEFAULT FALSE,
    manual_added       BOOLEAN NOT NULL DEFAULT FALSE,
    created_by         VARCHAR(255),
    created_date       TIMESTAMP,
    last_modified_by   VARCHAR(255),
    last_modified_date TIMESTAMP,
    CONSTRAINT uq_player_team_league_season UNIQUE (player_id, team_id, league_id, season),
    CONSTRAINT fk_player_stats_player FOREIGN KEY (player_id) REFERENCES player.players (id) ON DELETE CASCADE,
    CONSTRAINT fk_player_stats_team FOREIGN KEY (team_id) REFERENCES team.teams (id),
    CONSTRAINT fk_player_stats_league FOREIGN KEY (league_id) REFERENCES league.leagues (id)
);

CREATE INDEX idx_player_stats_team ON player.player_statistics (team_id);
CREATE INDEX idx_player_stats_league ON player.player_statistics (league_id);