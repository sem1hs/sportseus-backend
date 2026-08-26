CREATE TABLE transfer.transfers
(
    id                 UUID PRIMARY KEY,
    player_id          UUID         NOT NULL,
    team_in_id         UUID         NOT NULL,
    team_out_id        UUID         NOT NULL,
    transfer_date      DATE         NOT NULL,
    raw_type           VARCHAR(100) NOT NULL,
    transfer_type      VARCHAR(20)  NOT NULL,
    fee                BIGINT,
    manually_edited    BOOLEAN      NOT NULL DEFAULT FALSE,
    created_by         VARCHAR(255),
    created_date       TIMESTAMP,
    last_modified_by   VARCHAR(255),
    last_modified_date TIMESTAMP,
    CONSTRAINT uq_transfer_player_date_in_out
        UNIQUE (player_id, transfer_date, team_in_id, team_out_id),
    CONSTRAINT fk_transfer_player FOREIGN KEY (player_id) REFERENCES player.players (id) ON DELETE CASCADE,
    CONSTRAINT fk_transfer_team_in FOREIGN KEY (team_in_id) REFERENCES team.teams (id),
    CONSTRAINT fk_transfer_team_out FOREIGN KEY (team_out_id) REFERENCES team.teams (id)
);

CREATE INDEX idx_transfer_player ON transfer.transfers (player_id);
CREATE INDEX idx_transfer_team_in ON transfer.transfers (team_in_id);
CREATE INDEX idx_transfer_team_out ON transfer.transfers (team_out_id);