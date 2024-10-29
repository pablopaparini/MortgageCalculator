# Mortgage Calculator Service

## Overview

The Mortgage Calculator Service is a Spring Boot application that provides an API to calculate monthly mortgage costs based on loan value, income, home value, and maturity period. It also checks the feasibility of the mortgage based on predefined criteria.

## Features

- Calculate monthly mortgage costs.
- Validate mortgage feasibility.
- Retrieve mortgage interest rates.

## Prerequisites

- Java 17 or higher
- Gradle 8.10.2 or higher

## Configuration

The application uses the following configuration properties defined in `src/main/resources/application.properties`:

```ini
spring.application.name=assessment
management.endpoints.web.exposure.include=*
management.endpoint.health.show-details=always

mortgage.rates[0].maturityPeriod=30
mortgage.rates[0].interestRate=3.5
mortgage.rates[0].lastUpdate=2023-10-01 00:00:00
mortgage.rates[1].maturityPeriod=15
mortgage.rates[1].interestRate=2.8
mortgage.rates[1].lastUpdate=2023-10-01 00:00:00

calculator.income.time.limit=4
```

## OpenAPI Documentation
The API documentation is available via Swagger UI.

### Accessing Swagger UI
1. Run the application:
    ```sh
    ./gradlew bootRun
    ```
2. Open a web browser and navigate to:
    ```
    http://localhost:8080/swagger-ui.html
    ```

## Running Tests
To run the tests, use the following command:
```sh
./gradlew test
```
