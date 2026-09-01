CREATE TABLE news.news_tags
(
    news_id UUID NOT NULL,
    tag_id  UUID NOT NULL,
    PRIMARY KEY (news_id, tag_id),
    CONSTRAINT fk_news_tags_news FOREIGN KEY (news_id) REFERENCES news.news (id) ON DELETE CASCADE,
    CONSTRAINT fk_news_tags_tag FOREIGN KEY (tag_id) REFERENCES news.tags (id) ON DELETE CASCADE
);