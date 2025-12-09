# Use of AI / Coding Assistants

During the development of OrderHub I used ChatGPT (Web) in a **supporting** role.  
All generated code and text was **manually reviewed, adapted and integrated** by me.

---

## Where AI was used

### Backend

- Spring Security configuration  
  (HTTP Basic setup, role-based access rules, H2 console access).
- Structure of the `GlobalExceptionHandler` and JSON error format.
- Support in designing and implementing the two example unit tests for `OrderService`

### Frontend

- Suggestions for component structure:
  - `AppShell`, `ProductsView`, `OrdersView`, `CartDropdown`,
    `ProductModal`, `PaginationBar`, etc.
- Patterns for managing:
  - auth state (including roles),
  - cart state with stock checks,
  - pagination in lists.

### Documentation

- First drafts for:
  - `README.md`
  - `docs/backend.md`
  - `docs/frontend.md`
  - `docs/api.md`
  - `docs/ai-usage.md`
  - `docs/limitations-and-future-work.md`
- I used AI suggestions as a starting point and then:
  - shortened and restructured the texts,
  - aligned them with the actual implementation,
  - fixed technical details (ports, commands, roles, endpoints).

---

## How AI suggestions were handled

- All suggestions were treated as **examples**, not as final solutions.
- I:
  - verified that code suggestions compile and work in this project,
  - adapted naming, structure and types to fit the existing codebase,
  - simplified or discarded over-engineered or unfitting examples,
  - ensured that the final implementation matches the requirements.

---

## Reflection

- **Strengths**
  - Very useful for boilerplate and common patterns (CRUD, pagination, modals, security configs).
  - Speeds up writing initial versions of documentation, which I then refined.
  - Helpful as a “second opinion” for small design decisions.

- **Limitations**
  - Some suggestions were too generic or more complex than needed and had to be simplified.
  - Occasionally, examples did not fully match my domain model, security rules or assignment scope and were discarded.
  - Documentation drafts sometimes contained outdated or irrelevant details and needed manual correction.

Overall, ChatGPT (Web) helped with routine work (boilerplate code and documentation drafts),  
but the **final design, implementation and documentation** were consciously decided and validated by me.
