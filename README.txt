# RESTful API with Authentication

This project is a RESTful API built with Spring Boot, featuring user authentication using JWT (JSON Web Tokens). The API allows users to register, authenticate, and access protected resources based on their roles.

## Prerequisites

- Java JDK (version used during development: 17)
- Any preferred IDE with Spring Boot support or simply cmd

## Setup & Running the Application

1. **Clone the Repository**:
   ```
   git clone https://github.com/GeorgeAlep/AuthenticationAPI
   cd AuthenticationAPI
   ```
2. **Running the Application**:
   - The application can be run using the generated JAR file.
   - Navigate to the target directory where the JAR file is located.
   - Run the JAR file using the command:
     ```
     java -jar AuthenticationAPI.jar
     ```

## Features

- **User Registration**: New users can register with their name, email, and password.
- **User Authentication**: Registered users can authenticate and receive a JWT for accessing protected endpoints.
- **Role-Based Authorization**: Some endpoints are protected and can only be accessed by users with specific roles.
- **Student Management**: 
  - The API provides endpoints to manage and retrieve student data.
  - Students can be added through a POST request using tools like Postman with an `application/json` header.

## Endpoints

- `/api/v1/auth/register`: Register a new user.
- `/api/v1/auth/authenticate`: Authenticate a user and receive a JWT.
- `/api/v1/students`: Retrieve all students.
- `/api/v1/students/{studentID}`: Retrieve one specific student (protected endpoint, requires `ROLE_ADMIN`).

## Admin User

Upon startup, an admin user is automatically created. The JWT token for the admin user is printed to the console and can be used to access protected endpoints. The details of the admin user, including its credentials, can be modified in the `DatabaseConfiguration` class.

## Built With

- **Spring Boot**: The framework used to create the RESTful API.
- **Spring Security**: For authentication and authorization.
- **JWT**: For generating and verifying JSON Web Tokens.
- **IntelliJ IDEA**: The IDE used for development.
- **Lombok**: To reduce boilerplate code.

## Notes

- Instead of using Maven for building the project, the application is packaged into a JAR file which can be run directly.
- Always ensure that the secret key used for JWT generation is kept secure and not exposed.