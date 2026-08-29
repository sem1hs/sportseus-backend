ALTER TABLE coach.coaches
    ALTER COLUMN external_id DROP NOT NULL;

ALTER TABLE coach.coaches
    ADD COLUMN manual_added BOOLEAN NOT NULL DEFAULT FALSE;