CREATE TABLE statement_file
(
    id                BIGSERIAL PRIMARY KEY,
    bank_code         VARCHAR(20)  NOT NULL,
    account_number    VARCHAR(64)  NOT NULL,
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

CREATE TABLE statement_structure (
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
    data_end_row_index   INTEGER NULL,
    detected_at        TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE bank_transaction (
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

