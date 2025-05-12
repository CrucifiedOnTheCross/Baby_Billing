-- Создание таблицы тарифов
CREATE TABLE tariffs
(
    id          INT PRIMARY KEY,
    name        VARCHAR(50) NOT NULL,
    type        SMALLINT    NOT NULL UNIQUE,
    parameters  JSONB       NOT NULL,
    is_archived BOOLEAN     NOT NULL DEFAULT FALSE
);

-- Пример: индекс для быстрого поиска по is_archived
CREATE INDEX idx_tariffs_is_archived ON tariffs (is_archived);

-- Создание таблицы звонков
CREATE TABLE calls
(
    id                BIGSERIAL PRIMARY KEY,
    type              VARCHAR(2)        NOT NULL CHECK (type IN ('01', '02')),
    msisdn_client     VARCHAR(15)    NOT NULL,
    start_time        TIMESTAMP      NOT NULL,
    end_time          TIMESTAMP      NOT NULL,
    tariff_id         INT            NOT NULL REFERENCES tariffs (id),
    is_partner_client BOOLEAN        NOT NULL,
    money             DECIMAL(10, 1) NOT NULL
);

-- Индекс для ускорения запросов по абоненту и времени
CREATE INDEX idx_calls_msisdn_time ON calls (msisdn_client, start_time);

-- Создание таблицы истории тарификации
CREATE TABLE month_tariff_history
(
    id             BIGSERIAL PRIMARY KEY,
    msisdn         VARCHAR(15) NOT NULL,
    period_start   TIMESTAMP   NOT NULL,
    period_end     TIMESTAMP   NOT NULL,
    tariff_id      INT         NOT NULL REFERENCES tariffs (id),
    minute_balance INT         NOT NULL
);

-- Индекс для ускорения выборки истории тарификации по msisdn и дате
CREATE INDEX idx_mth_msisdn_period ON month_tariff_history (msisdn, period_start, period_end);
