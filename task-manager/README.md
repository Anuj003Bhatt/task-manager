# Task Manager

This project contains the codebase for the backend server of the Task Manager application, including details on the modules, workflows, and instructions for setting up and running the system locally.

## Pre-requisites
To run the system locally, ensure the following prerequisites are installed:
- Java 17
- Maven
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

