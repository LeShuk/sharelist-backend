CREATE TABLE jwt_refresh_token
(
    id         UUID                        NOT NULL,
    login      VARCHAR(255)                NOT NULL,
    token      VARCHAR(255)                NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_jwt_refresh_token PRIMARY KEY (id)
);

ALTER TABLE jwt_refresh_token
    ADD CONSTRAINT uc_jwt_refresh_token_login UNIQUE (login);