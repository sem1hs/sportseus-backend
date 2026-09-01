CREATE TABLE news.tags
(
    id                 UUID PRIMARY KEY,
    name               VARCHAR(100) NOT NULL,
    slug               VARCHAR(120) NOT NULL,
    created_by         VARCHAR(255),
    created_date       TIMESTAMP,
    last_modified_by   VARCHAR(255),
    last_modified_date TIMESTAMP,
    CONSTRAINT uq_tag_slug UNIQUE (slug)
);