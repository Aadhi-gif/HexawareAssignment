CREATE DATABASE IF NOT EXISTS BankFundsDB;
USE BankFundsDB;

DROP PROCEDURE IF EXISTS SimulateTransferFailure;
DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS accounts;

CREATE TABLE accounts (
    account_id INT PRIMARY KEY,
    account_holder VARCHAR(100) NOT NULL,
    balance DECIMAL(12,2) NOT NULL,
    status ENUM('ACTIVE', 'INACTIVE') NOT NULL DEFAULT 'ACTIVE',
    CHECK (balance >= 0)
) ENGINE = InnoDB;

CREATE TABLE transactions (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    account_id INT NOT NULL,
    related_account_id INT NULL,
    transaction_type ENUM('CREDIT', 'DEBIT', 'FEE') NOT NULL,
    amount DECIMAL(12,2) NOT NULL,
    transaction_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    description VARCHAR(200),
    CONSTRAINT fk_transactions_account
        FOREIGN KEY (account_id) REFERENCES accounts(account_id),
    CHECK (amount > 0)
) ENGINE = InnoDB;

INSERT INTO accounts (account_id, account_holder, balance, status) VALUES
(101, 'Arun', 10000.00, 'ACTIVE'),
(102, 'Bala', 5000.00, 'ACTIVE'),
(103, 'Charan', 2500.00, 'INACTIVE'),
(999, 'Bank Fee Account', 0.00, 'ACTIVE');

SELECT * FROM accounts ORDER BY account_id;

DROP PROCEDURE IF EXISTS TransferFunds;

DELIMITER //

CREATE PROCEDURE TransferFunds(
    IN p_from_account INT,
    IN p_to_account INT,
    IN p_amount DECIMAL(12,2)
)
BEGIN
    DECLARE v_from_balance DECIMAL(12,2);
    DECLARE v_from_status VARCHAR(20);
    DECLARE v_to_status VARCHAR(20);

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    START TRANSACTION;

    SELECT balance, status
    INTO v_from_balance, v_from_status
    FROM accounts
    WHERE account_id = p_from_account
    FOR UPDATE;

    SELECT status
    INTO v_to_status
    FROM accounts
    WHERE account_id = p_to_account
    FOR UPDATE;

    IF v_from_status <> 'ACTIVE' OR v_to_status <> 'ACTIVE' THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Both accounts must be ACTIVE';
    END IF;

    IF p_amount <= 0 OR v_from_balance < p_amount THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Invalid amount or insufficient balance';
    END IF;

    UPDATE accounts
    SET balance = balance - p_amount
    WHERE account_id = p_from_account;

    UPDATE accounts
    SET balance = balance + p_amount
    WHERE account_id = p_to_account;

    INSERT INTO transactions
    (account_id, related_account_id, transaction_type, amount, description)
    VALUES
    (p_from_account, p_to_account, 'DEBIT', p_amount, CONCAT('Transfer to account ', p_to_account)),
    (p_to_account, p_from_account, 'CREDIT', p_amount, CONCAT('Transfer from account ', p_from_account));

    COMMIT;
END //

DELIMITER ;

CALL TransferFunds(101, 102, 1000.00);

SELECT * FROM accounts ORDER BY account_id;
SELECT * FROM transactions ORDER BY transaction_id;

DELIMITER //

CREATE PROCEDURE SimulateTransferFailure(
    IN p_from_account INT,
    IN p_amount DECIMAL(12,2)
)
BEGIN
    UPDATE accounts
    SET balance = balance - p_amount
    WHERE account_id = p_from_account
      AND status = 'ACTIVE'
      AND balance >= p_amount;

    SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Deliberate failure after debit for rollback demonstration';
END //

DELIMITER ;

SELECT account_id, balance
FROM accounts
WHERE account_id IN (101, 102);

START TRANSACTION;
CALL SimulateTransferFailure(101, 500.00);

ROLLBACK;

SELECT account_id, balance
FROM accounts
WHERE account_id IN (101, 102);

START TRANSACTION;

UPDATE accounts
SET balance = balance - 500.00
WHERE account_id = 101 AND status = 'ACTIVE' AND balance >= 500.00;

UPDATE accounts
SET balance = balance + 500.00
WHERE account_id = 102 AND status = 'ACTIVE';

