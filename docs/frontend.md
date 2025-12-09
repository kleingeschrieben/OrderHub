# Frontend

The frontend is built with **React** and **TypeScript** and communicates with the backend via a generated TypeScript API client.  
This project is my **first time working with React** – in my day-to-day work I usually use **Angular**, so the implementation focuses on clear, simple patterns rather than advanced React concepts.

---

## Technology Stack

- **React + TypeScript**
  - Functional components with hooks (`useState`, `useEffect`).
  - Simple local state management (auth, active page, cart).

- **Vite**
  - Dev server and build tool.
  - Runs the app on a local dev port (e.g. `http://localhost:5173`).
  - To avoid CORS issues in development, a **Vite proxy** is used for all `/api` requests and forwards them to the backend.

- **Bootstrap**
  - CSS framework for layout and styling (navbar, tables, accordion, modal, buttons).

- **openapi-typescript-codegen**
  - Generates the TypeScript API client from the backend’s OpenAPI spec.
  - Uses `backend/target/openapi/openapi.json` as input.
  - Output is written to `src/generated-api/`.

---

## Structure (Overview)

- `src/generated-api/`
  - Generated client (e.g. `ProductsService`, `OrdersService`, `AuthService`, `OpenAPI`).

- `src/models/`
  - `AuthCredentials.ts` – auth state including `username`, `password`, `roles`.
  - `CartItem.ts` – type for cart items (product + quantity).

- `src/components/`
  - `layout/AppShell.tsx`
    - Main layout with navbar, cart dropdown and page content.
  - `auth/LoginPage.tsx`
    - Login form; on success sets credentials in `OpenAPI` (Basic Auth) and updates `AuthState`.
  - `cart/CartDropdown.tsx`
    - Shopping cart dropdown in the navbar (increase/decrease quantities, checkout).
  - `products/ProductsView.tsx`
    - Product table with search, pagination and admin actions.
  - `products/ProductModal.tsx`
    - Modal dialog for creating/editing a product.
  - `orders/OrdersView.tsx`
    - Accordion-style order list with line items and pagination.
  - `utils/PaginationBar.tsx`
    - Reusable pagination component.

- `src/App.tsx`
  - Holds global UI state:
    - `auth` (including roles),
    - `activePage` (`products` / `orders`),
    - cart items and cart open/closed.
  - Passes props into `AppShell`, `ProductsView` and `OrdersView`.
  - Computes `isAdmin` via `auth.roles.includes("ROLE_ADMIN")` and hides/shows admin features in the UI.

---

## Vite Proxy (CORS)

In `vite.config.ts` a proxy is configured so that frontend requests to `/api` are forwarded to the backend:

```ts
import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      "/api": {
        target: "http://localhost:1337",
        changeOrigin: true,
      },
    },
  },
});
