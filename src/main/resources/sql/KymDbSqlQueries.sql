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
