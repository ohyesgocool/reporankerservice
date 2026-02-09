# RepoRanker Service

RepoRanker is a Spring Boot application that allows you to rank GitHub repositories based on various metrics. It's built with Java 21 and leverages modern features like virtual threads and reactive programming to provide a high-performance, scalable service.

## Features

- **Technology Stack**: Built with the latest technologies including Spring Boot 3, Java 21, and Maven.
- **Reactive and Asynchronous**: Uses Spring WebFlux for a non-blocking, reactive approach to handle API requests, making it efficient and scalable.
- **Virtual Threads**: Leverages Java 21's virtual threads for improved concurrency and performance with lower overhead than traditional platform threads.
- **Caching**: Implements a Caffeine-based in-memory cache to store frequent API responses, reducing latency and minimizing redundant calls to the GitHub API.
- **API Documentation**: Integrated with `springdoc-openapi` to provide a Swagger UI for easy API exploration and testing.
- **Monitoring**: Includes Spring Boot Actuator with exposed endpoints for health, info, and Prometheus metrics.

## Design Choices

- **Java 21**: Chosen for its long-term support and access to modern features like virtual threads, which significantly improve the performance of I/O-bound applications like this one.
- **Spring WebFlux**: The reactive approach is ideal for this use case, as it involves making calls to the external GitHub API. WebFlux handles these calls in a non-blocking way, leading to better resource utilization.
- **Caffeine Cache**: A high-performance, in-memory cache was chosen to reduce the number of requests to the GitHub API, which is often rate-limited. This improves response times for popular queries.

## Prerequisites

Before you can run this application, you will need the following installed:

- [Java 21](https://www.oracle.com/java/technologies/downloads/#java21)
- [Apache Maven](https://maven.apache.org/download.cgi)

## Setup and Configuration

1.  **Clone the repository**:
    ```bash
    git clone <repository-url>
    cd reporankerservice
    ```

2.  **Configure GitHub API Token**:
    This application requires a GitHub API token to interact with the GitHub API. You need to generate a personal access token from your GitHub account.

    - Open the `src/main/resources/application.properties` file.
    - Find the line `github.api.token=YOUR_TOKEN_HERE`.
    - Replace `YOUR_TOKEN_HERE` with your actual GitHub personal access token.

    ```properties
    # GitHub API
    github.api.base-url=https://api.github.com
    github.api.token=ghp_YourPersonalAccessTokenHere
    github.api.version=2022-11-28
    ```

## How to Run

1.  **Build the application**:
    ```bash
    mvn clean install
    ```

2.  **Run the application**:
    ```bash
    mvn spring-boot:run
    ```
    The application will start on the default port `8080`.

## API Endpoints

Once the application is running, you can access the Swagger UI to view and test the available API endpoints.

- **Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

The primary endpoint will allow you to fetch and rank repositories based on different criteria. You can see the full request and response models in the Swagger documentation.
