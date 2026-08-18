CREATE TABLE league.league
(
    id                 UUID PRIMARY KEY,
    external_id        INTEGER      NOT NULL,
    name               VARCHAR(150) NOT NULL,
    type               VARCHAR(20)  NOT NULL,
    logo_url           VARCHAR(500),
    country_name       VARCHAR(100) NOT NULL,
    country_code       VARCHAR(10)  NOT NULL,
    country_flag       VARCHAR(500) NOT NULL,
    season             INTEGER      NOT NULL,

    created_by         VARCHAR(255),
    created_date       TIMESTAMP,
    last_modified_by   VARCHAR(255),
    last_modified_date TIMESTAMP,

    CONSTRAINT ux_league_external_season UNIQUE (external_id, season),
    CONSTRAINT chk_league_type CHECK (type IN ('LEAGUE', 'CUP'))
);