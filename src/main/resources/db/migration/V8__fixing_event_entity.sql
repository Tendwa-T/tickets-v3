ALTER TABLE events
    DROP FOREIGN KEY events_users_id_fk_2;

ALTER TABLE events
    DROP COLUMN created_by;

ALTER TABLE events
    DROP COLUMN status;

ALTER TABLE events
    ADD created_by BLOB NOT NULL;

ALTER TABLE events
    ADD status VARCHAR(255) DEFAULT 'DRAFT' NOT NULL;

ALTER TABLE events
    ALTER status SET DEFAULT 'DRAFT';