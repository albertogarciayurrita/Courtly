# Courtly

Courtly is a backend API for a sports facility booking system. The project is focused on building a clean, realistic and well-documented backend using Java, Spring Boot, PostgreSQL and Docker.

The main goal of the project is to model a real booking domain where users can reserve sports courts in fixed time slots while preventing conflicting reservations through transactional consistency and database-level constraints.

> This project is being developed as a backend-first personal project, with a strong focus on clean architecture, API design, testing, concurrency, documentation and deployment.

---

## Project Goal

The purpose of Courtly is to simulate a real-world backend system for managing sports facility reservations.

The core business problem is simple but realistic:

> Manage limited resources, in this case sports courts, across fixed time slots while ensuring that two users cannot book the same court at the same time.

This makes the project especially useful for practicing backend concepts such as:

- REST API design
- Authentication and authorization
- Relational database modelling
- Transactional consistency
- Race condition prevention
- Testing
- Docker-based development
- API documentation
- Incremental feature delivery using GitHub Issues, Milestones and Releases

---

## Tech Stack

Planned stack:

- Java 21
- Spring Boot
- Maven 
- PostgreSQL
- Spring Data JPA / Hibernate
- Flyway
- Docker & Docker Compose
- JWT Authentication
- Swagger
- JUnit
- Mockito
- Testcontainers
- GitHub Actions
- Redis
- Message queue for asynchronous email processing

---

## Architecture

Courtly follows a modular monolith approach.

Instead of organizing the application only by technical layers, the project is organized by business domains. Each domain contains its own controllers, services, repositories, entities and DTOs where appropriate.

Example structure:

````text
src/main/java/com/courtly
├── auth
├── user
├── facility
├── court
├── reservation
├── credits
├── waitlist
└── shared
````

The basic request flow follows a traditional backend structure:

Controller
↓
Service
↓
Repository
↓
PostgreSQL

This keeps the project simple enough for a personal backend project, while still maintaining clear separation between business areas.

This keeps the project simple enough for a personal backend project, while still maintaining clear separation between business areas.

---

## Core Domain

Courtly is based on the following main concepts:

### Users

Users can register, log in, authenticate using JWT and make reservations.

Planned roles:

- `USER`
- `ADMIN`

### Facilities

A facility represents a sports location or club.

Examples:

- Tennis club
- Padel club
- Sports center

### Courts

A court is a reservable resource inside a facility.

Each court can be active or inactive, allowing admins to temporarily disable courts for maintenance or other reasons.

### Reservations

A reservation represents a booking made by a user for a specific court and time slot.

Important rules:

- A user must be authenticated to create a reservation.
- A court must exist and be active.
- A reservation cannot be created in the past.
- A user must have enough credits.
- A court cannot be booked twice for the same time slot.
- Booking conflicts must be handled safely.

### Credits

Users have a credit balance.

Credits are deducted when a reservation is created and refunded when a reservation is cancelled, depending on the cancellation rules.

A `CreditTransaction` entity will keep an audit trail of credit movements.

---

## Key Backend Features

### Authentication and Authorization

The API will include user registration, login and JWT-based authentication.

Admin-only endpoints will be protected using role-based authorization.

### Booking Flow

The reservation flow is the core feature of the project.

The system must validate the request, check user credits, create the reservation and deduct credits safely.

### Double Booking Prevention

A key goal of the project is to prevent two users from booking the same court at the same time.

This will be handled through:

- Application-level validation
- Database-level unique constraint
- Transactional consistency
- Proper conflict handling with `HTTP 409 Conflict`

PostgreSQL will remain the source of truth for booking consistency.

### API Documentation

Swagger / OpenAPI will be used to document the available endpoints, request bodies, response bodies and HTTP status codes.

### Testing

The project will include:

- Unit tests for business logic
- Integration tests for API and database flows
- More advanced concurrency tests in later versions

### Docker

The project will provide a Docker Compose setup for running the backend and PostgreSQL locally.

---

## Roadmap

The project is organized into milestones and releases.

---

## v1.0.0 - MVP

First functional version of Courtly.

Planned scope:

- Spring Boot project setup
- PostgreSQL configuration
- Docker Compose setup
- Flyway migrations
- Base package structure by domain
- User entity
- User registration
- User login with JWT
- Role-based authorization
- Facility entity and CRUD
- Court entity and CRUD
- Fixed booking slots
- Availability endpoint
- Reservation entity
- Booking creation flow
- Credit balance
- Credit transactions
- Credit deduction on booking
- Reservation cancellation
- Credit refund on cancellation
- Database constraint to prevent double booking
- HTTP 409 Conflict handling for booking conflicts
- Validation and global exception handling
- Swagger / OpenAPI documentation
- Unit tests for core services
- Integration tests for reservation flow
- Professional README
- Deployment preparation
- Release v1.0.0

---

## v1.1.0 - Quality & Advanced Booking

Planned scope:

- Waitlist
- Automatic assignment from waitlist
- Testcontainers
- More complete concurrency tests
- GitHub Actions CI
- Idempotency for reservation creation

---

## v1.2.0 - Async, Redis & Resilience

Planned scope:

- Email queue producer
- Email consumer
- Retry logic for transient email failures
- Redis availability cache
- Cache invalidation on booking changes
- Rate limiting for login and reservation endpoints

---

## Git Workflow

The project uses a simple Git workflow:

```text
main
develop
feature/*
```

### Branches

- `main`: stable branch with released versions.
- `develop`: integration branch for completed features.
- `feature/*`: short-lived branches for specific issues.

Example feature branches:

```text
feature/project-setup
feature/auth
feature/reservations
feature/credits
feature/waitlist
feature/email-queue
feature/redis-cache
```

---

## Releases

Stable versions will be marked using Git tags and GitHub Releases.

Example:

- `v1.0.0` - MVP
- `v1.1.0` - Quality & Advanced Booking
- `v1.2.0` - Async, Redis & Resilience

---

## GitHub Project Management

The project is managed using GitHub Issues, Milestones, Labels and a simple Kanban board.

Board columns:

- Backlog
- In Progress
- Review/Test
- Done

Labels are organized by type, area and priority.

### Type Labels

- `type: feature`
- `type: bug`
- `type: test`
- `type: docs`
- `type: infra`
- `type: refactor`

### Area Labels

- `area: auth`
- `area: reservations`
- `area: users`
- `area: courts`
- `area: facilities`
- `area: credits`
- `area: waitlist`
- `area: redis`
- `area: async`
- `area: ci-cd`

### Priority Labels

- `priority: high`
- `priority: medium`
- `priority: low`

---

## Planned API Areas

The final API will include endpoints related to:

- Authentication
- Users
- Facilities
- Courts
- Availability
- Reservations
- Credits
- Waitlist
- Admin operations

Detailed endpoint documentation will be available through Swagger once implemented.

---

## Local Development

Local development instructions will be added as the project setup is implemented.

Planned startup flow:

```bash
docker compose up -d
```

Then run the Spring Boot application locally or through Docker, depending on the final setup.

---

## Deployment

Deployment instructions will be added once the MVP is ready.

The goal is to deploy the backend and expose online API documentation through Swagger.

---

## Current Status

The project is currently in the planning and initial setup phase.

The first development milestone is:

```text
v1.0.0 - MVP
```

The immediate goal is to build the first stable backend version with authentication, court management, reservations, credits, conflict prevention, documentation, tests and deployment preparation.

---

## Author

Developed by Alberto García Yurrita as a personal backend engineering project.