# Budget Web Application

Application is web app based on REST service with database. App reads pdf files with bank accounts billings and save entries to the database. 
It will be possible to read PDFs from different banks.
Duplicated entries should be ignored. Then app will classify expenses and incomes by categories. User can create budget monthly limits for each category and plan monthly budget.

### Used technologies
* Java 17
* Spring 3.0.1
* Apache Log4J 2.11.1
* Apache PdfBOX 2.0.13

### Endpoints

GET http://localhost:8080/file?filename=path_to_pdf - read PDF files. Returns pdf path. 
At the moment file is logged by logger.

