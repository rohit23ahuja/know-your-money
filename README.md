## Know Your Money

### use case
[in-progress] creating this expense manager for my self use to do my monthly accounting. 
I expect to do accounting for my accounts, my wife's accounts and our joint accounts.
will be feeding monthly account statements from different banks. 
expect data to be saved in db.
enrichment should be done on data like tagging of expenses, auto detection of transaction remarks, summary panel.
processed file should be uploaded to my google drive or any other cloud storage.
processed file should be deleted from my local system. 

### database
i will be using postgres db to store data.

### file format
input file format - download monthly statements in csv,xls or pdf (in that order of preference) format from net banking.
think it will be better to support pdf format first. because it is certain that every will be providing statement in atleast pdf format.
output file format - generate accounting reports in xls format.

#### file format as pdf
Initially i decided that I will first provide support for PDF format. reason being every bank gives statement in PDF format. But have decided today that will provide support for xls format first.
challenges with pdf file format is:-
* pdf format primarily is for file printing
* text extraction as of now looks hard for pdf vs excel documents.
* text extraction as such is not hard for pdf but looks tricky if we have to do in a structured way.

#### file format as xls

### tech stack
will be using spring batch, spring and java to develop this application.

### feature description - file loading in kym
input source in kym app will be monthly account statements. 
app should be able to recognise the underlying bank of the statement.
app should then be able to recognise the format based on the identified bank.
app should then load the file in memory based on the format.

### feature description - save transaction in kym
loaded file contains account transaction and other information.
this loaded information needs to be saved in database.
information that needs to be saved - account information, transactions, balances.

### feature description - generate accounting report
loaded transactions need to be transformed and enriched.
transformation needs to happen in a specific format and also generate unique transaction id.
enrichment includes features like tagging, auto generated remarks.
accounting reports will be generated from the transformed and enriched data.

### file archiving
processed files will be archived in a separate folder on cloud storage.

### accounting reports storage
generated accounting reports will be uploaded on cloud storage.

### Reference links
https://medium.com/@_samkitjain/developing-a-bank-statement-analyser-7470bffbe5e2
someone had the same idea - https://github.com/alokkusingh/spring-batch-pdf-parser