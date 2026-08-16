# Product CRUD API

A portfolio-ready REST API built with Java 17, Spring Boot, Spring Data JPA, Oracle, Maven, validation, and centralized exception handling.

## Architecture

`HTTP request -> ProductController -> ProductService -> ProductRepository -> Oracle`

## Requirements

- JDK 17+
- Oracle Database (XE or Free works)
- Maven 3.6.3+ or an IDE with Maven support
- Postman (optional)

## 1. Create the Oracle user

Run this as a DBA user. Change the password before using it outside local practice.

```sql
CREATE USER product_user IDENTIFIED BY product_password;
GRANT CREATE SESSION, CREATE TABLE, CREATE SEQUENCE TO product_user;
ALTER USER product_user QUOTA UNLIMITED ON USERS;
```

Hibernate creates the `PRODUCTS` table and `PRODUCT_SEQ` sequence when the application starts.

## 2. Configure the connection

The defaults in `application.properties` connect to Oracle XE/Free using:

```text
URL:      jdbc:oracle:thin:@localhost:1521/XEPDB1
Username: product_user
Password: product_password
```

For a public GitHub project, use environment variables instead of committing a password:

```powershell
$env:DB_URL="jdbc:oracle:thin:@localhost:1521/XEPDB1"
$env:DB_USERNAME="product_user"
$env:DB_PASSWORD="your_password"
```

## 3. Run the project

In Eclipse, import it with **File -> Import -> Existing Maven Projects**, select this folder, and run `ProductCrudApiApplication` as a Java application.

Or use a terminal:

```bash
mvn spring-boot:run
```

The API starts at `http://localhost:8080`.

## API endpoints

| Operation | Method | URL | Success status |
|---|---|---|---|
| Create | POST | `/api/products` | 201 Created |
| Read all | GET | `/api/products` | 200 OK |
| Read one | GET | `/api/products/{id}` | 200 OK |
| Update | PUT | `/api/products/{id}` | 200 OK |
| Delete | DELETE | `/api/products/{id}` | 204 No Content |

### Create or update request body

```json
{
  "name": "Mechanical Keyboard",
  "sku": "KEY-001",
  "price": 2499.00,
  "quantity": 10
}
```

Important error responses:

- `400 Bad Request`: validation failed
- `404 Not Found`: product ID does not exist
- `409 Conflict`: SKU already exists

## Run automated tests

Tests use an in-memory H2 database in Oracle compatibility mode, so Oracle does not need to be running:

```bash
mvn test
```

## What this project demonstrates

- Layered Controller-Service-Repository architecture
- Constructor dependency injection
- Spring Data JPA CRUD operations
- Oracle sequence-based ID generation
- Bean Validation with `@Valid`
- `ResponseEntity` and correct HTTP status codes
- Global error handling with `@RestControllerAdvice`
- Integration tests using MockMvc
- Safe configuration suitable for a public repository
