
# Task Manager

## Overview
The Task Manager App is a versatile and user-friendly tool designed to help users efficiently manage their tasks and stay organized. With this app, users can easily add, track, update, and delete tasks, ensuring they stay on top of their to-do lists. The app also allows users to track the status and priority of each task, providing a comprehensive view of their workload and helping them prioritize effectively.

## Demo
[![Task Manager - Demo](sample/logo.png)](https://drive.google.com/file/d/1EAu_w0zOA-vxBySYy_HwkI0cSuxh0BqN/view?usp=sharing "Task Manager - Demo")  

## Key Features

### Task Management
- **Add Tasks**: Quickly add new tasks with a straightforward interface.
- **Track Tasks**: View a list of all tasks with their current status and priority levels.
- **Update Tasks**: Modify task details, including their status and priority, to reflect changes in your schedule or priorities.
- **Delete Tasks**: Remove tasks that are no longer needed to keep your task list clean and focused.
- **Status Tracking**: Monitor the progress of each task with status indicators (e.g., todo, in progress, completed).
- **Priority Setting**: Assign priority levels to tasks (e.g., low, medium, high) to ensure that important tasks are highlighted.

### User Management
- **User Accounts**: Users can sign up and log into the system, allowing them to create a personalized task list that is accessible only to them. Additonally they can reset their passwords as well using registered email addresses.
- **Admin Controls**: Additional features to view, enable/disable all users have been incorporated into the system to give admin controls in the future releases.

## Getting Started

This app comprises two major modules: the frontend built with Angular, and the backend developed using Java and Spring Boot, enhanced with Spring Security for added protection.

## Setting Up the App Locally
To run the app from the source code, you have two options:
- Use Docker
- Manually start the applications

The documentation for manually running the applications is included separately in the respective project directories. You can find them below:
- [Task Manager UI](./task-manager-ui/README.md)
- [Task Manager Server](./task-manager/README.md)

### Running via docker
Navigate to the root of the repository and run the following command:
```
docker compose up
```

Once the Docker containers are up and running, the following utilities will be accessible at the respective URLs:


 App | URL 
 --- | ---
 UI | http://localhost:4200
 Backend API Swagger | http://localhost:8080/swagger-ui/index.html
 Pg Admin | http://localhost:5050 
 Postgre DB | http://localhost:8001
 Local Mail Server (MailHog) | http://localhost:8025

PgAmin login credentials:
- Username: admin@admin.com
- password: root

## Contributing
To contribute to the project, please review the respective project ReadMe.
- [Task Manager UI](./task-manager-ui/README.md)
- [Task Manager Server](./task-manager/README.md)

## Assumptions
- There are user endpoints like enable/disable/edit/list user which may require an admin privilege but that is not take into account for this demo app. As of now it only shows that endpoints require `some` sort of authorization to use these endpoints.
- Since this is a demo app, no pagination has been implemented on the UI. Although it is present in the backend.
- The text search is only UI side search, because making that a server side search opens the code for injections and security risks. So in order to keep this app simple as of now the search is only a UI side search.
- No real integrations have been coded for the SMTP servers, so it is better to use docker mailhog server only. [Although same can be done with minimal code changes]
- No real email ID is required only the patterns are validated
- Strong password verification are not required to keep it simple as of now.
