# Forum API

## Overview
Forum API is a REST API for an online discussion forum built with Spring Boot.
The project allows users to register accounts, authenticate using JWT tokens, create and manage posts, add comments, and like posts. 
The main goal of the project was to learn backend development with Spring Boot, Spring Security, JPA, testing, and layered application architecture.

This was me second larger Spring Boot backend project created to practice REST API development, authentication, testing, and application architecture.
## Features
### Authentication
* User registration
* User login
* JWT-based authentication
* BCrypt password hashing

### Posts
* Create post
* Get post by ID
* Get paginated list of posts
* Edit own post
* Delete own post

### Comments
* Create comment
* Get paginated comments for a post
* Edit own comment
* Delete comment
### Likes
* Like a post
* Remove like from a post
### Authorization & Security
* Role-based access control (USER, ADMIN)
* Ownership validation for posts and comments
* Stateless authentication using JWT
* Custom JWT authentication filter
### Error Handling
* Global exception handling using `@RestControllerAdvice`
* Consistent API error responses

## Tech Stack
* Java 21
* Spring Boot 4
* Spring Security
* Spring Data JPA (Hibernate)
* PostgreSQL
* Docker Compose
* JWT Authentication (JJWT)
* ModelMapper
* Bean Validation
* Lombok
* JUnit 5
* Mockito
* MockMvc
* Maven

## Project Structure
```text
config/
controllers/
dtos/
entities/
exceptions/
mappers/
repositories/
security/
services/
```

### Architecture Layers
* Controllers – handle HTTP requests and responses
* Services – contain business logic
* Repositories – database access layer
* DTOs – request and response models
* Entities – JPA domain models
* Security – JWT authentication and authorization
* Exceptions – custom exceptions and global error handling

## Running the Application
### Start PostgreSQL
```bash
docker compose up -d
```

Database configuration:
Host: localhost
Port: 5433
Database: forumdb
Username: postgres
Password: postgres

### Run the application
```bash
mvn spring-boot:run
```

The API will be available at: http://localhost:8080

## Testing
The project includes tests for multiple application layers:

### Service Tests
* Business logic validation
* Exception scenarios
* Repository interaction verification

### Controller Tests
* HTTP status validation
* Request validation
* Response verification

### Security Tests
* Authentication checks
* Authorization checks
* JWT protected endpoints

### Testing Tools
* JUnit 5
* Mockito
* MockMvc

## What I Learned
During the development of this project I gained practical experience with:
* Spring Boot application development
* REST API design
* JWT authentication and authorization
* Spring Security
* JPA entity relationships
* Pagination using Spring Data
* Global exception handling
* Unit and integration testing
* Layered application architecture
* Dockerized PostgreSQL setup

## Future Improvements
Potential future enhancements:
* Search functionality
* Refresh tokens
* Swagger / OpenAPI documentation
* Full Dockerized application deployment
* Advanced admin features
