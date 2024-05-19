# Task Manager UI

This project contains the codebase for the backend server of the Task Manager application, including details on the modules, workflows, and instructions for setting up and running the system locally.

This project was generated with [Angular CLI](https://github.com/angular/angular-cli) version 17.3.6.

## Pre-requisites
To run the system locally, ensure the following prerequisites are installed:
- Node/NPM
- Backend Server. [Set up via docker as mentioned [here](../task-manager/README.md)]

## Setting Up Locally

To run the UI locally, follow the below steps:
- Set up the backend server as mentioned [here](../task-manager/README.md)
- Navigate to the root of this project and run:
```
npm install
```
- Start the development server using below command
```
ng serve
```

## Development server
Run `ng serve` for a dev server. Navigate to `http://localhost:4200/`. The application will automatically reload if you change any of the source files.

## Additional Resources
In certain flows, such as password reset, the user needs access to a local SMTP mail server. This server will be set up as part of the backend server dependencies and can be accessed at http://localhost:8025