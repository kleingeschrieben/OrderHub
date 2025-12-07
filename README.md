# OrderHub
Bestellsystem auf Basis von Java (Spring Boot) und React zur Verwaltung von Produkten und Bestellungen.

## Architekturentscheidungen

### Backend (Spring Boot)
- Spring Web: Bereitstellung der REST-API
- Spring Security:
  - In-Memory-Benutzer über `UserDetailsService`
  - Authentifizierung REST-API
- H2 In-Memory-Datenbank
- Spring Data JPA: Datenbankzugriff
- Swagger / OpenAPI:
  - API-Dokumentation
  - Export der OpenAPI-Spezifikation zur Generierung des TypeScript-API-Clients

### Frontend (React)
- Vite: Dev-Server und Build-Tool
- Bootstrap: CSS-Framework
- `openapi-typescript-codegen`: Generierung eines TypeScript-API-Clients aus der OpenAPI-Spezifikation
