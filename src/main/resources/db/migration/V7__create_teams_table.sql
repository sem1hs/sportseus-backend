CREATE TABLE team.teams
(
    id                 UUID PRIMARY KEY,
    external_id        INTEGER      NOT NULL,
    name               VARCHAR(150) NOT NULL,
    code               VARCHAR(10),
    country            VARCHAR(100),
    founded            INTEGER,
    national           BOOLEAN      NOT NULL DEFAULT FALSE,
    logo_url           VARCHAR(500),
    venue_id           UUID,
    manually_edited    BOOLEAN      NOT NULL DEFAULT FALSE,
    created_by         VARCHAR(255),
    created_date       TIMESTAMP,
    last_modified_by   VARCHAR(255),
    last_modified_date TIMESTAMP,

    CONSTRAINT ux_teams_external_id UNIQUE (external_id),
    CONSTRAINT fk_teams_venue FOREIGN KEY (venue_id)
        REFERENCES team.venues (id) ON DELETE SET NULL
);