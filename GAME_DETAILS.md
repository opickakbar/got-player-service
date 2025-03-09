# Game of Three - Overview & Instructions

## Overview
The **Game of Three** is a simple, turn-based multiplayer game where two players take turns modifying a number until they reach 1. Each player can either add `-1`, `0`, or `1` to the current number to make it divisible by 3, then divide it by 3. The game continues until a player receives the number 1, declaring them the winner.

## How the Game Works
1. The **GameManagerService** starts the game by generating a random number (between 10 and 100) and sending it to **Player 1**.
2. **Player 1** processes the number:
    - Adds `-1`, `0`, or `1` to make it divisible by 3.
    - Divides it by 3.
    - Sends the new number to **Player 2** via RabbitMQ.
3. **Player 2** repeats the same process and sends it back to **Player 1**.
4. The game continues until a player receives `1`, which triggers the game over condition.
5. The **GameManagerService** retrieves the winner from Redis and announces it.

## System Architecture
The game is designed using a **microservices architecture** with event-driven communication:

```
+---------------------+       +---------------------+       +---------------------+
|  GameManagerService|       |    Player 1 (8081)  |       |    Player 2 (8082)  |
|  (Game Flow)       |       |  (Process Moves)   |       |  (Process Moves)   |
+---------------------+       +---------------------+       +---------------------+
            |                             |                          |
            |       +------------------+  |                          |
            +------>|      RabbitMQ     |<--------------------------+
                    +------------------+
```

### Key Components
1. **GameManagerService**
    - Controls game flow.
    - Sends the first move to Player 1.
    - Fetches and announces the winner.
    - Uses **Redis** for storing player registrations and the winner.

2. **PlayerService**
    - Each instance acts as Player 1 or Player 2.
    - Listens for game moves via **RabbitMQ**.
    - Processes moves and sends the updated number to the opponent.
    - Detects when the game ends.

3. **RabbitMQ** (Message Broker)
    - Handles event-driven communication between players.
    - Ensures asynchronous message delivery.

## Running the Game
### Prerequisites
Ensure **Docker**, **RabbitMQ**, and **Redis** are running:
```sh
docker-compose up --build
```

### Starting the Services
#### 1. Run the GameManagerService
```sh
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8091"
```

#### 2. Run the Player Services
Each player instance must run on a separate port.

**Player 1:**
```sh
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8081"
```

**Player 2:**
```sh
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8082"
```

## Playing the Game
### 1. Start the Game
Send a **POST** request to start the game:
```sh
curl -X POST http://localhost:8091/games/start
```
**Example Response:**
```json
"Game Started with number: 42"
```

### 2. Check the Game Result
Send a **GET** request to check the winner:
```sh
curl -X GET http://localhost:8091/games/result
```
**Example Response (Winner Exists):**
```json
"Winner is Player1"
```
**Example Response (Game Not Finished Yet):**
```json
"Game is not finished yet!"
```

You can also check the logs in the player instances to see how the game run and move they made.

## Troubleshooting
| Issue                                   | Solution |
|-----------------------------------------|----------|
| Player not receiving moves              | Ensure RabbitMQ is running (`docker ps`) |
| Instance registered as Spectator        | Clear Redis data (`docker exec -it <Container_id> redis-cli FLUSHALL`) |
| Only one player responding              | Check RabbitMQ queues (`rabbitmqctl list_queues`) |
| Game does not start                     | Ensure both players have registered |

---
Made with ❤️ by Muhammad Taufik Akbar

