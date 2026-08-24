CREATE TABLE team.team_statistics
(
    id                 UUID PRIMARY KEY,
    team_id            UUID    NOT NULL,
    league_id          UUID    NOT NULL,
    season             INTEGER NOT NULL,
    stats              JSONB   NOT NULL,
    created_by         VARCHAR(255),
    created_date       TIMESTAMP,
    last_modified_by   VARCHAR(255),
    last_modified_date TIMESTAMP,
    CONSTRAINT fk_ts_team FOREIGN KEY (team_id) REFERENCES team.teams (id) ON DELETE CASCADE,
    CONSTRAINT fk_ts_league FOREIGN KEY (league_id) REFERENCES league.league (id) ON DELETE CASCADE,
    CONSTRAINT ux_ts_team_league_season UNIQUE (team_id, league_id, season)
);