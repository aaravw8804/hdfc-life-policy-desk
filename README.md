# HDFC Life Policy Desk API

A Spring Boot 3 / Java 17 REST API for HDFC Life policies and claims. Policy and
claim data live in an in-memory store (`PolicyStore` / `InMemoryPolicyStore`);
Flyway owns the relational schema (`customers`, `policies`, `claims`, `riders`,
`policy_riders`) so the database is always ready to receive real data later.

## How to run

Dev profile (H2, default):

```bash
./mvnw spring-boot:run
```

or

```bash
mvn spring-boot:run
```

The app starts on `http://localhost:8080` with the `dev` profile active,
Flyway migrations applied against an in-memory H2 database, and the six seed
policies loaded.

Prod profile (PostgreSQL) needs `DB_URL`, `DB_USER`, `DB_PASSWORD` set, then:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

Swagger UI: open **`http://localhost:8080/swagger-ui/index.html`** after
starting the app. OpenAPI JSON is at `http://localhost:8080/v3/api-docs`.

## Endpoints

| Method | Path | Behaviour | Status |
|---|---|---|---|
| GET | `/api/policies` | All policies (optionally `?status=` and/or `?type=`) | 200 |
| GET | `/api/policies/{policyNo}` | One policy | 200 / 404 |
| GET | `/api/policies?status=Active` | Filter by status | 200 |
| GET | `/api/policies?type=TERM` | Filter by type | 200 |
| POST | `/api/policies` | Create a policy | 201 / 409 |
| PUT | `/api/policies/{policyNo}` | Replace a policy's details | 200 / 404 |
| DELETE | `/api/policies/{policyNo}` | Remove a policy | 204 / 404 |
| GET | `/api/policies/{policyNo}/claims` | Claims for a policy, oldest first | 200 / 404 |
| POST | `/api/claims` | File a claim | 201 / 400 / 404 |
| GET | `/api/claims/{claimNo}` | One claim | 200 / 404 |

## Entity-relationship list

- **customers** — `full_name` (unique), `email` (unique)
- **policies** — `policy_no` (unique) → FK `customer_id` → `customers.id`
- **claims** — `claim_no` (unique) → FK `policy_id` → `policies.id`
- **riders** — `code` (unique), `name`
- **policy_riders** — junction table, FK `policy_id` → `policies.id`, FK `rider_id` → `riders.id`, composite PK `(policy_id, rider_id)`

## In-memory store vs PostgreSQL

The in-memory store is fine while the desk is small, single-instance, and the
data can be rebuilt from a seed on every restart — that's the case here, so
REST reads/writes stay off the database entirely. It stops being fine the
moment the app needs to survive a restart, run more than one instance, or
share data with another service, at which point PostgreSQL (or another
durable store) takes over. Flyway earns its place independent of that
decision: unlike `ddl-auto=update`, it gives versioned, ordered, checked-in
migration scripts that run the same way in every environment, so schema
changes are reviewable, repeatable, and safe to run against production data —
`ddl-auto=update` just infers a shape from entities and can silently drop or
alter columns in ways nobody reviewed.
