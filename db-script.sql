-- sql scripts will be here for database first approach

CREATE TABLE USERS (
                       id BIGINT PRIMARY KEY,
                       username VARCHAR(100) UNIQUE NOT NULL,
                       password_hash VARCHAR(255) NOT NULL ,
                       email VARCHAR(255) UNIQUE NOT NULL,
                       first_name VARCHAR(50),
                       last_name VARCHAR(50),
                       status INT CHECK (status in (0,1,2)) DEFAULT 0,
                       created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                       updated_at datetime DEFAULT GETDATE()
);

CREATE TABLE ROLES (
                       id BIGINT PRIMARY KEY,
                       name VARCHAR(50) UNIQUE NOT NULL,
                       description VARCHAR(255),
);

CREATE TABLE USER_ROLES (
                            user_id BIGINT,
                            role_id BIGINT,
                            PRIMARY KEY (user_id, role_id),
                            assigned_at smalldatetime DEFAULT CURRENT_TIMESTAMP
                            FOREIGN KEY (user_id) REFERENCES USERS(id) ON DELETE CASCADE,
                            FOREIGN KEY (role_id) REFERENCES ROLES(id) ON DELETE CASCADE,
);

CREATE TABLE ACCOUNT (
                         id BIGINT PRIMARY KEY,
                         user_id BIGINT,
                         balance BIGINT DEFAULT 0,
                         account_type BIGINT CHECK (account_type in (0, 1, 2, 3)),
                         transfer_code BIGINT UNIQUE NOT NULL,
                         FOREIGN KEY (user_id) REFERENCES USERS(id) ON DELETE CASCADE,
);

CREATE TABLE ACCOUNT_ACTIVITIES (
                          id BIGINT PRIMARY KEY,
                          transaction_id BIGINT,
                          description VARCHAR(255),
                          expense_date smalldatetime DEFAULT CURRENT_TIMESTAMP,
                          account_id BIGINT,
    FOREIGN KEY (account_id) REFERENCES ACCOUNT(id) ON DELETE CASCADE,
    FOREIGN KEY (transaction_id) REFERENCES TRANSACTIONS(id) ON DELETE CASCADE
);



CREATE TABLE TRANSACTIONS (
                          id BIGINT PRIMARY KEY,
                          amount DECIMAL(15, 2),
                          category VARCHAR(50),
                          payment_type NVARCHAR CHECK (payment_type in ('CASH','CREDIT_CARD','EXCHANGE')),
                          transaction_time smalldatetime DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE CURRENCY_EXCHANGE (
                                   id BIGINT PRIMARY KEY,
                                   transaction_id BIGINT,
                                   from_currency NVARCHAR CHECK (from_currency in ('TR', 'USD', 'EURO')),
                                   to_currency NVARCHAR CHECK (from_currency in ('TR', 'USD', 'EURO')),
                                   exchange_rate DECIMAL(10, 4) NOT NULL,
                                   exchange_date DATE NOT NULL,
     FOREIGN KEY (transaction_id) references TRANSACTIONS(id) ON DELETE CASCADE
);

CREATE TRIGGER SetUpdatedAt
    ON USERS
    AFTER UPDATE
    AS
BEGIN
    UPDATE USERS
    SET updated_at = GETDATE()
    WHERE id IN (SELECT DISTINCT id FROM Inserted);
END;


CREATE SEQUENCE account_sequence START WITH 852963 INCREMENT BY 1;
CREATE SEQUENCE user_sequence START WITH 564982 INCREMENT BY 1;