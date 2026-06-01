ALTER TABLE fk_reim_main
    ADD COLUMN version INT NOT NULL DEFAULT 0 COMMENT 'Optimistic lock version';

UPDATE fk_reim_main
SET version = 0
WHERE version IS NULL;
