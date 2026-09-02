ALTER TABLE news.news
    ADD COLUMN featured BOOLEAN NOT NULL DEFAULT FALSE;

CREATE INDEX idx_news_featured ON news.news (publish_date DESC) WHERE featured = true;