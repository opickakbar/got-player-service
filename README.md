# PlayerService - Game of Three

PlayerService is a microservice that represents an individual player in the "Game of Three." Each instance of PlayerService acts as either **Player 1** or **Player 2**, listening for game moves, processing turns, and sending responses using **RabbitMQ**.

## Features
- **Player Registration**: Automatically registers itself as either Player 1 or Player 2 upon startup.
- **RabbitMQ Integration**: Listens to game move events and publishes responses to the correct player queue.
- **Move Processing**: Determines the next move based on game logic and forwards it to the opponent.
- **Game Over Handling**: Detects when a player wins and records the winner in Redis.
- **Scalability**: Designed to work with multiple player instances running concurrently.

## Tech Stack
- **Java 21**
- **Spring Boot**
- **Spring AMQP (RabbitMQ)**
- **Redis**
- **JUnit & Mockito** (for testing)

## Configuration
PlayerService is configured using **application.yml**, specifying the RabbitMQ and Redis connection settings.

Example configuration:
```yaml
spring:
  rabbitmq:
    host: localhost
    port: 5672
    username: guest
    password: guest
  redis:
    host: localhost
    port: 6379
server:
  port: 8081  # Change to 8082 for Player 2
```

## Running PlayerService
Each PlayerService instance runs on a separate port to represent a unique player.

### Running Player 1
```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8081"
```

### Running Player 2
```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8082"
```

## Key Components
- **`PlayerService`**: Core service that registers the player, processes moves, and determines the next move.
- **`GameEventService`**: Publishes game move events to RabbitMQ.
- **`GameRedisManager`**: Manages player registration and winner storage in Redis.
- **`PlayerEventListener`**: Listens to incoming game move events and triggers `PlayerService`.
- **`RabbitConfig`**: Configures RabbitMQ queues and exchanges for message routing.

## Running Tests
To run unit tests:
```bash
mvn test
```
Expected output:
```
[INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

## Troubleshooting
| Issue                                                     | Solution                                                               |
|-----------------------------------------------------------|------------------------------------------------------------------------|
| Player not receiving moves                                | Ensure RabbitMQ is running (`docker ps`)                               |
| Instance registered as Spectator (not Player1 or Player2) | Clear Redis data (`docker exec -it <Container_id> redis-cli FLUSHALL`) |
| Only one player responding                                | Check RabbitMQ queues (`rabbitmqctl list_queues`)                      |
| Game does not start                                       | Ensure both players have registered                                    |

---

Made with love - Muhammad Taufik Akbar

