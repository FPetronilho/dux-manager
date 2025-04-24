# Dux Manager Microservice
The Digital User Context (DUX) Manager microservice is part of the Tracktainment application, which is designed to track books, movies, and games consumed by users. This microservice is responsible for managing digital users and their associated assets (e.g., books, games, movies). It uses MongoDB as the primary database and provides REST APIs for external clients.

## Table of Contents
- [Overview](#overview)
- [Architecture](#architecture)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [API Endpoints](#api-endpoints)
- [API Documentation](#api-documentation)
- [Setup and Installation](#setup-and-installation)
  - [Prerequisites](#prerequisites)
  - [Local Development](#local-development)
  - [Docker Setup](#docker-setup)
- [Authentication](#authentication)
- [Error Handling](#error-handling)
- [Validation](#validation)
- [Next Features](#next-features)
- [Potential Tracktainment Upgrades](#potential-tracktainment-upgrades)

## Overview
DUX Manager is a microservice application that provides RESTful APIs for managing digital users and their assets. It follows clean architecture principles with a clear separation of concerns, making it maintainable, testable, and scalable. The application allows users to create, read and delete digital users and their assets.

## Architecture
The project follows a clean architecture with clear separation of concerns:
- **Application Module**: Handles application configuration and properties
- **Core Module**: Contains business rules, domain models, and use cases
- **Data Provider SQL Module**: Implementation of persistence layer using JPA/Hibernate
- **Entry Point REST Module**: REST API controllers and resources

## Features
- Create, read and delete operations for digital users and assets.
- Advanced filtering and search capabilities;
- MongoDB field-level encryption;
- Comprehensive validation and error handling;
- Design Patterns integration (DTO, Builder, Factory and Code Generation);
- Docker containerization for deployment;
- OAuth2/JWT authentication;
- Swagger documentation;
- Comprehensive unit testing with JUnit & Mockito with over 85% line coverage.

## Tech Stack
- Java 17
- Spring Boot 3.3.4
- Spring Data MongoDB
- Spring Security with OAuth2
- Jakarta Validation
- Lombok
- MapStruct
- MongoDB
- Maven
- Docker
- Swagger/OpenAPI
- HTTPS enabled via SSL certificates
- JUnit 5 & Mockito

### Project Structure

```
dux-manager
├── dux-manager-application            # Spring Boot application module
├── dux-manager-core                   # Core domain and business logic
│       ├── domain                     # Domain models
│       ├── dto                        # Data Transfer Objects
│       ├── exception                  # Exception definitions
│       ├── security                   # Security context
│       ├── usecases                   # Business use cases
│       ├── util                       # Utility classes
│       └── dataprovider               # Data provider interfaces
│
├── dux-manager-entrypoint-rest        # REST API entry point
│       ├── api                        # API interfaces
│       ├── controller                 # REST controllers
|       ├── converter                  # Enum to String converter
│       ├── exception                  # REST exception handlers
│       ├── mapper                     # Mappers for REST module
│       ├── security                   # Security configuration
│       └── swagger                    # OpenAPI/Swagger config
│
├── dux-manager-dataprovider-no-sql    # MongoDB data provider implementation
│       ├── dataprovider               # MongoDB data provider implementations
│       ├── document                   # MongoDB documents
│       └── mapper                     # MongoDB-specific mappers
│
└── resources                          # Project resources
    ├── certificate                    # SSL certificates
    └── docker                         # Docker configuration
```

## API Endpoints

| Method   | Endpoint                                      | Description                                                      |
|----------|-----------------------------------------------|------------------------------------------------------------------|
| POST     | `/api/v1/digitalUsers`                        | Create a new digital user                                        |
| GET      | `/api/v1/digitalUsers/{id}`                   | Get a digital user by ID                                         |
| GET      | `/api/v1/digitalUsers`                        | Find a digital user by subject, identity provider, and tenant ID |
| DELETE   | `/api/v1/digitalUsers/{id}`                   | Delete a digital user                                            |
| POST     | `/api/v1/assets/digitalUsers/{digitalUserId}` | Create an asset for a digital user                               |
| GET      | `/api/v1/assets`                              | List assets with filters                                         |
| DELETE   | `/api/v1/assets`                              | Delete an asset                                                  |

## API Documentation
When running the application, the Swagger UI is available at:
```
https://localhost:8443/dux-manager/swagger-ui.html
```
The OpenAPI specification is available at:
```
https://localhost:8443/dux-manager/api-docs
```

## Data Model
### **Digital User Entity Attributes**
- `id`: Unique identifier for the digital user.
- `identityProviderInformation`: Contains details about the identity provider (subject, identity provider, tenant ID).
- `personalInformation`: Personal details such as full name, email address, and birth date.
- `contactMediumList`: List of contact mediums (e.g., phone, email, geographic address).
- `assets`: List of assets associated with the digital user.
### **Asset Entity Attributes**
- `id`: Unique identifier for the asset.
- `externalId`: ID from the source system (e.g., book-manager, game-manager).
- `type`: Type of asset (e.g., book, game, movie).
- `permissionPolicy`: Permission policy (e.g., owner, viewer).
- `artifactInformation`: Details about the artifact (group ID, artifact ID, version).
- `createdAt`: Record creation timestamp.
- `updatedAt`: Last update timestamp.

## Setup and Installation
### Prerequisites

- Java 17+
- Maven 3.6+
- PostgreSQL 15+
- Docker (optional, for containerized deployment)

### Local Development
 - Step 1: Clone the repository
```
git clone https://github.com/FPetronilho/dux-manager.git
cd dux-manager
```
- Step 2: Configure application properties - Create a .env file to setup environment variables or update dux-manager-application/src/main/resources/application-local.yaml.
- Step 3: Build the project
```
mvn clean install
```
- Step 4: Run the application
```bash
java -jar dux-manager.jar
```

### Docker Setup
- Step 1: Create a docker network -  As DUX Manager is meant to be used as a support to other microservices, create a network so that these microservices can communicate with DUX Manager.
```
docker network create your-network
```
- Step 2: Set environment variables in .env file
- Step 3: Build and run with Docker compose
```
cd resources/docker
docker-compose up -d
``` 
The dux-manager service will be accessible at https://localhost:8443.

## Authentication
This application uses OAuth 2.0 with JWT for authentication and authorization. To access the protected endpoints, you must include a valid JWT token in the Authorization header:
```
Authorization: Bearer <your_jwt_token>
```

## Error Handling
The service provides structured error responses with the following format:
```json
{
  "code": "E-002",
  "httpStatusCode": 404,
  "reason": "Resource not found.",
  "message": "Asset your-asset-id not found."
}
```
Common error codes:
- `E-002`: Resource not found
- `E-003`: Resource already exists
- `E-007`: Parameter validation error

## Validation
The service includes comprehensive validation for all inputs:
- Digital user identity provider information validation.
- Asset external ID and type validation.
- Date format validation.
- Query parameter validation.


## Next Features
- CI/CD pipeline.

## Potential Tracktainment Upgrades
- **Review Microservice**: A microservice to handle reviews of books, games and movies.
- **Recommendation Microservice**: A microservice to handle books, games and movies recommendations based on what the user has consumed so far.
- **Notification Microservice** : A microservice to send notifications to users about recommendations.