INSERT INTO transactions
(account_id, related_account_id, transaction_type, amount, description)
VALUES
(101, 102, 'DEBIT', 500.00, 'Transfer to account 102'),
(102, 101, 'CREDIT', 500.00, 'Transfer from account 101');

SAVEPOINT before_fee;

UPDATE accounts
SET balance = balance + 25.00
WHERE account_id = 999;

INSERT INTO transactions
(account_id, related_account_id, transaction_type, amount, description)
VALUES
(999, 101, 'FEE', 25.00, 'Transfer fee');

ROLLBACK TO SAVEPOINT before_fee;

COMMIT;

WITH signed_transactions AS (
    SELECT
        transaction_id,
        account_id,
        transaction_time,
        transaction_type,
        amount,
        description,
        CASE
            WHEN transaction_type = 'CREDIT' THEN amount
            ELSE -amount
        END AS signed_amount
    FROM transactions
    WHERE transaction_time >= DATE_FORMAT(CURDATE(), '%Y-%m-01')
      AND transaction_time < DATE_ADD(
            DATE_FORMAT(CURDATE(), '%Y-%m-01'), INTERVAL 1 MONTH
          )
),
running_statement AS (
    SELECT
        transaction_id,
        account_id,
        transaction_time,
        transaction_type,
        amount,
        description,
        SUM(signed_amount) OVER (
            PARTITION BY account_id
            ORDER BY transaction_time, transaction_id
            ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW
        ) AS net_movement
    FROM signed_transactions
),
statement_with_previous AS (
    SELECT
        transaction_id,
        account_id,
        transaction_time,
        transaction_type,
        amount,
        description,
        net_movement,
        ROW_NUMBER() OVER (
            PARTITION BY account_id
            ORDER BY transaction_time, transaction_id
        ) AS row_num,
        LAG(net_movement) OVER (
            PARTITION BY account_id
            ORDER BY transaction_time, transaction_id
        ) AS previous_net_movement
    FROM running_statement
)
SELECT
    transaction_id,
    account_id,
    transaction_time,
    transaction_type,
    amount,
    description,
    row_num,
    previous_net_movement,
    net_movement
FROM statement_with_previous
ORDER BY account_id, transaction_time, transaction_id;


SELECT * FROM accounts ORDER BY account_id;
SELECT * FROM transactions ORDER BY transaction_id;

WITH MonthlyTransactions AS (
    SELECT
        transaction_id,
        account_id,
        transaction_time,
        transaction_type,
        amount,
        description,
        CASE
            WHEN transaction_type = 'CREDIT' THEN amount
            WHEN transaction_type = 'DEBIT' THEN -amount
            WHEN transaction_type = 'FEE' THEN -amount
            ELSE 0
        END AS signed_amount
    FROM transactions
),
RunningBalance AS (
    SELECT
        transaction_id,
        account_id,
        transaction_time,
        transaction_type,
        amount,
        description,

        SUM(signed_amount) OVER (
            PARTITION BY account_id
            ORDER BY transaction_time, transaction_id
            ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW
        ) AS running_balance,

        ROW_NUMBER() OVER (
            PARTITION BY account_id
            ORDER BY transaction_time, transaction_id
        ) AS row_num

    FROM MonthlyTransactions
)
SELECT
    transaction_id,
    account_id,
    transaction_time,
    transaction_type,
    amount,
    description,
    row_num,
    LAG(running_balance, 1, 0) OVER (
        PARTITION BY account_id
        ORDER BY transaction_time, transaction_id
    ) AS previous_balance,
    running_balance
FROM RunningBalance
ORDER BY account_id, transaction_time, transaction_id;

SELECT a.account_id, a.account_holder, a.balance
FROM accounts a
WHERE a.status = 'ACTIVE'
  AND a.balance > (
      SELECT AVG(b.balance)
      FROM accounts b
      WHERE b.status = a.status
  );
  
SELECT @@autocommit AS autocommit_setting;

SET autocommit = 1;
UPDATE accounts
SET balance = balance + 10.00
WHERE account_id = 101;

SELECT account_id, balance FROM accounts WHERE account_id = 101;

UPDATE accounts
SET balance = balance - 10.00
WHERE account_id = 101;