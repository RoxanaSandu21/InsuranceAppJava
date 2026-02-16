-- GEOGRAPHY

INSERT INTO countries (name)
VALUES ('Romania');

INSERT INTO counties (name, country_id)
VALUES ('Bucuresti', 1),
       ('Cluj', 1),
       ('Iasi', 1),
       ('Timis', 1),
       ('Brasov', 1);

INSERT INTO cities (name, county_id)
VALUES ('Bucuresti', 1),
       ('Cluj-Napoca', 2),
       ('Turda', 2),
       ('Iasi', 3),
       ('Timisoara', 4),
       ('Brasov', 5);;

-- CLIENTS

INSERT INTO clients (type,
                     name,
                     identification_number,
                     email,
                     phone_number,
                     address,
                     number_of_changes_of_in)
VALUES ('INDIVIDUAL',
        'Popescu Ion',
        '1960101123456',
        'ion.popescu@gmail.com',
        '0712345678',
        'Str. Libertatii 10, Bucuresti',
        0),

       ('COMPANY',
        'SC SoftTech SRL',
        'RO12345678',
        'office@softtech.ro',
        '0211234567',
        'Bd. Unirii 25, Bucuresti',
        0),

       ('INDIVIDUAL',
        'Maria Ionescu',
        '2850505123456',
        'maria.ionescu@yahoo.com',
        '0722111222',
        'Str. Florilor 5, Timisoara',
        0),

       ('COMPANY',
        'SC Construct Invest SRL',
        'RO88776655',
        'contact@construct.ro',
        '0314445566',
        'Calea Bucuresti 100, Brasov',
        0);

-- BUILDINGS

INSERT INTO buildings (client_id,
                       street,
                       number,
                       city_id,
                       construction_year,
                       type,
                       number_of_floors,
                       surface_area,
                       insured_value,
                       risk_indicator)
VALUES (1,
        'Str. Victoriei',
        12,
        1,
        1998,
        'RESIDENTIAL',
        2,
        120,
        150000.00,
        'EARTHQUAKE'),

       (1,
        'Str. Independentei',
        5,
        2,
        2010,
        'RESIDENTIAL',
        1,
        85,
        98000.00,
        'NONE'),

       (2,
        'Bd. Muncii',
        100,
        3,
        2005,
        'OFFICE',
        4,
        450,
        500000.00,
        'FLOOD'),

       (3,
        'Str. Revolutiei',
        20,
        5,
        2015,
        'RESIDENTIAL',
        3,
        75,
        110000.00,
        'NONE'),

       (3,
        'Str. Lunga',
        15,
        6,
        1980,
        'RESIDENTIAL',
        1,
        150,
        180000.00,
        'EARTHQUAKE'),

       (4,
        'Sos. Cristianului',
        50,
        6,
        2020,
        'INDUSTRIAL',
        1,
        1000,
        2000000.00,
        'FLOOD'),

       (4,
        'Bd. Pipera',
        1, 1,
        2019,
        'OFFICE',
        10,
        2000,
        4500000.00,
        'NONE');

-- BROKERS

INSERT INTO brokers (unique_identifier,
                     broker_code,
                     name,
                     email,
                     phone_number,
                     active,
                     commission_percentage)
VALUES ('f7758d03-1e1c-4f2d-bbba-ec568ffb062e',
        'BR2111',
        'Broker 1',
        'broker1@example.com',
        '0712345678',
        1,
        5),

       ('26833dc8-4bf6-4182-8317-5816b8df8f1f',
        'BR2222',
        'Broker 2',
        'broker2@example.com',
        '0211234567',
        1,
        10),

       ('a1111d03-1e1c-4f2d-bbba-ec568ffb0999',
        'BR3333',
        'Broker 3',
        'broker3@example.com',
        '0733333333',
        1,
        12);

-- CURRENCY

INSERT INTO currencies (code,
                        name,
                        exchange_rate_to_base,
                        active)
VALUES ('EUR',
        'Euro',
        1.0,
        1),
       ('RON',
        'Lei',
        5.0,
        1);

-- FEES

INSERT INTO fee_configurations (name,
                                type,
                                percentage,
                                effective_from,
                                effective_to,
                                active)
VALUES ('Fee1',
        'ADMIN_FEE',
        7.0,
        '2026-01-01',
        '2027-12-31',
        1),
       ('FLOOD',
        'RISK_ADJUSTMENT',
        8,
        '2027-01-01',
        '2027-12-31',
        1);

-- RISKS

INSERT INTO risk_factor_configurations (level,
                                        reference_id,
                                        adjustment_percentage,
                                        active)
VALUES ('COUNTRY',
        1,
        12.0,
        1);


-- POLICIES

INSERT INTO policies (policy_number,
                      client_id,
                      building_id,
                      broker_id,
                      policy_status,
                      start_date,
                      end_date,
                      base_premium,
                      final_premium,
                      currency_id)
VALUES ('POL-2026-CB35C34A',
        1,
        2,
        1,
        'ACTIVE',
        '2026-12-01',
        '2027-12-01',
        1000.0,
        1290.0,
        1),
       ('POL-2026-CB35C34B',
        1,
        2,
        1,
        'ACTIVE',
        '2026-12-01',
        '2027-12-01',
        700.0,
        903.0,
        2),
       ('POL-2026-CB35C34C',
        2,
        3,
        2,
        'ACTIVE',
        '2026-12-01',
        '2027-12-01',
        1500.0,
        1935.0,
        2),
       ('POL-2024-OLD001', 3, 4, 2, 'EXPIRED', '2024-01-01', '2025-01-01', 100.0, 120.0, 1),
       ('POL-2026-CAN011', 3, 5, 3, 'CANCELLED', '2026-02-01', '2027-02-01', 500.0, 650.0, 2),
       ('POL-2026-BIG001', 4, 6, 3, 'ACTIVE', '2026-06-01', '2027-06-01', 2000.0, 2500.0, 1),
       ('POL-2026-BUC001', 4, 7, 1, 'ACTIVE', '2026-08-15', '2027-08-15', 10000.0, 12500.0, 2),
       ('POL-2026-CJ002', 1, 2, 2, 'ACTIVE', '2026-09-01', '2027-09-01', 200.0, 240.0, 2),
       ('POL-2026-TM002', 3, 4, 1, 'ACTIVE', '2026-10-01', '2027-10-01', 150.0, 180.0, 1);





