CREATE TABLE subscriber
(
    msisdn   VARCHAR(20)  PRIMARY KEY
);

CREATE TABLE call
(
    id             SERIAL PRIMARY KEY,
    type           VARCHAR(2),
    msisdn_client  VARCHAR(20),
    msisdn_partner VARCHAR(20),
    start_time     TIMESTAMP,
    end_time      TIMESTAMP
);