CREATE TABLE standing.standings
(
    id                 UUID PRIMARY KEY,
    league_id          UUID    NOT NULL,
    team_id            UUID    NOT NULL,
    season             INTEGER NOT NULL,
    rank               INTEGER,
    points             INTEGER,
    goals_diff         INTEGER,
    group_name         VARCHAR(100),
    form               VARCHAR(20),
    status             VARCHAR(30),
    description        VARCHAR(255),

    all_played         INTEGER,
    all_win            INTEGER,
    all_draw           INTEGER,
    all_lose           INTEGER,
    all_goals_for      INTEGER,
    all_goals_against  INTEGER,

    home_played        INTEGER,
    home_win           INTEGER,
    home_draw          INTEGER,
    home_lose          INTEGER,
    home_goals_for     INTEGER,
    home_goals_against INTEGER,

    away_played        INTEGER,
    away_win           INTEGER,
    away_draw          INTEGER,
    away_lose          INTEGER,
    away_goals_for     INTEGER,
    away_goals_against INTEGER,

    manually_edited    BOOLEAN NOT NULL DEFAULT FALSE,
    manual_added       BOOLEAN NOT NULL DEFAULT FALSE,

    created_by         VARCHAR(255),
    created_date       TIMESTAMP,
    last_modified_by   VARCHAR(255),
    last_modified_date TIMESTAMP,

    CONSTRAINT uq_standing_league_team_season UNIQUE (league_id, team_id, season),
    CONSTRAINT fk_standing_league FOREIGN KEY (league_id) REFERENCES league.leagues (id),
    CONSTRAINT fk_standing_team FOREIGN KEY (team_id) REFERENCES team.teams (id)
);

CREATE INDEX idx_standing_league ON standing.standings (league_id);
CREATE INDEX idx_standing_team ON standing.standings (team_id);
CREATE INDEX idx_standing_season ON standing.standings (season);