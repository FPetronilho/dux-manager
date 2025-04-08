# Dux Manager Microservice

The Digital User Context (DUX) Manager microservice is part of the Tracktainment application, which is designed to track books, movies, and games consumed by users. This microservice is responsible for managing digital users and their associated assets (e.g., books, games, movies). It uses MongoDB as the primary database and provides REST APIs for external clients.

---

## Overview

The DUX Manager microservice provides Create, Read and Delete operations for managing digital users and their assets. It adheres to the principles of Clean Architecture, ensuring modularity, scalability, and maintainability. The service uses MongoDB for persistent storage and integrates with other microservices via REST APIs.

---

## Architecture

The project follows a clean architecture with clear separation of concerns:

- **Application Module**: Handles application configuration and properties.
- **Core Module**: Contains domain models, DTOs, use cases, and interfaces for data providers.
- **Data Provider NoSQL Module**: Implementation of persistence layer using MongoDB.
- **Entry Point REST Module**: REST API controllers and exception handling.

---

## Features

- Create, read and delete operations for digital users and assets.
- Advanced filtering and search capabilities for assets.
- Integration with external services for asset management.
- Comprehensive validation and error handling.
  
---

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

---

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

---

## Getting Started

### Prerequisites

- Java 17+
- Maven
- MongoDB or another compatible NoSQL database
- Docker (optional, for containerized deployment)

### Configuration

Create an `application.properties` or `application.yml` file with the following properties:

```properties
# MongoDB configuration
spring.data.mongodb.uri=mongodb://localhost:27017/dux-manager
```

### Building

```bash
mvn clean package
```

### Running

```bash
java -jar dux-manager.jar
```

### Docker Setup

The dux-manager application can now be containerized using Docker. To run the application in Docker, follow these steps:
- Step 1: Build the Docker Image - 
Run the following command to build the Docker image:
```
docker-compose up --build
```

 - Step 2: Start the Containers - 
Start the containers using the following command:
```
docker-compose up
```

The dux-manager service will be accessible at http://localhost:8081.

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
- `E-001`: Internal server error
- `E-002`: Resource not found
- `E-003`: Resource already exists
- `E-007`: Parameter validation error

## Validation

The service includes comprehensive validation for all inputs:
- Digital user identity provider information validation.
- Asset external ID and type validation.
- Date format validation.
- Query parameter validation.

## Development

### Tech Stack

- Java 17
- Spring Boot
- Spring Data MongoDB
- Jakarta Validation
- Lombok
- MapStruct
- Logging with SLF4J
- MongoDB
- Maven
- Docker

### Project Structure

```
com.tracktainment.duxmanager
├── api                    # API interfaces
├── controller             # REST controllers
├── dataprovider           # Data provider implementations
├── domain                 # Domain models
├── document               # MongoDB document models
├── dto                    # Data Transfer Objects
├── exception              # Exception handling
├── mapper                 # Object mappers
├── usecases               # Business logic implementation
└── util                   # Utility classes
```

### Next Features

- Authentication and authorization;
- Unit testing;
- Update protocol from HTTP to HTTPS;
- Database encryption;
- CI/CD pipeline.

## Potential Tracktainment Upgrades

- **Review Microservice**: A microservice to handle reviews of books, games and movies.
- **Recommendation Microservice**: A microservice to handle books, games and movies recommendations based on what the user has consumed so far.
- **Notification Microservice** : A microservice to send notifications to users about recommendations.
