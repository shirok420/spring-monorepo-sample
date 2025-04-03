# Spring Boot 3 Monorepo Sample

This is a sample monorepo project using Spring Boot 3 and Java 17 with Gradle.

## Project Structure

The project follows a monorepo architecture with the following modules:

```
spring-monorepo-sample/
├── app/                    # Application module (Application logic)
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/example/app/
│   │   │   │       ├── controller/    # REST controllers
│   │   │   │       └── Application.java
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── test/
│   └── build.gradle        # App module build file
├── core/                   # Core module (Database logic)
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/example/core/
│   │   │   │       ├── config/        # Database configuration
│   │   │   │       ├── entity/        # JPA entities
│   │   │   │       ├── repository/    # Spring Data repositories
│   │   │   │       ├── service/       # Business logic services
│   │   │   │       └── CoreModuleConfig.java
│   │   │   └── resources/
│   │   │       └── application-core.properties
│   │   └── test/
│   └── build.gradle        # Core module build file
├── build.gradle            # Root build file
├── settings.gradle         # Gradle settings file
├── .env                    # Environment variables for local development
├── Dockerfile              # Docker image definition
├── docker-compose.yaml     # Docker Compose configuration
└── README.md               # Project documentation
```

## Architecture

This project follows a modular architecture with two main modules:

1. **Core Module**: Contains all database-related logic including:
   - Entity definitions
   - Repository interfaces
   - Service classes
   - Database configuration (SQLite)

2. **App Module**: Contains all application logic including:
   - REST controllers
   - Main application class
   - Web configuration

## Technology Stack

- Java 17
- Spring Boot 3.1.5
- Spring Data JPA
- SQLite Database
- Gradle (Multi-project build)
- Lombok
- Docker & Docker Compose

## Database

The project uses SQLite as the database for simplicity. The database file will be created automatically in the root directory when the application starts.

## Getting Started

### Prerequisites

- Java 17 or higher
- Gradle 7.0 or higher
- Docker and Docker Compose (for containerized deployment)

### Running the Application

#### Option 1: Using Gradle

1. Clone the repository
2. Navigate to the project root directory
3. Run the application:

```bash
./gradlew app:bootRun
```

The application will start on <http://localhost:8080>

#### Option 2: Using Docker Compose

1. Clone the repository
2. Navigate to the project root directory
3. Build and run the application using Docker Compose:

```bash
docker-compose up -d
```

The application will start on <http://localhost:8080>

To stop the application:

```bash
docker-compose down
```

To view logs:

```bash
docker-compose logs -f
```

### Building the Application

To build the application, run:

```bash
./gradlew build
```

This will create executable JARs for each module.

### VSCode Setup

This project is configured to work with VSCode. The `.env` file contains environment variables for local development.

To set up the project in VSCode:

1. Install the "Spring Boot Extension Pack" extension
2. Open the project folder in VSCode
3. Use the Spring Boot Dashboard to run the application

## API Endpoints

The application exposes the following REST endpoints:

- `GET /api/items` - Get all items
- `GET /api/items/{id}` - Get item by ID
- `GET /api/items/search?name={name}` - Search items by name
- `GET /api/items/price?maxPrice={price}` - Find items under a certain price
- `POST /api/items` - Create a new item
- `PUT /api/items/{id}` - Update an existing item
- `DELETE /api/items/{id}` - Delete an item

## API Documentation with Swagger/OpenAPI

The project includes Swagger/OpenAPI for API documentation and testing. When the application is running, you can access:

- Swagger UI: <http://localhost:8080/swagger-ui.html>
- OpenAPI Documentation: <http://localhost:8080/api-docs>

Swagger UI provides an interactive interface to:

- View all available endpoints
- Test API calls directly from the browser
- See request/response models
- Understand API parameters and responses

## License

This project is licensed under the MIT License.
