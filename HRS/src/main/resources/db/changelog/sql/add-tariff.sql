INSERT INTO tariffs (id, name, type, parameters, is_archived)
VALUES (
    11,
    'Классика',
    1,
    '{
        "description": "Исходящие звонки: Абонентам «Ромашка» = 1.5 у.е./минута; Абонентам других операторов = 2.5 у.е./минута. Входящие звонки: бесплатно.",
        "outgoingCalls": {
            "internal": {
                "costPerMinute": 1.5,
                "currency": "у.е."
            },
            "external": {
                "costPerMinute": 2.5,
                "currency": "у.е."
            }
        },
        "incomingCalls": {
            "costPerMinute": 0,
            "currency": "у.е."
        }
    }'::jsonb,
    false
);

INSERT INTO tariffs (id, name, type, parameters, is_archived)
VALUES (
    12,
    'Помесячный',
    2,
    '{
        "description": "50 минут включено на входящие/исходящие звонки, с 51 минуты расчет по тарифу ''Классика''",
        "monthlyFee": 100,
        "currency": "у.е.",
        "includedMinutes": 50,
        "overlimitTariffId": 11,
        "callTypes": {
            "outgoing": {
                "internal": true,
                "external": true
            },
            "incoming": true
        }
    }'::jsonb,
    false
);