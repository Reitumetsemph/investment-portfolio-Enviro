# Enviro365 Investments — Junior Developer Assessment

A full-stack Spring Boot application that allows investors to view their portfolio,
submit withdrawals (with business-rule validation), view withdrawal history, and
download a filtered CSV statement.

---

## Tech stack

- **Backend:** Spring Boot 3.5, Spring Web, Spring Data JPA, Bean Validation
- **Database:** H2 (in-memory)
- **Frontend:** Plain HTML + JavaScript, styled with Bootstrap 5 (loaded from CDN)
- **Build:** Maven (wrapper included)
- **Java version:** 21

---

## Prerequisites

- JDK 21 (or higher) installed and on `PATH`
- An internet connection on first run so Maven can download dependencies

No Maven installation needed — the project ships with the Maven wrapper (`mvnw`).

---

## Running the application

From the project root:

```powershell
# Windows PowerShell
./mvnw spring-boot:run
```

```bash
# macOS / Linux
./mvnw spring-boot:run
```

When you see `Started InvestmentPortfolioApplication`, the app is ready.

- **Web UI:** <http://localhost:8080>
- **H2 console:** <http://localhost:8080/h2-console>
  - JDBC URL: `jdbc:h2:mem:investorsdb`
  - User: `sa`
  - Password: *(blank)*

### Seed data

On startup, the app seeds two investors and three products so the UI has
something to work with immediately:

| ID | Name | Email | Age |
|---|---|---|---|
| 1 | Alice Young | alice@enviro365.com | 30 |
| 2 | Bob Senior | bob@enviro365.com | 70 |

| ID | Type | Balance | Owner |
|---|---|---|---|
| 1 | SAVINGS | R 10,000 | Alice (1) |
| 2 | RETIREMENT | R 50,000 | Alice (1) |
| 3 | RETIREMENT | R 100,000 | Bob (2) |

This means Alice (under 65) cannot withdraw from her retirement product,
but Bob (over 65) can — a quick way to demo the age rule.

---

## Using the UI

1. **View Portfolio** — enter an investor ID (1 or 2) and click *View Portfolio*.
2. **Submit Withdrawal** — enter a product ID and amount, click *Submit Withdrawal*.
   The portfolio refreshes automatically on success. Business-rule errors
   from the backend are displayed inline.
3. **Withdrawal History** — enter an investor ID and click *Load History*.
4. **Download CSV** — pick a From/To date range and click *Download CSV*.

---

## REST API

All endpoints return JSON unless noted. Error responses are shaped as
`{"error": "message"}` and use appropriate HTTP status codes (400, 404, 500).

### Investors

| Method | URL | Description |
|---|---|---|
| `POST` | `/investors` | Create an investor (`fullName`, `email`, `age`) |
| `GET` | `/investors` | List all investors |
| `GET` | `/investors/{id}` | Get a single investor |
| `GET` | `/investors/{id}/portfolio` | Get an investor's details + their products |

### Products

| Method | URL | Description |
|---|---|---|
| `POST` | `/products` | Create a product (`type`, `balance`, `investorId`) |
| `GET` | `/products` | List all products |

### Withdrawals

| Method | URL | Description |
|---|---|---|
| `POST` | `/withdrawals` | Submit a withdrawal (`productId`, `amount`) |
| `GET` | `/withdrawals/investor/{investorId}` | List an investor's withdrawal history |
| `GET` | `/withdrawals/investor/{investorId}/export?from=YYYY-MM-DD&to=YYYY-MM-DD` | Download CSV of withdrawals in date range |

---

## Business rules (enforced in `WithdrawalService`)

1. Amount must be greater than zero.
2. **Retirement** withdrawals are only allowed when the investor's age is **greater than 65**.
3. Withdrawal amount must **not exceed the product's current balance**.
4. Withdrawal amount must **not exceed 90%** of the product's current balance.

Rule violations return HTTP **400** with the failing rule's message in the body.

---

## Architecture

Standard layered Spring Boot architecture:

```
Controller (HTTP)      → InvestorController, ProductController, WithdrawalController
Service (business)     → InvestorService, ProductService, WithdrawalService
Repository (data)      → InvestorRepository, ProductRepository, WithdrawalNoticeRepository
Entity (persistence)   → Investor, Product, WithdrawalNotice
DTO (transfer)         → PortfolioResponse, ProductRequest, WithdrawalRequest, ErrorResponse
Exception              → GlobalExceptionHandler (@RestControllerAdvice)
Config                 → DataSeeder (CommandLineRunner)
```

Controllers receive HTTP requests, delegate to services. Services hold all
business logic and orchestrate repositories. Repositories extend
`JpaRepository` and use Spring Data's derived queries — no SQL is written
by hand.

---

## Advanced requirements covered

- ✅ **Global exception handling** — `GlobalExceptionHandler` maps
  `IllegalArgumentException` → 400, `NoSuchElementException` → 404,
  `MethodArgumentNotValidException` → 400 with aggregated field messages,
  and a catch-all → 500.
- ✅ **DTO layer** — incoming requests use dedicated DTOs
  (`ProductRequest`, `WithdrawalRequest`); the portfolio response uses
  `PortfolioResponse`; errors use `ErrorResponse`.
- ✅ **Input validation** — `@Valid` on controllers with `@NotBlank`,
  `@Email`, `@Min`, `@NotNull`, `@Positive` annotations on entities/DTOs.
- ✅ **UI validation** — the frontend rejects empty fields, non-positive
  amounts, and inverted date ranges before calling the API.

---

## Screenshots

> _Replace these placeholders with your own screenshots before submission._

- `screenshots/portfolio.png` — Portfolio dashboard with Alice loaded
- `screenshots/withdrawal-success.png` — Successful withdrawal + refreshed balance
- `screenshots/withdrawal-rule.png` — Business rule firing (red message)
- `screenshots/history.png` — Withdrawal history table
- `screenshots/csv.png` — Downloaded CSV opened in Excel/Notepad

---

## AI usage

GitHub Copilot (Claude model) was used as a pair-programming assistant
during development. Specifically:

- Explained Spring Boot, JPA, and Jackson concepts step-by-step while the
  code was written by hand.
- Suggested annotations, derived-query method names, and idiomatic patterns.
- Helped diagnose PowerShell quoting issues during API testing.

All code in this repository was typed by the author, reviewed line by line,
and is understood by the author. No code was generated and committed
without review.

---

## Author

**Name:** Reitumetse Mphahlele
**Email:** _your.email@example.com_

---

## Project layout

```
investment-portfolio/
├── src/
│   ├── main/
│   │   ├── java/com/enviro/assessment/junior/reitumetse/
│   │   │   ├── InvestmentPortfolioApplication.java
│   │   │   ├── config/DataSeeder.java
│   │   │   ├── controller/
│   │   │   ├── dto/
│   │   │   ├── entity/
│   │   │   ├── exception/
│   │   │   ├── repository/
│   │   │   └── service/
│   │   └── resources/
│   │       ├── application.properties
│   │       └── static/
│   │           ├── index.html
│   │           └── app.js
│   └── test/
└── pom.xml
```
