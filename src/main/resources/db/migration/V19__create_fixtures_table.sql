CREATE TABLE fixture.fixtures
(
    id                 UUID PRIMARY KEY,
    external_id        BIGINT      NOT NULL,
    season             INTEGER     NOT NULL,

    -- zaman / durum
    match_date         TIMESTAMP   NOT NULL,
    timestamp_epoch    BIGINT,
    status_short       VARCHAR(10) NOT NULL,
    status_long        VARCHAR(50),
    elapsed            INTEGER,
    extra              INTEGER,
    round              VARCHAR(100),
    referee            VARCHAR(150),

    -- FK'lar
    league_id          UUID        NOT NULL,
    home_team_id       UUID        NOT NULL,
    away_team_id       UUID        NOT NULL,
    venue_id           UUID, -- opsiyonel (nullable)

    -- goals (güncel/final skor) — nullable (oynanmamış maç null, 0 değil)
    goals_home         INTEGER,
    goals_away         INTEGER,

    -- score blokları — nullable
    ht_home            INTEGER,
    ht_away            INTEGER,
    ft_home            INTEGER,
    ft_away            INTEGER,
    et_home            INTEGER,
    et_away            INTEGER,
    pen_home           INTEGER,
    pen_away           INTEGER,

    -- sonuç
    home_winner        BOOLEAN,
    away_winner        BOOLEAN,

    manually_edited    BOOLEAN     NOT NULL DEFAULT FALSE,
    manual_added       BOOLEAN     NOT NULL DEFAULT FALSE,

    created_by         VARCHAR(255),
    created_date       TIMESTAMP,
    last_modified_by   VARCHAR(255),
    last_modified_date TIMESTAMP,

    CONSTRAINT uq_fixture_external_id UNIQUE (external_id),
    CONSTRAINT fk_fixture_league FOREIGN KEY (league_id) REFERENCES league.leagues (id),
    CONSTRAINT fk_fixture_home_team FOREIGN KEY (home_team_id) REFERENCES team.teams (id),
    CONSTRAINT fk_fixture_away_team FOREIGN KEY (away_team_id) REFERENCES team.teams (id),
    CONSTRAINT fk_fixture_venue FOREIGN KEY (venue_id) REFERENCES team.venues (id) ON DELETE SET NULL
);

CREATE INDEX idx_fixture_league ON fixture.fixtures (league_id);
CREATE INDEX idx_fixture_home_team ON fixture.fixtures (home_team_id);
CREATE INDEX idx_fixture_away_team ON fixture.fixtures (away_team_id);
CREATE INDEX idx_fixture_season ON fixture.fixtures (season);