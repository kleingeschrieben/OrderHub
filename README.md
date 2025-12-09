# OrderHub

OrderHub is a small order management system built with **Spring Boot** and **React (TypeScript)**.  
It allows managing products and orders, with a simple shopping cart and role-based access control.

---

## Features (Overview)

- **Products**
  - Create, update, soft-delete products
  - Fields: `id`, `name`, `price`, `stock`, `deleted`
  - Pagination and simple name search (`search` query parameter)

- **Orders**
  - Orders with multiple items (product + quantity)
  - Server-side total price calculation and stock checks
  - Pagination and detail view with all order items

- **Users & Roles**
  - In-memory users with HTTP Basic Authentication:
    - Admin → `admin` / `admin` (`ROLE_ADMIN`)
    - User → `user` / `user` (`ROLE_USER`)
  - Admin: full product management and access to all orders
  - User: read-only products, only own orders

- **Frontend**
  - Login page
  - Navbar with products/orders navigation and cart dropdown
  - Product list with search, pagination and admin actions
  - Orders as accordion with items
  - Cart respecting stock → checkout creates an order

---

## Documentation

For more details, see:

- [Architecture](docs/architecture.md)
- [Backend](docs/backend.md)
- [Frontend](docs/frontend.md)
- [API & OpenAPI](docs/api.md)
- [Limitations & Future Ideas](docs/limitations-and-future-work.md)
- [Use of AI / Coding Assistants](docs/ai-usage.md)

---

## Getting Started

### Prerequisites

- Java 21+
- Node.js + npm
- Maven

### Backend

First build the backend to generate the OpenAPI contract (used by the frontend API client).  
This creates `target/openapi/openapi.json`:

```bash
cd backend
mvn clean install
```

Then start the application for local development:

```bash
mvn spring-boot:run
```

The backend runs on: `http://localhost:1337`

Useful endpoints:

- Swagger UI: `http://localhost:1337/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:1337/v3/api-docs`
- H2 Console: `http://localhost:1337/h2-console`

#### Database (H2) and Sample Data

- H2 stores the database in a `data` folder inside the **backend** directory, e.g.:
  - `backend/data/orderhub-db.mv.db`
- The repository already contains a pre-filled database file with 50 products:
  - `docs/orderhub-db.mv.db`
- To use the sample data:
  1. Start the backend once so that H2 creates the initial `backend/data/orderhub-db.mv.db`.
  2. Stop the backend.
  3. Replace `backend/data/orderhub-db.mv.db` with `docs/orderhub-db.mv.db`
  4. Start the backend again.

This way you don’t start with an empty system but with a database that already contains example products.

### Frontend

Make sure the backend is running and that `backend/target/openapi/openapi.json` exists.

```bash
cd frontend
npm install
npm run generate-api
npm run dev
```

The frontend runs on e.g. `http://localhost:5173` (depending on Vite config).

### Demo Users

- **Admin** → `admin` / `admin`
- **User** → `user` / `user`

Login is done via the React login page; credentials are applied to the generated API client.
