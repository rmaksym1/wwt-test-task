# 🚀 WinWin.travel Backend Test Task

## 🧠 Overview

This project implements a **two-service microservice system**:

- **auth-api (Service A)** → Authentication + request processing
- **data-api (Service B)** → Text transformation service
- **PostgreSQL** → persistence layer
- **Docker Compose** → full environment orchestration

Service A handles authentication and delegates processing to Service B.  
Service B performs a simple transformation and validates internal requests.

---

## 📍 Table of Contents

- [🏗 Architecture](#️-architecture)
- [⚙️ Tech Stack](#-tech-stack)
- [📌 Key Features](#-key-features)
- [🔧 Configuration](#-configuration)
- [🚀 How to Run](#-how-to-run)
- [🔐 Authentication Flow](#-authentication-flow)
- [⚡ Process Flow](#-process-flow)
- [🔄 Service B](#-service-b-data-api)
- [🔁 End-to-End Flow](#-end-to-end-flow)

---

## 🏗 Architecture

```text
┌─────────┐
│ Client  │
└────┬────┘
     │
     │ Register / Login
     ▼
┌────────────────────┐
│ auth-api           │
│ JWT Authentication │
└────┬───────────────┘
     │
     │ Returns JWT Token
     ▼
┌─────────┐
│ Client  │
└────┬────┘
     │
     │ Protected Request (Bearer JWT)
     ▼
┌────────────────────┐
│ auth-api           │
│ Request Processing │
└────┬──────────┬────┘
     │          ▼
     │    ┌────────────┐
     │    │ PostgreSQL │
     │    └────────────┘
     │
     │ HTTP + X-Internal-Token
     ▼
┌────────────────────┐
│ data-api           │
│ Transformation API │
└────┬───────────────┘
```

#### [⬆ Back to Table of Contents](#-table-of-contents)

---

## ⚙️ Tech Stack

| Category      | Technologies                          |
|---------------|---------------------------------------|
| Language      | Java 17+                              |
| Framework     | Spring Boot (Web, Security, Data JPA) |
| Database      | PostgreSQL                            |
| Security      | JWT Authentication, BCrypt            |
| Communication | WebClient (service-to-service)        |
| DevOps        | Docker, Docker Compose                |

#### [⬆ Back to Table of Contents](#-table-of-contents)

---

## 📌 Key Features

- Stateless authentication (JWT)
- BCrypt password hashing
- Resilient service-to-service communication (WebClient)
- Secure inter-service communication (internal token validation)
- Persistent processing logs
- Containerized microservice architecture

#### [⬆ Back to Table of Contents](#-table-of-contents)

---

## 🔧 Configuration
You can use .env.template, or template below:

```bash
POSTGRES_USER=your_user
POSTGRES_PASSWORD=your_password
POSTGRES_DB=your_db
POSTGRES_LOCAL_PORT=5433

AUTH_DOCKER_PORT=8080
DATA_DOCKER_PORT=8081

INTERNAL_TOKEN=your_x_internal_token

JWT_EXPIRATION=your_jwt_expiration
JWT_SECRET=your_jwt_secret

DEBUG_PORT=5005
```

#### [⬆ Back to Table of Contents](#-table-of-contents)

---

## 🚀 How to Run

### 1. Build services

```bash
mvn -f auth-api/pom.xml clean package -DskipTests
mvn -f data-api/pom.xml clean package -DskipTests
```

### Run tests (Recommended)

```bash
mvn -f auth-api/pom.xml test
```

### 2. Start docker

```bash
docker compose up -d --build
```

### 🐳 Docker Networking

All services run in a shared Docker network.

- auth-api → http://auth-api:8080
- data-api → http://data-api:8081

#### [⬆ Back to Table of Contents](#-table-of-contents)

---

## 🔐 Authentication Flow

#### Register
```bash
curl -X POST http://localhost:8080/api/auth/register \
-H "Content-Type: application/json" \
-d '{"email":"test@test.com","password":"password","repeatPassword":"password"}'
```
#### Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
-H "Content-Type: application/json" \
-d '{"email":"test@test.com","password":"password"}'
```
#### Response:

```bash
{
  "token": "JWT_TOKEN"
}
```

#### [⬆ Back to Table of Contents](#-table-of-contents)

---

## ⚡ Process Flow

### Call protected endpoint

```bash
curl -X POST http://localhost:8080/api/process \
-H "Authorization: Bearer <JWT_TOKEN>" \
-H "Content-Type: application/json" \
-d '{"text":"hello"}'
```
#### Response:

```bash
{
  "text": "OLLEH"
}
```

#### [⬆ Back to Table of Contents](#-table-of-contents)

---

## 🔄 Service B (data-api)

### Transform endpoint

`POST /api/transform`

#### Headers

```bash
X-Internal-Token: <INTERNAL_TOKEN>
```

#### Request

```bash
{
  "text": "text"
}
```

#### Response

```json
{
  "text": "TXET"
}
```
### ❗ Security Rule

#### Missing or invalid X-Internal-Token → 403 Forbidden

#### [⬆ Back to Table of Contents](#-table-of-contents)

---

## 🔁 End-to-End Flow
**User → Auth API → JWT → Process → Data API → Response**

#### [⬆ Back to Table of Contents](#-table-of-contents)