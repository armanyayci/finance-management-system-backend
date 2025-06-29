-- sql scripts will be here for database first approach

CREATE TABLE USERS (
                       id BIGINT PRIMARY KEY,
                       username VARCHAR(100) UNIQUE NOT NULL,
                       password_hash VARCHAR(255) NOT NULL ,
                       email VARCHAR(255) UNIQUE NOT NULL,
                       first_name VARCHAR(50),
                       last_name VARCHAR(50),
                       status INT CHECK (status in (0,1,2)) DEFAULT 0,
                       last_login datetime,
                       created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                       updated_at datetime DEFAULT GETDATE(),
                       image varbinary(max) null,
                       two_factor_enabled BIT DEFAULT 0
);

CREATE TABLE ROLES (
                       id BIGINT PRIMARY KEY,
                       name VARCHAR(50) UNIQUE NOT NULL,
                       description VARCHAR(255),
);

CREATE TABLE USER_ROLE (
                            user_id BIGINT,
                            role_id BIGINT,
                            PRIMARY KEY (user_id, role_id),
                            FOREIGN KEY (user_id) REFERENCES USERS(id) ON DELETE CASCADE,
                            FOREIGN KEY (role_id) REFERENCES ROLES(id) ON DELETE CASCADE,
);

CREATE TABLE ACCOUNT (
                         id BIGINT PRIMARY KEY,
                         user_id BIGINT,
                         balance BIGINT DEFAULT 0,
                         account_type BIGINT CHECK (account_type in (0, 1, 2, 3)),
                         transfer_code VARCHAR(255) UNIQUE NOT NULL,
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
                          payment_type NVARCHAR(20) CHECK (payment_type in ('CASH','CREDIT_CARD','EXCHANGE')),
                          transaction_time smalldatetime DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE CURRENCY_EXCHANGE (
                                   id BIGINT PRIMARY KEY,
                                   transaction_id BIGINT,
                                   from_currency NVARCHAR(50),
                                   to_currency NVARCHAR(50),
                                   exchange_rate DECIMAL(10, 4) NOT NULL,
                                   exchange_date DATE NOT NULL,
     FOREIGN KEY (transaction_id) references TRANSACTIONS(id) ON DELETE CASCADE
);

CREATE TABLE FINANCIAL_GOALS (
                                   id BIGINT PRIMARY KEY,
                                   description NVARCHAR (200),
                                   account_type BIGINT CHECK (account_type in (0, 1, 2, 3)),
                                   amount DECIMAL(10, 4) NOT NULL,
                                   user_id BIGINT,
                                   created_at DATE NOT NULL,
                                   FOREIGN KEY (user_id) references USERS(id) ON DELETE CASCADE
);

CREATE TABLE EXPENSE (
    id BIGINT PRIMARY KEY,
    name NVARCHAR(255) NOT NULL,
    description NVARCHAR(255),
    amount DECIMAL(19,2) NOT NULL,
    category NVARCHAR(255),
    last_payment_date DATE,
    is_recurring BIT,
    is_paid bit,
    user_id BIGINT NOT NULL,
    CONSTRAINT FK_EXPENSE_USER FOREIGN KEY (user_id) REFERENCES USERS(id)
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

alter table ACCOUNT_ACTIVITIES
    add is_income bit;

ALTER TABLE ACCOUNT_ACTIVITIES
drop column expense_date;

CREATE SEQUENCE account_sequence START WITH 852963 INCREMENT BY 1;
CREATE SEQUENCE user_seq START WITH 564990 INCREMENT BY 1;
CREATE SEQUENCE acc_activity_sequence START WITH 325641 INCREMENT BY 1;
CREATE SEQUENCE transactions_sequence START WITH 256314 INCREMENT BY 1;
CREATE SEQUENCE currency_sequence START WITH 102363 INCREMENT BY 1;
CREATE SEQUENCE currency_exchange_sequence START WITH 405698 INCREMENT BY 1;
CREATE SEQUENCE goal_sequence START WITH 17524 INCREMENT BY 1;
CREATE SEQUENCE expense_sequence START WITH 54802 INCREMENT BY 1;
create table ACCOUNT_CURRENCY(
                         id BIGINT primary key,
                         currency_name varchar(50),
                         amount decimal(15, 2),
                         account_id BIGINT,
                         type varchar(20),
                         FOREIGN KEY (account_id) references ACCOUNT(id) ON DELETE CASCADE
);

CREATE TABLE VERIFICATION_CODES (
    id BIGINT PRIMARY KEY IDENTITY(1,1),
    user_id BIGINT NOT NULL,
    code VARCHAR(10) NOT NULL,
    created_at DATETIME DEFAULT GETDATE() NOT NULL,
    expires_at DATETIME NOT NULL,
    is_used BIT DEFAULT 0 NOT NULL,
    verification_type VARCHAR(20) CHECK (verification_type IN ('TWO_FACTOR_AUTH', 'PASSWORD_RESET', 'EMAIL_VERIFICATION')) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES USERS(id) ON DELETE CASCADE
);
