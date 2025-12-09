# Backend

The backend is built with **Spring Boot 4** and exposes a REST API for managing products and orders.

---

## Technology Stack

- **Spring Web**
    - Exposes the REST API under `/api/**`.

- **Spring Security**
    - In-memory users via `UserDetailsService`
        - `admin` / `admin` → `ROLE_ADMIN`
        - `user` / `user` → `ROLE_USER`
    - HTTP Basic authentication for all protected endpoints.
    - Role-based access control:
        - product management (POST/PUT/DELETE) restricted to `ROLE_ADMIN`
        - orders visible depending on the authenticated user/role.

- **Spring Data JPA**
    - Repository layer for database access:
        - `ProductRepository`
        - `OrderRepository`
    - Pagination support via `Pageable`.

- **H2 Database**
    - Lightweight relational database for local development.
    - Stores:
        - `Product`
        - `Order`
        - `OrderItem`
    - H2 console available at `/h2-console` (enabled via Security config).

- **Swagger / OpenAPI**
    - `springdoc-openapi-starter-webmvc-ui`
        - Interactive API documentation at `/swagger-ui/index.html`.
        - JSON OpenAPI spec at `/v3/api-docs`.
    - `springdoc-openapi-maven-plugin`
        - Generates `target/openapi/openapi.json` during `mvn clean install`.
        - This file is used by the frontend to generate the TypeScript API client.