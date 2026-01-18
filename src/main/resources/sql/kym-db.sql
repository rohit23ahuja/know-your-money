CREATE TABLE statement_file
(
    id                BIGSERIAL PRIMARY KEY,
    account_name      VARCHAR(100)  NOT NULL,
    account_number    VARCHAR(64)  NOT NULL,
    statement_type    VARCHAR(100)  NOT NULL,
    bank_code         VARCHAR(20)  NOT NULL,
    original_filename VARCHAR(255) NOT NULL,
    uploaded_at       TIMESTAMPTZ  NOT NULL DEFAULT now()
);

CREATE TABLE statement_cell
(
    id                BIGSERIAL PRIMARY KEY,
    statement_file_id BIGINT       NOT NULL
        REFERENCES statement_file (id) ON DELETE CASCADE,
    row_index         INTEGER      NOT NULL,
    col_index         INTEGER      NOT NULL,
    cell_ref          VARCHAR(10)  NOT NULL,
    raw_value_text    TEXT NULL,
    created_at        TIMESTAMPTZ  NOT NULL DEFAULT now()
);

CREATE TABLE account_statement_structure (
    id                BIGSERIAL PRIMARY KEY,
    statement_file_id BIGINT NOT NULL
        REFERENCES statement_file(id) ON DELETE CASCADE,
    header_row_index  INTEGER NOT NULL,
    date_col_index        INTEGER NOT NULL,
    narration_col_index  INTEGER NOT NULL,
    debit_col_index       INTEGER NULL,
    credit_col_index      INTEGER NULL,
    balance_col_index     INTEGER NULL,
    data_start_row_index INTEGER NOT NULL,
    data_end_row_index   INTEGER NOT NULL,
    detected_at        TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE creditcard_statement_structure (
    id                BIGSERIAL PRIMARY KEY,
    statement_file_id BIGINT NOT NULL
        REFERENCES statement_file(id) ON DELETE CASCADE,
    header_row_index  INTEGER NOT NULL,
    transactiontype_col_index INTEGER NOT NULL,
    customername_col_index INTEGER NOT NULL,
    datetime_col_index INTEGER NOT NULL,
    description_col_index INTEGER NOT NULL,
    rewards_col_index INTEGER NULL,
    amt_col_index INTEGER NOT NULL,
    debitcredit_col_index INTEGER NULL,
    data_start_row_index INTEGER NOT NULL,
    data_end_row_index   INTEGER NOT NULL,
    detected_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE account_transaction (
    id                BIGSERIAL PRIMARY KEY,
    statement_file_id BIGINT NOT NULL
        REFERENCES statement_file(id) ON DELETE CASCADE,
    txn_date          DATE NOT NULL,
    narration         TEXT NOT NULL,
    debit_amount      NUMERIC(12,2) NULL,
    credit_amount     NUMERIC(12,2) NULL,
    balance           NUMERIC(12,2) NULL,
    source_row_index  INTEGER NOT NULL,
    created_at        TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE creditcard_transaction (
    id BIGSERIAL PRIMARY KEY,
    statement_file_id BIGINT NOT NULL
        REFERENCES statement_file(id) ON DELETE CASCADE,
    txn_type TEXT NOT NULL,
    customer_name TEXT NOT NULL,
    txn_date DATE NOT NULL,
    txn_time TIME NOT NULL,
    description TEXT NOT NULL,
	rewards integer null,
	amt NUMERIC(12,2) NULL,
	debit_credit text null,
    source_row_index INTEGER NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);
