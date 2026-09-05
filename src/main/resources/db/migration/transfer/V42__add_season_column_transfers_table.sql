-- season kolonu ekle (nullable başlat, doldur, non-null yap)
ALTER TABLE transfer.transfers
    ADD COLUMN season INTEGER;

-- mevcut kayıtları doldur: ay >= 6 ise yıl, değilse yıl-1
UPDATE transfer.transfers
SET season = CASE
                 WHEN EXTRACT(MONTH FROM transfer_date) >= 6 THEN EXTRACT(YEAR FROM transfer_date)
                 ELSE EXTRACT(YEAR FROM transfer_date) - 1
    END;

-- artık non-null
ALTER TABLE transfer.transfers
    ALTER COLUMN season SET NOT NULL;

-- filtreleme için index
CREATE INDEX idx_transfer_season ON transfer.transfers (season);