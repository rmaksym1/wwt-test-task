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
- [🐳 Docker Networking](#-docker-networking)
- [🔁 End-to-End Flow](#-end-to-end-flow)

---

## 🏗 Architecture

```bash
Client
↓
auth-api (localhost:8080)
↓ (JWT + X-Internal-Token)
data-api (localhost:8081)
↓
PostgreSQL
```

---

## ⚙️ Tech Stack

- Java 17+
- Spring Boot (Web, Security, Data JPA)
- PostgreSQL
- JWT Authentication
- WebClient (service-to-service communication)
- Docker / Docker Compose
- BCrypt password hashing

---

## 📌 Key Features

- Stateless authentication (JWT)
- BCrypt password hashing
- Resilient service-to-service communication (WebClient)
- Secure inter-service communication (internal token validation)
- Persistent processing logs
- Containerized microservice architecture

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

## 🚀 How to Run

### 1. Build services

```bash
mvn -f auth-api/pom.xml clean package -DskipTests
mvn -f data-api/pom.xml clean package -DskipTests
```

### 2. Start system

```bash
docker compose up -d --build
```

### 🌐 Services

Service	URL
```bash
auth-api	http://localhost:8080 (by default)

data-api	http://localhost:8081 (by default)

postgres	localhost:5432
```


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
  "result": "OLLEH"
}
```

---

## 🔄 Service B (data-api)

### Transform endpoint

```bash
POST /api/transform
```

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

```bash
{
  "result": "TXET"
}
```
### ❗ Security Rule

#### Missing or invalid X-Internal-Token → 403 Forbidden

---

## 🐳 Docker Networking

All services run in a shared Docker network.

- auth-api → http://auth-api:8080
- data-api → http://data-api:8081

---

## 🔁 End-to-End Flow
1. User registers
2. User logs in → receives JWT 
3. Calls /api/process 
4. auth-api validates JWT 
5. auth-api calls data-api 
6. data-api validates internal token 
7. result returned 
8. log saved in database