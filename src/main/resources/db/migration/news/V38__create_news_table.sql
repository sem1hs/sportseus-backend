CREATE TABLE news.news
(
    id                 UUID PRIMARY KEY,
    title              VARCHAR(255) NOT NULL,
    slug               VARCHAR(300) NOT NULL,
    content            TEXT         NOT NULL,
    image_url          TEXT,
    category           VARCHAR(30)  NOT NULL,
    status             VARCHAR(20)  NOT NULL,
    breaking           BOOLEAN      NOT NULL DEFAULT FALSE,
    publish_date       TIMESTAMP,
    author_id          UUID         NOT NULL,
    created_by         VARCHAR(255),
    created_date       TIMESTAMP,
    last_modified_by   VARCHAR(255),
    last_modified_date TIMESTAMP,
    CONSTRAINT uq_news_slug UNIQUE (slug),
    CONSTRAINT fk_news_author FOREIGN KEY (author_id) REFERENCES auth.users (id)
);

CREATE INDEX idx_news_status ON news.news (status);
CREATE INDEX idx_news_category ON news.news (category);
CREATE INDEX idx_news_author ON news.news (author_id);