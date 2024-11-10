## Know Your Money

### use case
[in-progress] creating this expense manager for my self use to do my monthly accounting. 
I expect to do accounting for my accounts, my wife's accounts and our joint accounts.
will be feeding monthly account statements from different banks. 
expect data to be saved in db
enrichment should be done on data like tagging of expenses, auto detection of transaction remarks, summary panel
processed file should be uploaded to my google drive or any other cloud storage.
processed file should be deleted from my local system 

### database
i will be using postgres db to store data

### file format
input file format - download monthly statements in csv,xls or pdf (in that order of preference) format from net banking
output file format - generate accounting reports in xls format

### tech stack
will be using spring batch, spring and java to develop this application

### feature description - file loading in kym
input source in kym app will be monthly account statements. 
app should be able to recognise the underlying bank of the statement.
app should then be able to recognise the format based on the identified bank
app should then load the file in memory based on the format.

### feature description - save transaction in kym
loaded file contains account transaction and other information.
this loaded information needs to be saved in database.
information that needs to be saved - account information, transactions, balances

### feature description - generate accounting report

