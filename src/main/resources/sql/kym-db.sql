CREATE TABLE TRANSACTIONS  (
	TRANSACTION_ID BIGINT  NOT NULL PRIMARY KEY ,
	TRANSACTION_DATE TIMESTAMP NOT NULL,
	TRANSACTION_DETAILS VARCHAR(1000),
	TRANSACTION_REFERENCE VARCHAR(100),
	VALUE_DATE TIMESTAMP NOT NULL,
	WITHDRAW_AMOUNT NUMERIC(10,2),
	DEPOSIT_AMOUNT NUMERIC(10,2),
	CLOSING_BALANCE NUMERIC(10,2)
) ;

CREATE TABLE STATEMENT_CELLS (
	CELL VARCHAR(10) NOT NULL PRIMARY KEY,
	TEXT VARCHAR(1000)
);

insert into statement_cells(cell, text) values('A1', 'Hdfc Bank');
select * from statement_cells;
truncate table statement_cells;

select * from statement_cells where cell like '%23';
select * from statement_cells where cell like 'A%';