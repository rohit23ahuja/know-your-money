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
