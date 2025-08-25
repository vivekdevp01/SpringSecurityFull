# ProductionServices

A Spring Boot application providing production-related services with OAuth2 authentication (Google), JWT-based stateless security, and robust error handling.

## Features
- **OAuth2 Login:** Authenticate users via Google OAuth2.
- **JWT Authentication:** Issue and validate JWT tokens for stateless API security.
- **User Management:** Create and manage users in a database.
- **Role-Based Access:** Users are assigned the `ROLE_USER` authority by default.
- **Global Exception Handling:** Centralized error responses for common exceptions.
- **Frontend Integration:** Redirects to a frontend page with JWT after login.

## Technologies Used
- Java 17+
- Spring Boot
- Spring Security (OAuth2, JWT)
- Hibernate/JPA
- Lombok
- Gradle

## Project Structure
```
src/
  main/
    java/
      com/example/productionservices/
        advices/         # Global exception handlers
        auth/            # Authentication logic
        configs/         # Security and app configuration
        controllers/     # REST API controllers
        dtos/            # Data Transfer Objects
        entities/        # JPA entities (User, etc.)
        enums/           # Enum types
        exceptions/      # Custom exceptions
        filters/         # JWT filter
        handlers/        # OAuth2 success handler
        repositories/    # JPA repositories
        services/        # Business logic
        utils/           # Utility classes
    resources/
      application.properties
      application.yml
      static/
        home.html        # Frontend landing page
      templates/
  test/
    java/
      com/example/productionservices/
        ProductionServicesApplicationTests.java
```

## Setup & Running

### Prerequisites
- Java 17 or higher
- Gradle

### Configuration
1. **Google OAuth2 Setup:**
   - Register your app in Google Cloud Console.
   - Set client ID and secret in `application.properties` or `application.yml`.
2. **Database:**
   - Configure your database connection in `application.properties` or `application.yml`.

### Build & Run
```sh
./gradlew build
./gradlew bootRun
```

### Access
- **Frontend:** [http://localhost:8300/home.html](http://localhost:8300/home.html)
- **API:** [http://localhost:8300/api/v1/](http://localhost:8300/api/v1/)

## Authentication Flow
1. User visits `/home.html` and clicks "Login with Google".
2. After successful OAuth2 login, backend:
   - Creates/updates user in DB.
   - Issues JWT tokens.
   - Sets refresh token as HTTP-only cookie.
   - Redirects to frontend with access token in URL.
3. Frontend extracts access token and sends it in `Authorization: Bearer <token>` header for API requests.

## Error Handling
- All exceptions are handled by `GlobalExceptionHandler`.
- Standardized error responses for validation, access denied, and internal errors.

## Customization
- Add more roles/authorities by extending the `User` entity and updating `getAuthorities()`.
- Customize OAuth2 providers in `application.properties`.


## Author
Vivek Pundir

