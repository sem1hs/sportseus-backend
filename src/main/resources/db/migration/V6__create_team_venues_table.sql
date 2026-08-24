CREATE TABLE team.venues
(
    id                 UUID PRIMARY KEY,
    external_id        INTEGER NOT NULL,
    name               VARCHAR(150),
    address            VARCHAR(255),
    city               VARCHAR(100),
    capacity           INTEGER,
    surface            VARCHAR(50),
    image_url          VARCHAR(500),

    created_by         VARCHAR(255),
    created_date       TIMESTAMP,
    last_modified_by   VARCHAR(255),
    last_modified_date TIMESTAMP,

    CONSTRAINT ux_venues_external_id UNIQUE (external_id)
);