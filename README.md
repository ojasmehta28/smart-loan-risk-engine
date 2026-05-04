# Smart Loan Risk Engine

## Overview

Smart Loan Risk Engine is a Spring Boot–based backend system that evaluates loan applications dynamically using configurable rules stored in a database.

Instead of hardcoded decision logic, the system uses an expression-based rule engine to determine loan eligibility at runtime. This design allows business rules to be modified without code changes or redeployment.

---

## Key Features

* Expression-based rule evaluation
* Dynamic rule configuration via database
* Support for AND, OR, and nested bracket conditions
* Priority-based rule execution
* Safe parsing and validation of expressions
* Centralized exception handling with structured responses
* Pagination support for rule management
* RESTful API design with clean layering

---

## Architecture

The system follows a layered architecture:

Client → Controller → Service → Rule Engine → Repository → Database

### Components

* **Controller Layer**

  * Handles HTTP requests
  * Validates input using DTOs
  * Delegates to service layer

* **Service Layer**

  * Contains business logic
  * Validates rules and input
  * Coordinates between components

* **Rule Engine**

  * Core decision-making component
  * Evaluates expressions dynamically
  * Supports AND / OR precedence and bracket logic
  * Handles safe parsing to avoid runtime failures

* **Repository Layer**

  * Uses Spring Data JPA
  * Provides abstraction over database operations

* **Database (PostgreSQL)**

  * Stores loan applications and rules

* **Global Exception Handler**

  * Centralized error handling
  * Returns consistent API responses

---

## Rule Engine Design

Rules are stored as expressions in the database, for example:

```
income >= 50000 AND creditScore >= 700
```

The engine processes rules as follows:

1. Fetch rules sorted by priority
2. Replace variables (income, creditScore) with actual values
3. Evaluate expressions dynamically
4. Apply logical precedence (AND > OR)
5. Resolve nested expressions using recursive bracket evaluation
6. Return the first matching rule decision

---

## Tech Stack

### Backend

* Java 17
* Spring Boot
* Spring Web (REST APIs)
* Spring Data JPA

### Database

* PostgreSQL

### API & Documentation

* Swagger / OpenAPI (API documentation and testing)

### Build & Dependency Management

* Maven

### Validation & Error Handling

* Jakarta Validation (`@Valid`, `@NotNull`, etc.)
* Global Exception Handling (`@RestControllerAdvice`)

### Logging

* SLF4J (Simple Logging Facade for Java)

### Architecture & Design

* Layered Architecture (Controller → Service → Repository)
* Expression-Based Rule Engine
* DTO Pattern for API abstraction

### Development & Testing Tools

* Postman (API testing)
* pgAdmin (Database management)


---

## API Endpoints

### Loan APIs

* **POST /loan/apply**

  * Apply for a loan
  * Returns decision: APPROVED / REJECTED / REVIEW

### Rule Management APIs

* **POST /rules**

  * Create a new rule

* **GET /rules**

  * Fetch all rules

* **PUT /rules/{id}**

  * Update a rule

* **DELETE /rules/{id}**

  * Delete a rule

* **GET /rules/paginated?page={page}&size={size}**

  * Fetch rules with pagination

---

## Sample Requests

### Create Rule

```
POST /rules
```

```json
{
  "decision": "APPROVED",
  "priority": 2,
  "expression": "income >= 50000 AND creditScore >= 700"
}
```

---

### Apply Loan

```
POST /loan/apply
```

```json
{
  "applicantName": "Ojas",
  "income": 60000,
  "creditScore": 750,
  "loanAmount": 200000
}
```

---

## Error Handling

The application uses centralized exception handling:

* Validation errors → HTTP 400
* Business rule errors → HTTP 400
* Unexpected errors → HTTP 500

Example response:

```json
{
  "message": "Error",
  "details": "Invalid expression format"
}
```

---

## Design Decisions

* **DTO Pattern**

  * Separates API contract from database schema

* **Layered Architecture**

  * Improves maintainability and scalability

* **Rule Engine Separation**

  * Decouples decision logic from business logic
  * Enables dynamic rule updates without code changes

* **Expression-Based Rules**

  * Provides flexibility compared to fixed condition structures

---

## Limitations

* Expression parser is custom and supports basic syntax
* No distributed caching (e.g., Redis) implemented
* No authentication/authorization layer
* No horizontal scaling (single instance)

---

## Future Improvements

* Introduce caching for rule retrieval (Redis)
* Use expression parsing libraries (SpEL / MVEL)
* Add authentication and authorization
* Implement rate limiting
* Support rule versioning and audit logs
* Deploy as scalable microservice

---

## How to Run

1. Clone the repository
2. Configure PostgreSQL in `application.properties`
3. Run the application:

```
mvn spring-boot:run
```

4. Test APIs using Postman

---

## Summary

This project demonstrates how to build a configurable decision engine using a layered backend architecture. It emphasizes clean design, dynamic rule evaluation, and production-style error handling.
