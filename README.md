# RepoRanker Service

This is a Spring Boot application that provides a REST API for searching and ranking popular GitHub repositories.

## Features

- Search for repositories by creation date and language.
- Score repositories based on stars, forks, and recency of updates.
- Caching to improve performance and avoid hitting GitHub API rate limits.
- OpenAPI documentation for the API.

## Getting Started

### Prerequisites

- Java 21
- Maven

### Running the Application

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/reporankerservice.git
   ```
2. Navigate to the project directory:
   ```bash
   cd reporankerservice
   ```
3. Build the application:
   ```bash
   mvn clean install
   ```
4. Run the application:
   ```bash
   java -jar target/reporankerservice-0.0.1-SNAPSHOT.jar
   ```

### API Documentation

Once the application is running, you can access the OpenAPI documentation at:

[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## API Endpoint

### Search Repositories

- **GET** `/api/repositories/search`

#### Parameters

- `createdFrom` (string, required): The earliest creation date for repositories (YYYY-MM-DD).
- `language` (string, required): The programming language to filter by.

#### Example

```bash
curl -X GET "http://localhost:8080/api/repositories/search?createdFrom=2022-01-01&language=java"
```
