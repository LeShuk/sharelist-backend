CREATE TABLE credentials
(
    id       UUID         NOT NULL,
    login    VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    CONSTRAINT pk_credentials PRIMARY KEY (id)
);

ALTER TABLE credentials
    ADD CONSTRAINT uc_credentials_login UNIQUE (login);