CREATE TABLE coach.coaches
(
    id                 UUID PRIMARY KEY,
    external_id        INTEGER      NOT NULL,
    name               VARCHAR(150) NOT NULL,
    first_name         VARCHAR(100),
    last_name          VARCHAR(100),
    age                INTEGER,
    birth_date         DATE,
    birth_place        VARCHAR(150),
    birth_country      VARCHAR(100),
    nationality        VARCHAR(100),
    height             VARCHAR(20),
    weight             VARCHAR(20),
    photo              TEXT,
    manually_edited    BOOLEAN      NOT NULL DEFAULT FALSE,
    created_by         VARCHAR(255),
    created_date       TIMESTAMP,
    last_modified_by   VARCHAR(255),
    last_modified_date TIMESTAMP,
    CONSTRAINT uq_coach_external_id UNIQUE (external_id)
);