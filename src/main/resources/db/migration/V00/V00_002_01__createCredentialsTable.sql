/*
 -- Rollback script:

 DROP TABLE credentials;

 DELETE FROM flyway_schema_history WHERE version='00.002.01';
 */

CREATE TABLE credentials
(
    id       UUID         NOT NULL,
    login    VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    CONSTRAINT pk_credentials PRIMARY KEY (id)
);

ALTER TABLE credentials
    ADD CONSTRAINT uc_credentials_login UNIQUE (login);

-- DROP INDEX IF EXISTS idx_credentials_login;

CREATE INDEX idx_credentials_login
    ON credentials USING btree
    (login COLLATE pg_catalog."default" ASC NULLS LAST);