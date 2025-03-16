CREATE TABLE IF NOT EXISTS aviasales_users
(
    id       BIGSERIAL PRIMARY KEY, -- Use BIGSERIAL for automatic id generation for compatibility
    username VARCHAR(100) NOT NULL, -- Username column with a maximum length of 100
    password VARCHAR(255) NOT NULL, -- Password column with a maximum length of 255
    role     VARCHAR(20)  NOT NULL  -- Role column
);