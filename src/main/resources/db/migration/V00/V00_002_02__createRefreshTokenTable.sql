/*
 -- Rollback script:

 DROP TABLE jwt_refresh_token;

 DELETE FROM flyway_schema_history WHERE version='00.002.02';
 */

CREATE TABLE jwt_refresh_token
(
    id         UUID                        NOT NULL,
    login      VARCHAR(255)                NOT NULL,
    token      VARCHAR(255)                NOT NULL,
    expired_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_jwt_refresh_token PRIMARY KEY (id)
);

ALTER TABLE jwt_refresh_token
    ADD CONSTRAINT uc_jwt_refresh_token_login UNIQUE (login);