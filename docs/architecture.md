# Architecture

OrderHub follows a classic layered architecture: controllers expose REST endpoints,
services contain business logic, repositories handle persistence, and mappers convert between entities and DTOs.
The React frontend communicates with the backend via a generated TypeScript API client.

---

## Layers

- **Controllers**
    - `ProductController`
    - `OrderController`
    - `AuthController`

- **Services**
    - `ProductService`
    - `OrderService`

- **Repositories**
    - `ProductRepository`
    - `OrderRepository`

- **Domain & DTOs**
    - Entities:
        - `Product`, `Order`, `OrderItem`
    - DTOs:
        - `ProductRequest`, `ProductResponse`
        - `OrderRequest`, `OrderResponse`
        - `OrderItemRequest`, `OrderItemResponse`
    - Mapper classes convert between entities and DTOs.

- **Error Handling**
    - `GlobalExceptionHandler` provides consistent JSON error responses.

- **Security**
    - `SecurityConfig` defines:
        - HTTP Basic Auth
        - In-memory users (`admin/admin`, `user/user`)
        - Role-based access rules for product and order endpoints
        - H2 console access and frame options.

---

For technical details of the backend stack, see  
**[Backend](backend.md)**.  
For frontend structure, see **[Frontend](frontend.md)**.
