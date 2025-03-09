# PlayerService

## Overview
The **PlayerService** represents an individual player in the "Game of Three." Each instance acts as either **Player 1** or **Player 2**, processing moves, sending responses, and interacting with **GameManagerService** via **RabbitMQ**.

## Features
- **Automatic Player Registration**: Registers itself as Player 1 or Player 2 upon startup.
- **Move Processing**: Listens for game move events and computes the next move.
- **RabbitMQ Integration**: Uses event-driven messaging for turn-based gameplay.
- **Game Over Detection**: Identifies when the game ends and records the winner in **Redis**.

## Technologies Used
- **Java 21**
- **Spring Boot** (REST API, Dependency Injection)
- **Spring AMQP (RabbitMQ)** (Message Queue for Communication)
- **Redis** (Centralized Caching Player & State)
- **JUnit & Mockito** (Unit Testing)

## Project Structure
```
PlayerService
├── config
│   ├── RabbitConfig.java
├── controller
│   ├── PlayerEventListener.java
├── service
│   ├── PlayerService.java
│   ├── GameEventService.java
│   ├── GameRedisManager.java
├── dto
│   ├── GameMoveEventDto.java
├── util
│   ├── Utils.java
```

## Running the Service
Before starting, ensure **RabbitMQ** and **Redis** are running.

### Steps to Run:
1. **Start RabbitMQ & Redis**
   ```sh
   docker-compose up --build
   ```
2. **Run PlayerService**

   **Running Player 1:**
   ```sh
   mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8081"
   ```

   **Running Player 2:**
   ```sh
   mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8082"
   ```

## Running Tests
To execute unit tests:
```sh
mvn test
```
Expected output:
```
[INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

---
Made with ❤️ by Muhammad Taufik Akbar