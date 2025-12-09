# API

The REST API is fully documented via Swagger / OpenAPI:

- Swagger UI: `http://localhost:1337/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:1337/v3/api-docs`

Below is a compact overview of the main CRUD endpoints.

---

## Products

Base path: `/api/products`

| Method | Path                      | Description                   | Access        |
|--------|---------------------------|-------------------------------|---------------|
| GET    | `/api/products`          | List products (paged, search) | USER, ADMIN   |
| GET    | `/api/products/{id}`     | Get single product            | USER, ADMIN   |
| POST   | `/api/products`          | Create product                | ADMIN only    |
| PUT    | `/api/products/{id}`     | Update product                | ADMIN only    |
| DELETE | `/api/products/{id}`     | Soft-delete product           | ADMIN only    |

---

## Orders

Base path: `/api/orders`

| Method | Path                      | Description                      | Access                          |
|--------|---------------------------|----------------------------------|---------------------------------|
| GET    | `/api/orders`            | List orders (paged)             | USER (own), ADMIN (all)        |
| GET    | `/api/orders/{id}`       | Get single order                | USER (own), ADMIN (any)        |
| POST   | `/api/orders`            | Create order for current user   | USER, ADMIN                    |
| PUT    | `/api/orders/{id}`       | Update order                    | USER (own), ADMIN (any)        |
| DELETE | `/api/orders/{id}`       | Delete order                    | ADMIN only                     |

---

## Auth

Base path: `/api/auth`

| Method | Path                | Description                               | Access      |
|--------|---------------------|-------------------------------------------|-------------|
| GET    | `/api/auth/login`  | Return current user and roles (Basic Auth) | USER, ADMIN |

> For detailed request/response models, validation rules and error codes, please use the Swagger UI.
