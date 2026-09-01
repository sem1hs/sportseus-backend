CREATE TABLE team.league_teams
(
    id                 UUID PRIMARY KEY,
    league_id          UUID    NOT NULL,
    team_id            UUID    NOT NULL,
    season             INTEGER NOT NULL,

    created_by         VARCHAR(255),
    created_date       TIMESTAMP,
    last_modified_by   VARCHAR(255),
    last_modified_date TIMESTAMP,

    CONSTRAINT fk_lt_league FOREIGN KEY (league_id)
        REFERENCES league.league (id) ON DELETE CASCADE,
    CONSTRAINT fk_lt_team FOREIGN KEY (team_id)
        REFERENCES team.teams (id) ON DELETE CASCADE,
    CONSTRAINT ux_lt_league_team_season UNIQUE (league_id, team_id, season)
);