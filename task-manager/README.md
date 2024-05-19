# Task Manager

This project contains the codebase for the backend server of the Task Manager application, including details on the modules, workflows, and instructions for setting up and running the system locally.

## Pre-requisites
To run the system locally, ensure the following prerequisites are installed:
- Java 17
- Maven
- Mockito (for Unit Testing)
- Docker
- Postgres Database (*if not using Docker at all.*)
- Local SMTP server (*again, if not using Docker at all.*)

## Setting Up Locally
To run the server from the source code, you have three options:
- Run using Docker
- Set up dependencies via Docker and run manually *(preferred)*
- Set up the entire project manually

### Run using Docker
To run the system via Docker, a `docker-compose.yml` file is provided in the root of the project.

Follow these steps to run the system:
- Update the `docker-compose.yml` file to include the project as shown below:
```
  app:
    container_name: task-manager
    build: ./
    ports:
      - "8080:8080"
    networks:
      - appNet
    depends_on:
      - postgres_database
```
- If any properties have been altered, update them in `src/main/resources/application-prod.properties` and `src/test/resources/application.properties`.
- Open a terminal, navigate to the root of the project, and run the following command:
```
docker compose up
```

This will set up the dependencies inside the Docker containers and run the app.


### Setup dependencies via Docker and run manually.
To set up the system dependencies via Docker, a `docker-compose.yml` file is provided in the root of the project.

Follow these steps:
- Navigate to the root of the project
- Run the following command to start the dependencies
```
docker compose up
```
- If any properties have been altered, update them in `src/main/resources/application-dev.properties` and `src/test/resources/application.properties`
- Open `TaskManagerApplication.java` and run it.

As part of the Docker execution, the following dependencies will be set up locally, with their respective links:

| App | URL |
|---|----|
| Backend API Swagger | http://localhost:8080/swagger-ui/index.html |
| Pg Admin | http://localhost:5050 |
| Postgre DB | http://localhost:8001 |
| Local Mail Server (MailHog) | http://localhost:8025|

Ports may change depending on any alterations made to the file by the user.

### Setup entire project manually
To set up the entire system manually, install the above-mentioned dependencies on your local system, then update `src/main/resources/application-dev.properties` and `src/test/resources/application.properties`.

Once the setup is complete, open `TaskManagerApplication.java` and run it.

## Modules
This code base covers a bunch of modules from Rest API to encryption and mockito testing. Below is a list of modules to better understand the codebase.
- Config: All application configurations are present here. The list of configurations are below:
  - `Audit`: The servers implements auditing of the of entities and fills out the created and updated date and times. These configurations are present in this file.
  - `Mail config`: These contain the JavaMail sender beans and configurations used to create the bean which is eventually used to send emails to user.
  - `Open Api Config`: These contain the swagger and open api configurations.
  - `Security Config`: The apps uses `sping security` and bearer auth to secure the app. This file contains the security configurations for the same.
- API: All Rest endpoints are present in the `controller` package. There are 2 groups of APIs present:
  - `User`: These contain the user related actions like `login`, `signup`, `reset password`, `enable/disable user` and more.
    - The `login`, `signup`, `reset password`, `change password` and `OTP verification` endpoints all public. Rest all require user authentication.
  - `Task`: These contain the APIs for creating, fetching, updating and deleting user tasks. 
  - Note: Authentication/Authorization: All other endpoints require authorization. Certain endpoints may require an admin/different higher order privilege but that is not take into account for this demo app. As of now it only shows that endpoints require `some` sort of authorization to use these endpoints.
- Business Service: All the business service and supporting services are present here along with the dependencies. All services follow a strategy pattern in order to enable apps  to change the underneath business logic without affecting other parts of code.
  - Crypto: This contains the encryption/decryption services which are in-turn used by the converters in the package `converters`.
  - Mail: This contains the mail service to send OTP email to user. It uses the Java mail sender  
  - Task: These contain the business logic for creating, fetching, updating and deleting user tasks.
  - User: These contain the business logic all user related tasks. It also uses the crypto services to verify passwords and encrypt/decrypt password.
- Exception: These are custom exceptions bound to certain failures and corresponding response status.
- Advice: This is the general exception handling module that reads the exceptions and provides a suitable readable error messages to the user.
- Filter: These are the JWT filter that the security config uses to verify JWT and extract user details
- Model: All entities, DTOs and request/response models are present here.
  - Note: Entities use the corresponding converter to encrypt/decrypt certain columns.
- Repository: All the task and user repository are here.
- Type: These are the Enum constants used across the app. Like the task priority, status, user status etc.
- Util: These are other utilities like JWT util that implements JWT verifications and claim extraction logic.
