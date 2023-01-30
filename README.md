# poc



## Getting started

This is only a poc project to learn spring and java fundamentals.

### General Info
The application expose the swagger at : http://localhost:8080/poc/swagger-ui.html and instantiate an in-memory H2 database that have one table "Users" with 10 rows inserted during application startup.

### Scope
The scope of poc is:
1. Make CRUD the application by creating the Create, Update and Delete APIs.
2. Write the Junit.
3. Write header filter that check API Password and allows only the password setted in application yml (check the response present in the resources folder only at the end)
4. Update the Database by adding an other table called Documents that have an N-1 relation with Users
5. Write the api that prints all documents associated to the gived User

