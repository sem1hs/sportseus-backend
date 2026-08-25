CREATE TABLE player.players
(
    id                 UUID PRIMARY KEY,
    external_id        BIGINT       NOT NULL,
    name               VARCHAR(255) NOT NULL,
    first_name         VARCHAR(255),
    last_name          VARCHAR(255),
    age                INTEGER,
    birth_date         DATE,
    birth_place        VARCHAR(255),
    birth_country      VARCHAR(255),
    nationality        VARCHAR(255),
    height             VARCHAR(255),
    weight             VARCHAR(255),
    photo              TEXT,
    created_by         VARCHAR(255),
    created_date       TIMESTAMP,
    last_modified_by   VARCHAR(255),
    last_modified_date TIMESTAMP,
    CONSTRAINT uq_players_external_id UNIQUE (external_id)
);