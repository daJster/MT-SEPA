# Multi-Threaded SEPA Payment System (Spring + Virtual Threads)
**Author** : Jad El Karchi - [linkedin profile](www.linkedin.com/in/jad-el-karchi/)

[![Java](https://img.shields.io/badge/Java-21-blue?logo=java)](https://www.oracle.com/java/technologies/javase/21-relnote-issues.html)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5.4-green?logo=spring)](https://spring.io/projects/spring-boot)
[![License](https://img.shields.io/badge/License-MIT-informational.svg)](LICENSE)
[![Build](https://img.shields.io/badge/build-passing-brightgreen)]()
[![Virtual Threads](https://img.shields.io/badge/Virtual%20Threads-Enabled-orange)]()

A modern, benchmark-driven Spring Boot API that simulates a banking ecosystem for processing SEPA (Single Euro Payments Area) transactions using both **single-threaded** and **multi-threaded** approaches.

This project explores the performance advantages of **Virtual Threads** introduced in **Java 21** by benchmarking different implementations of transaction processing services.

---

## Tech Stack

- Java 21 with Virtual Threads (Project Loom)
- Spring Boot 3.x
- RESTful API (Spring Web)
- Maven Builder Wrapper (`mvnw`)
- Modular Service Factory Pattern
- Benchmarking Tools / Logging
- Simulated Banking Transactions

---

## Project Structure

```bash
src/main/java/com/example/sepapayment/
├── controller/            # REST controllers
│   └── TransactionController.java
├── service/               # Services
│   ├── STService.java     # Single-threaded implementation
│   ├── MTService.java     # Multi-threaded (Virtual Thread) implementation
│   └── TransactionService.java
├── factory/
│   └── TransactionServiceFactory.java
└── model/
    └── Transaction.java   # POJO for transactions
```

# API Entrypoints
| Method | Endpoint          | Description                      |
|--------|-------------------|----------------------------------|
| POST   | `api/transactions/process/ST`        | Process a transaction using selected strategy `ST` (Single-Threaded) |
| POST   | `api/transactions/process/MT`        | Process a transaction using selected strategy `MT` (Multi-Threaded) |


**Example:**

```bash
curl -X POST http://localhost:8080/api/transactions/process/ST \
     -H "Content-Type: application/json" \
     -d '{  
            "id": "1ad597", 
            "fromAccUUID": "A", 
            "toAccUUID": "B", 
            "amount": 500,
            "createdAt": "01/01/2025"
        }'
```

## Configuration
Set the server port in `application.properties` and enable UTC for consistency:


```bash
server.port=8080
spring.jackson.time-zone=UTC
```

