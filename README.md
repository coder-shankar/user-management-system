# User Managment System
## Project Overview
Provides API to manage user

This Gradle project is designed to run on Java 17 and includes Testcontainers for integration testing.

## Prerequisites
Make sure you have the following installed:
* Java 17
* Postgres 15.4
* Docker (for Testcontainers)

## Tech Stack
* SpringBoot 3
* Postgres
* java 17
* Swagger 
  

## Building the Project
Run the following command to build the project:

```bash
./gradlew build
```

## Running Integration Tests
This project utilizes Testcontainers for integration testing. Make sure Docker is installed and running.

Run the following command to execute integration tests:

```bash
./gradlew integrationTest
```

## Swagger Documentation
 `http://localhost:8080/swagger-ui/index.html`

## Run Jar File
```bash
./gradlew bootRun
```

* use username `admin` and password `admin` to get token from login api.
* user Bearer token for authorization in rest of the API


Additional Configuration
For any additional configuration or customization, refer to the project's build.gradle file.
