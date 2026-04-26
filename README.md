#  NearBuy - E-Commerce Backend Application

A comprehensive **e-commerce backend system** built using Spring Boot, PostgreSQL, and JPA, supporting complete workflows from product browsing to order placement and payment.

---

## 📋 Table of Contents

* [Overview](#overview)
* [Tech Stack](#tech-stack)
* [Architecture](#architecture)
* [Database Schema](#database-schema)
* [API Endpoints](#api-endpoints)
* [Setup Instructions](#setup-instructions)
* [Running the Application](#running-the-application)
* [Testing with Postman](#testing-with-postman)
* [Key Features](#key-features)
* [Data Models](#data-models)
* [Common Queries](#common-queries)

---

## Overview

NearBuy is a backend application that manages the complete lifecycle of an e-commerce platform, including:

* Product and category management
* Cart management
* Order processing and checkout
* Address management
* Payment simulation

---

## Tech Stack

* **Framework:** Spring Boot 4.x
* **Language:** Java 21+
* **Database:** PostgreSQL
* **ORM:** Spring Data JPA / Hibernate
* **Build Tool:** Maven
* **API Testing:** Postman
* **Documentation:** Swagger/OpenAPI + Postman collection

---

## Architecture

The application follows a **layered architecture**:

```text
Controller → Service → Repository → Database
```

### Layers

* **Controller:** Handles HTTP requests and responses
* **Service:** Contains business logic
* **Repository:** Handles DB operations via JPA
* **DTOs:** Used for request/response separation

---

## Database Schema

### Core Entities

#### users

```sql
- id (PK)
- name
- email
- password
- role (USER / SELLER / ADMIN)
```

---

#### categories

```sql
- id (PK)
- name
```

---

#### products

```sql
- id (PK)
- name
- description
- price
- stock
- category_id (FK)
```

---

#### carts

```sql
- id (PK)
- user_id
```

---

#### cart_items

```sql
- id (PK)
- cart_id (FK)
- product_id (FK)
- quantity
```

---

#### orders

```sql
- id (PK)
- user_id
- total_amount
- status
- address_id (FK)
```

---

#### order_items

```sql
- id (PK)
- order_id (FK)
- product_id (FK)
- quantity
- price
```

---

#### addresses

```sql
- id (PK)
- user_id
- street
- city
- state
- zip_code
- country
```

---

#### payments

```sql
- id (PK)
- order_id
- amount
- status
```

---

## 🔌 API Endpoints

### Auth APIs

```http
POST /auth/register
POST /auth/login
```

---

### Category APIs

```http
POST /categories
GET  /categories
GET  /categories/{categoryId}
```

---

### Product APIs

```http
POST /products
GET  /products?page=0&size=5&sort=price,desc
GET  /products/search?keyword=iphone
GET  /products/filter?min=1000&max=50000
```

---

### Cart APIs

```http
POST   /cart
GET    /cart
DELETE /cart/items/{itemId}
```

---

### Address APIs

```http
POST /address
GET  /address
```

---

### Order APIs

```http
POST /orders/checkout
GET  /orders
PUT  /orders/{orderId}/status?status=SHIPPED
```

---

### Payment APIs

```http
POST /payments/{orderId}?successful=true
```

Except `/auth/register` and `/auth/login`, APIs require:

```http
Authorization: Bearer <jwt-token>
```

---

## Setup Instructions

### Prerequisites

* Java 21+
* PostgreSQL
* Maven

---

### Database Setup

```sql
CREATE DATABASE nearbuy;
```

Update:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/nearbuy
spring.datasource.username=postgres
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

---

## ▶ Running the Application

```bash
mvn spring-boot:run
```

Application runs at:

```text
http://localhost:8080
```

Swagger UI runs at:

```text
http://localhost:8080/swagger-ui.html
```

OpenAPI JSON runs at:

```text
http://localhost:8080/v3/api-docs
```

---

##Testing with Postman

* Import your Postman collection
* Set base URL:

```text
http://localhost:8080
```

---

## Key Features

###  Cart System

* Prevents duplicate entries
* Updates quantity instead
* Calculates total price

---

### Checkout System

```text
Cart → Order → Payment
```

* Converts cart items to order items
* Clears cart after checkout

---

### Address Integration

* Each order is linked to delivery address

---

### Payment System

* Mock payment processing
* Tracks SUCCESS / FAILED status

---

### Exception Handling

* Global exception handling
* Validation error support
* Clean JSON responses

---

## Data Models

### RoleType

```text
USER
SELLER
ADMIN
```

---

### OrderStatus

```text
PLACED
CONFIRMED
SHIPPED
DELIVERED
CANCELLED
```

---

### PaymentStatus

```text
PENDING
SUCCESS
FAILED
```

---

##  Common Queries

### Get Products by Category

```sql
SELECT * FROM products WHERE category_id = 1;
```

---

### Get Cart Items

```sql
SELECT * FROM cart_items WHERE cart_id = 1;
```

---

### Get Orders by User

```sql
SELECT * FROM orders WHERE user_id = 1;
```

---

### Get Order with Address

```sql
SELECT o.id, o.total_amount, a.city
FROM orders o
JOIN addresses a ON o.address_id = a.id;
```

---

## Notes

### Best Practices Used

* DTO-based architecture
* Layered structure
* Pagination for scalability
* Exception handling using `@RestControllerAdvice`
* Logging using `@Slf4j`

---

##  Contribution

* Follow project structure
* Maintain clean code
* Add API documentation for new endpoints

---

## License

This project is built for learning and demonstration purposes.



 If you like this project, give it a star!
