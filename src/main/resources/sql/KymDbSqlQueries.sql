select * from statement_structure;
truncate table statement_file cascade;

insert into 
statement_structure(statement_file_id, header_row_index, date_col_index, narration_col_index, debit_col_index, credit_col_index, balance_col_index, data_start_row_index, data_end_row_index) 
values(16,1,1,1,1,1,1,1,1) returning id;

select statement_file_id, header_row_index, date_col_index, narration_col_index, debit_col_index, credit_col_index, balance_col_index, data_start_row_index, data_end_row_index 
from statement_structure where statement_file_id=17;

select * from statement_cell where row_index between 22 and 28;
select id, account_name, account_number, statement_type, bank_code, original_filename from statement_file where id=39;
select * from bank_transaction;

drop table statement_file cascade;
drop table bank_transaction cascade;
drop table statement_cell cascade;
drop table account_statement_structure cascade;
drop table creditcard_statement_structure cascade;

select * from statement_file;
select * from statement_cell;
select max(row_index) from statement_cell where statement_file_id=3;
select * from creditcard_statement_structure;

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

insert into 
creditcard_transaction(statement_file_id, txn_type, customer_name, txn_date, txn_time, description, rewards, amt, debit_credit, source_row_index)
values (1, 'Domestic', 'Rohit', '10-11-2025', '14:30', 'Swiggy', -30, 300.30, null, 23);