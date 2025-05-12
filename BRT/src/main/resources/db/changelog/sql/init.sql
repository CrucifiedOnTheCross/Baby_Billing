CREATE TABLE Tariffs
(
    id          INT PRIMARY KEY,
    is_archived BOOL NOT NULL
);

CREATE TABLE Clients
(
    msisdn      VARCHAR(15) PRIMARY KEY,
    last_name   VARCHAR(50)    NOT NULL,
    first_name  VARCHAR(50)    NOT NULL,
    middle_name VARCHAR(50),
    date        TIMESTAMP      NOT NULL,
    money       DECIMAL(10, 1) NOT NULL,
    tariff_id   INT            NOT NULL,

    FOREIGN KEY (tariff_id) REFERENCES Tariffs (id)
);

CREATE TABLE Calls_Statuses
(
    id          SMALLINT PRIMARY KEY,
    description VARCHAR(50) NOT NULL
);

CREATE TABLE Calls
(
    id                BIGSERIAL PRIMARY KEY,
    type              VARCHAR(2)     NOT NULL,
    msisdn_client     VARCHAR(15) NOT NULL,
    msisdn_partner    VARCHAR(15) NOT NULL,
    start_time        TIMESTAMP   NOT NULL,
    end_time          TIMESTAMP   NOT NULL,
    tariff_id         INT         NOT NULL,
    is_partner_client BOOL        NOT NULL,
    id_status         SMALLINT    NOT NULL,

    FOREIGN KEY (msisdn_client) REFERENCES Clients (msisdn),
    FOREIGN KEY (tariff_id) REFERENCES Tariffs (id),
    FOREIGN KEY (id_status) REFERENCES Calls_Statuses (id)
);

CREATE TABLE Transactions
(
    id          BIGSERIAL PRIMARY KEY,
    msisdn      VARCHAR(15)    NOT NULL,
    money       DECIMAL(10, 1) NOT NULL,
    description VARCHAR(100),
    date        TIMESTAMP      NOT NULL,

    FOREIGN KEY (msisdn) REFERENCES Clients (msisdn)
);
