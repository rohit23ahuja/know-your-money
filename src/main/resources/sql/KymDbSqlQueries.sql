select * from statement_structure;
truncate table statement_file cascade;

insert into 
statement_structure(statement_file_id, header_row_index, date_col_index, narration_col_index, debit_col_index, credit_col_index, balance_col_index, data_start_row_index, data_end_row_index) 
values(16,1,1,1,1,1,1,1,1) returning id;



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

select statement_file_id, header_row_index, date_col_index, narration_col_index, debit_col_index, credit_col_index, balance_col_index, data_start_row_index, data_end_row_index 
from statement_structure where statement_file_id=17;

select * from statement_cell where row_index between 22 and 28;
select * from statement_file;
select * from bank_transaction;
