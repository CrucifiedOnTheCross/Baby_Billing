CREATE TABLE users
(
    id       BIGSERIAL PRIMARY KEY,
    role     VARCHAR(10) NOT NULL CHECK (role IN ('manager', 'user')),
    login    VARCHAR(15) NOT NULL UNIQUE,
    password VARCHAR(255)
);