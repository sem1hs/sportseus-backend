CREATE TABLE news.news_relations
(
    id                 UUID PRIMARY KEY,
    news_id            UUID         NOT NULL,
    type               VARCHAR(20)  NOT NULL,
    external_id        INTEGER      NOT NULL,
    name               VARCHAR(200) NOT NULL,
    image_url          TEXT,
    created_by         VARCHAR(255),
    created_date       TIMESTAMP,
    last_modified_by   VARCHAR(255),
    last_modified_date TIMESTAMP,
    CONSTRAINT fk_news_relation_news FOREIGN KEY (news_id) REFERENCES news.news (id) ON DELETE CASCADE
);

CREATE INDEX idx_news_relation_news ON news.news_relations (news_id);
CREATE INDEX idx_news_relation_type ON news.news_relations (type, external_id);