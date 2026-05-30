ALTER TABLE fk_reim_main
  ADD COLUMN bill_date varchar(20) NULL COMMENT 'Bill date' AFTER creation_time;

UPDATE fk_reim_main
SET bill_date = LEFT(creation_time, 10)
WHERE bill_date IS NULL
  AND creation_time IS NOT NULL
  AND creation_time <> '';
