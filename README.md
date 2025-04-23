# common-platform-modelhub

## Table of Contents

- [Overview](#overview)
- [Quick Start Guide](#quick-start-guide)
- [Key Features](#key-features)
- [Usage Examples](#usage-examples)
  - [Creating and Managing Entities](#creating-and-managing-entities)
  - [Working with Fields](#working-with-fields)
  - [Managing Records](#managing-records)
  - [SQL-like Queries](#sql-like-queries)
- [API Reference](#api-reference)
  - [Entity Endpoints](#entity-endpoints)
  - [Field Endpoints](#field-endpoints)
  - [Record Endpoints](#record-endpoints)
- [Architecture](#architecture)
- [Technology Stack](#technology-stack)
- [Database Schema](#database-schema)
- [Development Guide](#development-guide)
  - [Prerequisites](#prerequisites)
  - [Setup and Configuration](#setup-and-configuration)
  - [Building and Running](#building-and-running)
  - [Testing](#testing)
- [Deployment](#deployment)
- [Troubleshooting](#troubleshooting)
- [Contributing](#contributing)

## Overview

ModelHub is a reactive, metadata-driven microservice that enables dynamic creation of data models at runtime without schema changes. It allows you to define custom entities with flexible fields and provides powerful query capabilities.

**Why ModelHub?** Traditional systems require downtime and migrations to modify data structures. ModelHub provides a flexible, schema-less approach that allows:

- **No-code model creation**: Define new data models without IT intervention
- **Zero-downtime evolution**: Extend data models without database schema changes
- **Structured data storage**: Store and query data with full validation
- **Future-proof systems**: Evolve your application without breaking changes

## Quick Start Guide

Get ModelHub up and running in minutes:

```bash
# 1. Clone the repository
git clone https://github.com/firefly-oss/common-platform-modelhub.git
cd common-platform-modelhub

# 2. Start PostgreSQL (Docker)
docker run --name postgres -e POSTGRES_PASSWORD=postgres -p 5432:5432 -d postgres:14

# 3. Build and run the application
mvn clean install
java -jar common-platform-modelhub-web/target/common-platform-modelhub.jar
```

### Create Your First Entity

```bash
# Create a customer entity
curl -X POST http://localhost:8080/api/v1/entities \
  -H "Content-Type: application/json" \
  -d '{
    "name": "customer",
    "description": "Customer entity",
    "active": true
  }'

# Save the returned entity ID
ENTITY_ID="the-returned-id"

# Add a field to the entity
curl -X POST http://localhost:8080/api/v1/fields \
  -H "Content-Type: application/json" \
  -d '{
    "entityId": "'$ENTITY_ID'",
    "fieldKey": "firstName",
    "fieldLabel": "First Name",
    "fieldType": "string",
    "required": true,
    "orderIndex": 1
  }'

# Create a record
curl -X POST http://localhost:8080/api/v1/records \
  -H "Content-Type: application/json" \
  -d '{
    "entityId": "'$ENTITY_ID'",
    "payload": {
      "firstName": "John"
    }
  }'

# Query records
curl -X POST http://localhost:8080/api/v1/records/query \
  -H "Content-Type: application/json" \
  -d '{
    "entityId": "'$ENTITY_ID'",
    "queryString": "firstName = '\''John'\''"
  }'
```

## Key Features

- **Dynamic Entity Definitions**: Create and manage data models at runtime
- **Custom Field Definitions**: Define fields with various data types (string, number, boolean, date, etc.)
- **Schema Validation**: Automatic validation of records against their entity schema
- **SQL-like Query Capabilities**: Powerful filtering with SQL-like syntax
- **Reactive API**: Non-blocking, reactive programming with Spring WebFlux
- **RESTful Endpoints**: Well-defined REST API for all operations
- **Comprehensive Documentation**: API documentation with Swagger/OpenAPI
- **Audit Fields**: Built-in created/updated timestamps for all records

## Architecture

The project is structured into multiple modules:

- **common-platform-modelhub-interfaces**: Contains DTOs and interfaces
- **common-platform-modelhub-models**: Contains database entities and repositories
- **common-platform-modelhub-core**: Contains business logic and services
- **common-platform-modelhub-web**: Contains REST controllers and web configuration

```
common-platform-modelhub/
├── common-platform-modelhub-interfaces/  # DTOs and interfaces
├── common-platform-modelhub-models/      # Database entities and repositories
├── common-platform-modelhub-core/        # Business logic and services
└── common-platform-modelhub-web/         # REST controllers and web configuration
```

## Technology Stack

- **Java 21**: Latest LTS version with virtual threads support
- **Spring Boot 3**: Modern application framework
- **Spring WebFlux**: Reactive programming model
- **Spring Data R2DBC**: Reactive database access
- **PostgreSQL with JSONB**: For storing structured data
- **Flyway**: Database migration and versioning
- **MapStruct**: For DTO ↔ Entity mapping
- **Jackson**: For JSON serialization
- **Swagger/OpenAPI**: API documentation
- **Maven**: Build and dependency management

## Database Schema

The core database schema consists of three main tables:

1. **virtual_entity**: Stores entity definitions
2. **virtual_entity_field**: Stores dynamic field definitions per entity
3. **virtual_entity_record**: Stores structured records as JSON

## Usage Examples

### Creating and Managing Entities

Entities are the foundation of ModelHub. They represent your data models (like "Customer", "Product", etc.).

#### Create an Entity

```bash
curl -X POST http://localhost:8080/api/v1/entities \
  -H "Content-Type: application/json" \
  -d '{
    "name": "product",
    "description": "Product catalog",
    "active": true
  }'
```

Response:
```json
{
  "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "name": "product",
  "description": "Product catalog",
  "active": true,
  "createdAt": "2023-06-15T10:30:00",
  "updatedAt": "2023-06-15T10:30:00"
}
```

#### Get All Entities

```bash
curl -X GET http://localhost:8080/api/v1/entities
```

#### Get Entity Schema

```bash
curl -X GET http://localhost:8080/api/v1/entities/product/schema
```

#### Update an Entity

```bash
curl -X PUT http://localhost:8080/api/v1/entities/3fa85f64-5717-4562-b3fc-2c963f66afa6 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "product",
    "description": "Updated product catalog",
    "active": true
  }'
```

### Working with Fields

Fields define the structure of your entities (like "name", "price", "category", etc.).

#### Add Fields to an Entity

```bash
# Add a name field
curl -X POST http://localhost:8080/api/v1/fields \
  -H "Content-Type: application/json" \
  -d '{
    "entityId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
    "fieldKey": "name",
    "fieldLabel": "Product Name",
    "fieldType": "string",
    "required": true,
    "orderIndex": 1
  }'

# Add a price field
curl -X POST http://localhost:8080/api/v1/fields \
  -H "Content-Type: application/json" \
  -d '{
    "entityId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
    "fieldKey": "price",
    "fieldLabel": "Price",
    "fieldType": "number",
    "required": true,
    "orderIndex": 2
  }'

# Add a category field
curl -X POST http://localhost:8080/api/v1/fields \
  -H "Content-Type: application/json" \
  -d '{
    "entityId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
    "fieldKey": "category",
    "fieldLabel": "Category",
    "fieldType": "string",
    "required": false,
    "orderIndex": 3
  }'
```

#### Get Fields for an Entity

```bash
curl -X GET http://localhost:8080/api/v1/fields/entity/3fa85f64-5717-4562-b3fc-2c963f66afa6
```

### Managing Records

Records are the actual data stored in your entities.

#### Create Records

```bash
# Create a laptop product
curl -X POST http://localhost:8080/api/v1/records \
  -H "Content-Type: application/json" \
  -d '{
    "entityId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
    "payload": {
      "name": "Laptop Pro",
      "price": 1299.99,
      "category": "Electronics"
    }
  }'

# Create a smartphone product
curl -X POST http://localhost:8080/api/v1/records \
  -H "Content-Type: application/json" \
  -d '{
    "entityId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
    "payload": {
      "name": "Smartphone X",
      "price": 899.99,
      "category": "Electronics"
    }
  }'

# Create a desk product
curl -X POST http://localhost:8080/api/v1/records \
  -H "Content-Type: application/json" \
  -d '{
    "entityId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
    "payload": {
      "name": "Office Desk",
      "price": 349.99,
      "category": "Furniture"
    }
  }'
```

#### Get Records for an Entity

```bash
curl -X GET http://localhost:8080/api/v1/records/entity/3fa85f64-5717-4562-b3fc-2c963f66afa6
```

#### Update a Record

```bash
curl -X PUT http://localhost:8080/api/v1/records/3fa85f64-5717-4562-b3fc-2c963f66afa7 \
  -H "Content-Type: application/json" \
  -d '{
    "entityId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
    "payload": {
      "name": "Laptop Pro 2023",
      "price": 1399.99,
      "category": "Electronics"
    }
  }'
```

### SQL-like Queries

ModelHub provides powerful SQL-like query capabilities for filtering and retrieving records.

#### Basic Query

```bash
# Find all electronics products
curl -X POST http://localhost:8080/api/v1/records/query \
  -H "Content-Type: application/json" \
  -d '{
    "entityId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
    "queryString": "category = '\''Electronics'\''"
  }'
```

#### Complex Query with Multiple Conditions

```bash
# Find expensive electronics
curl -X POST http://localhost:8080/api/v1/records/query \
  -H "Content-Type: application/json" \
  -d '{
    "entityId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
    "queryString": "category = '\''Electronics'\'' AND price > 1000"
  }'
```

#### Query with OR Conditions

```bash
# Find electronics or furniture
curl -X POST http://localhost:8080/api/v1/records/query \
  -H "Content-Type: application/json" \
  -d '{
    "entityId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
    "queryString": "category = '\''Electronics'\'' OR category = '\''Furniture'\''"
  }'
```

#### Query with Nested Conditions

```bash
# Find expensive electronics or any furniture
curl -X POST http://localhost:8080/api/v1/records/query \
  -H "Content-Type: application/json" \
  -d '{
    "entityId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
    "queryString": "(category = '\''Electronics'\'' AND price > 1000) OR category = '\''Furniture'\''"
  }'
```

#### Query with Sorting

```bash
# Find all products, sorted by price (highest first)
curl -X POST http://localhost:8080/api/v1/records/query \
  -H "Content-Type: application/json" \
  -d '{
    "entityId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
    "queryString": "ORDER BY price DESC"
  }'
```

#### Query with LIKE Operator

```bash
# Find products with names containing "Pro"
curl -X POST http://localhost:8080/api/v1/records/query \
  -H "Content-Type: application/json" \
  -d '{
    "entityId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
    "queryString": "name LIKE '\''%Pro%'\''"
  }'
```

#### Query with Pagination

```bash
# Get the second page of results (10 items per page)
curl -X POST http://localhost:8080/api/v1/records/query \
  -H "Content-Type: application/json" \
  -d '{
    "entityId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
    "queryString": "category = '\''Electronics'\''",
    "page": 1,
    "size": 10
  }'
```

## API Reference

### Entity Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/entities` | Get all entities |
| GET | `/api/v1/entities/{id}` | Get entity by ID |
| GET | `/api/v1/entities/name/{name}` | Get entity by name |
| GET | `/api/v1/entities/{name}/schema` | Get entity schema with fields |
| POST | `/api/v1/entities` | Create a new entity |
| PUT | `/api/v1/entities/{id}` | Update an entity |
| DELETE | `/api/v1/entities/{id}` | Delete an entity |

### Field Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/fields/entity/{entityId}` | Get all fields for an entity |
| GET | `/api/v1/fields/{id}` | Get field by ID |
| POST | `/api/v1/fields` | Create a new field |
| PUT | `/api/v1/fields/{id}` | Update a field |
| DELETE | `/api/v1/fields/{id}` | Delete a field |

### Record Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/records/entity/{entityId}` | Get all records for an entity |
| GET | `/api/v1/records/{id}` | Get record by ID |
| POST | `/api/v1/records` | Create a new record |
| PUT | `/api/v1/records/{id}` | Update a record |
| DELETE | `/api/v1/records/{id}` | Delete a record |
| POST | `/api/v1/records/query` | Query records with SQL-like capabilities |

#### SQL Query Operators

| Operator | SQL Syntax | Description |
|----------|------------|-------------|
| Equals | `=` or `eq` | Field equals value |
| Not Equals | `!=`, `<>` or `neq` | Field does not equal value |
| Greater Than | `>` or `gt` | Field greater than value |
| Greater Than/Equal | `>=` or `gte` | Field greater than or equal to value |
| Less Than | `<` or `lt` | Field less than value |
| Less Than/Equal | `<=` or `lte` | Field less than or equal to value |
| Contains | `LIKE` or `contains` | Field contains substring |
| Starts With | `startsWith` | Field starts with substring |
| Ends With | `endsWith` | Field ends with substring |
| In List | `IN` or `in` | Field value is in list |
| Not In List | `NOT IN` or `notIn` | Field value is not in list |
| Is Null | `IS NULL` or `isNull` | Field is null |
| Is Not Null | `IS NOT NULL` or `isNotNull` | Field is not null |

## Development Guide

### Prerequisites

- Java 21
- PostgreSQL 12+
- Maven 3.8+
- Docker (optional)

### Setup and Configuration

#### 1. Install Dependencies

```bash
# Install Java 21 (using SDKMan)
sdk install java 21-open

# Install PostgreSQL (or use Docker as shown in Quick Start)
# macOS
brew install postgresql
# Ubuntu/Debian
sudo apt-get install postgresql

# Create database
createdb modelhub
```

#### 2. Clone and Configure

```bash
# Clone repository
git clone https://github.com/firefly-oss/common-platform-modelhub.git
cd common-platform-modelhub

# Configure database connection
# Edit application.yaml or set environment variables:
export DB_HOST=localhost
export DB_PORT=5432
export DB_NAME=modelhub
export DB_USERNAME=postgres
export DB_PASSWORD=postgres
export DB_SSL_MODE=disable
```

#### 3. IDE Setup

- **IntelliJ IDEA**: Import as Maven project
- **VS Code**: Install Java Extension Pack
- **Eclipse**: Import as Maven project

### Building and Running

```bash
# Build the project
mvn clean install

# Run with Maven
mvn spring-boot:run -pl common-platform-modelhub-web

# Or run the JAR
java -jar common-platform-modelhub-web/target/common-platform-modelhub.jar

# Run with specific profile
java -jar -Dspring.profiles.active=dev common-platform-modelhub-web/target/common-platform-modelhub.jar
```

### Docker Deployment

```bash
# Build image
docker build -t modelhub .

# Run container
docker run -p 8080:8080 \
  -e DB_HOST=your-db-host \
  -e DB_PORT=5432 \
  -e DB_NAME=modelhub \
  -e DB_USERNAME=postgres \
  -e DB_PASSWORD=your-password \
  modelhub
```

### API Documentation

Once running, access the Swagger UI at:

```
http://localhost:8080/swagger-ui.html
```

### Testing

```bash
# Run all tests
mvn test

# Run tests for a specific module
mvn test -pl common-platform-modelhub-core

# Run with coverage report
mvn test jacoco:report
```

## Deployment

ModelHub can be deployed in various environments using Docker or traditional methods.

### Docker Deployment

```yaml
# docker-compose.yml example
version: '3.8'
services:
  modelhub:
    image: modelhub:latest
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - DB_HOST=postgres
      - DB_PORT=5432
      - DB_NAME=modelhub
      - DB_USERNAME=postgres
      - DB_PASSWORD=postgres
    depends_on:
      - postgres
    restart: unless-stopped

  postgres:
    image: postgres:14
    ports:
      - "5432:5432"
    environment:
      - POSTGRES_DB=modelhub
      - POSTGRES_USER=postgres
      - POSTGRES_PASSWORD=postgres
    volumes:
      - postgres-data:/var/lib/postgresql/data
    restart: unless-stopped

volumes:
  postgres-data:
```

### Kubernetes Deployment

```yaml
# kubernetes deployment example
apiVersion: apps/v1
kind: Deployment
metadata:
  name: modelhub
spec:
  replicas: 2
  selector:
    matchLabels:
      app: modelhub
  template:
    metadata:
      labels:
        app: modelhub
    spec:
      containers:
      - name: modelhub
        image: modelhub:latest
        ports:
        - containerPort: 8080
        env:
        - name: SPRING_PROFILES_ACTIVE
          value: "prod"
        - name: DB_HOST
          valueFrom:
            secretKeyRef:
              name: modelhub-db-credentials
              key: host
        - name: DB_PORT
          valueFrom:
            secretKeyRef:
              name: modelhub-db-credentials
              key: port
        - name: DB_NAME
          valueFrom:
            secretKeyRef:
              name: modelhub-db-credentials
              key: database
        - name: DB_USERNAME
          valueFrom:
            secretKeyRef:
              name: modelhub-db-credentials
              key: username
        - name: DB_PASSWORD
          valueFrom:
            secretKeyRef:
              name: modelhub-db-credentials
              key: password
        resources:
          limits:
            memory: "512Mi"
            cpu: "500m"
          requests:
            memory: "256Mi"
            cpu: "250m"
        readinessProbe:
          httpGet:
            path: /actuator/health/readiness
            port: 8080
          initialDelaySeconds: 30
          periodSeconds: 10
        livenessProbe:
          httpGet:
            path: /actuator/health/liveness
            port: 8080
          initialDelaySeconds: 60
          periodSeconds: 15
```

## Troubleshooting

### Common Issues and Solutions

#### Database Connection Problems

| Problem | Solution |
|---------|----------|
| Connection refused | • Check if PostgreSQL is running<br>• Verify host and port settings<br>• Check firewall rules |
| Authentication failed | • Verify username and password<br>• Check database user permissions |
| Database not found | • Ensure the database exists (`createdb modelhub`)<br>• Check DB_NAME environment variable |
| SSL issues | • Set `DB_SSL_MODE=disable` for local development<br>• Configure proper certificates for production |

#### API Errors

| Error | Solution |
|-------|----------|
| 400 Bad Request | • Check request payload format<br>• Verify required fields are present<br>• Ensure field types match schema |
| 404 Not Found | • Verify entity/field/record IDs exist<br>• Check URL path parameters |
| 500 Internal Server Error | • Check application logs<br>• Verify database connection<br>• Ensure sufficient memory/resources |

#### Query Issues

| Issue | Solution |
|-------|----------|
| No results returned | • Check entity ID is correct<br>• Verify field names in query match schema<br>• Check query syntax for SQL-like queries |
| Query syntax errors | • Ensure proper quoting of string values<br>• Check parentheses are balanced<br>• Verify operator syntax (=, >, <, etc.) |
| Performance problems | • Add pagination parameters (page, size)<br>• Add indexes to frequently queried fields<br>• Optimize complex queries |

### Debugging Tips

#### Enable Debug Logging

```bash
# Set environment variable
export LOGGING_LEVEL_COM_CATALIS=DEBUG

# Or in application.yaml
logging:
  level:
    com.catalis: DEBUG
```

#### Check Application Health

```bash
# Get application health status
curl http://localhost:8080/actuator/health

# Get detailed health information
curl http://localhost:8080/actuator/health/details
```

#### View SQL Queries

To see actual SQL queries being executed:

```yaml
# In application.yaml
logging:
  level:
    org.springframework.r2dbc: DEBUG
```

#### Memory Issues

If you encounter OutOfMemoryError:

```bash
# Increase heap size
java -Xmx512m -jar common-platform-modelhub-web/target/common-platform-modelhub.jar

# Monitor memory usage
jcmd <pid> GC.heap_info
```

## Contributing

We welcome contributions to the ModelHub project!

### Development Workflow

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/your-feature-name`
3. Commit your changes: `git commit -am 'Add some feature'`
4. Push to the branch: `git push origin feature/your-feature-name`
5. Submit a pull request

### Coding Standards

- Follow Java code conventions
- Write unit tests for new features
- Update documentation for API changes
- Use meaningful commit messages

### Code Review Process

All submissions require review. We use GitHub pull requests for this purpose.
