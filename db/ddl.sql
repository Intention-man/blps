CREATE TABLE IF NOT EXISTS aviasales_users
(
    id       BIGSERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role     VARCHAR(20)  NOT NULL
);

CREATE TABLE IF NOT EXISTS aviasales_airlines (
    airline_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS aviasales_cities (
    city_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS aviasales_tickets (
    ticket_id BIGSERIAL PRIMARY KEY,
    airline_id BIGINT NOT NULL,
    service_class VARCHAR(10) NOT NULL,
    price INTEGER NOT NULL,
    available_seats INTEGER NOT NULL,
    hours INTEGER NOT NULL,
    flight_number VARCHAR(20) NOT NULL,
    departure_city_id BIGINT NOT NULL,
    departure_date DATE NOT NULL,
    departure_time TIME NOT NULL,
    arrival_city_id BIGINT NOT NULL,
    arrival_date DATE NOT NULL,
    arrival_time TIME NOT NULL,
    FOREIGN KEY (airline_id) REFERENCES aviasales_airlines(airline_id),
    FOREIGN KEY (departure_city_id) REFERENCES aviasales_cities(city_id),
    FOREIGN KEY (arrival_city_id) REFERENCES aviasales_cities(city_id)
);