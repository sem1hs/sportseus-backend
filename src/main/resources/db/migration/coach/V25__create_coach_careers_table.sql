CREATE TABLE coach.coach_careers
(
    id                 UUID PRIMARY KEY,
    coach_id           UUID         NOT NULL,
    team_external_id   INTEGER      NOT NULL,
    team_name          VARCHAR(150) NOT NULL,
    team_logo          TEXT,
    start_date         DATE         NOT NULL,
    end_date           DATE,
    manually_edited    BOOLEAN      NOT NULL DEFAULT FALSE,
    manual_added       BOOLEAN      NOT NULL DEFAULT FALSE,
    created_by         VARCHAR(255),
    created_date       TIMESTAMP,
    last_modified_by   VARCHAR(255),
    last_modified_date TIMESTAMP,
    CONSTRAINT uq_coach_career UNIQUE (coach_id, team_external_id, start_date),
    CONSTRAINT fk_coach_career_coach FOREIGN KEY (coach_id) REFERENCES coach.coaches (id) ON DELETE CASCADE
);

CREATE INDEX idx_coach_career_coach ON coach.coach_careers (coach_id);