# Known Limitations and Future Ideas

This document lists the main limitations of the current implementation and possible future improvements.

---

## Known Limitations

### Authentication & Users

- Users are stored **in memory** (via `UserDetailsService`).
- There is **no registration** or user management UI.
- Authentication uses **HTTP Basic Auth** only (no sessions, no tokens).

### Data & Persistence

- H2 is used as the only database, mainly for **local development**.
- Data model is intentionally simple (no categories, no user entity).

### Validation & Error Handling

- Validation is present but limited
- No field-level error display in the frontend; errors are mostly shown as generic messages.
- No centralized logging/monitoring setup beyond simple log output.

### Frontend UX / UI

- Styling is kept **minimal** and Bootstrap-based.
- No advanced responsiveness/layout tuning for small/mobile screens.
- No sorting of tables (only pagination + simple search on products).
- Orders cannot be edited in detail from the UI (only created and deleted).

### Testing

- Not enough automated tests are included.
- The system has been verified manually via the UI and Swagger.

---

## Future Ideas

### Backend

- Replace in-memory users with for example keycloak.
- Switch from H2 to a production-ready database (e.g. PostgreSQL).
- Introduce **JWT** authentication instead of pure Basic Auth.
- Add more advanced filtering/sorting for products and orders.
- Implement audit fields (createdAt, updatedAt, createdBy, etc.).

### Frontend

- Improve UX and responsiveness (mobile-friendly layout, better spacing, nicer modals).
- Add table sorting, more filters (e.g. price range, stock status).
- Edit orders in a dedicated UI (change quantities, add/remove items after creation).
- Show more detailed validation errors per field instead of generic messages.

### Quality & Operations

- Add more **unit tests** for service and controller layers.
- Add **frontend tests** (component or E2E tests).
- Introduce Docker setup for backend + frontend.
- Set up a CI pipeline (build, tests, linting).
