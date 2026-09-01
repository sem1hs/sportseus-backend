ALTER TABLE team.league_teams
    ADD COLUMN manually_edited BOOLEAN NOT NULL DEFAULT FALSE;
ALTER TABLE team.league_teams
    ADD COLUMN manual_added BOOLEAN NOT NULL DEFAULT FALSE;

-- unique yoksa (varsa atla):
ALTER TABLE team.league_teams
    ADD CONSTRAINT uq_league_team_season UNIQUE (league_id, team_id, season);