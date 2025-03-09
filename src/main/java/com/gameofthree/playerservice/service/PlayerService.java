package com.gameofthree.playerservice.service;

import com.gameofthree.playerservice.dto.GameMoveEventDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.gameofthree.playerservice.util.Utils.GAME_MASTER_ID;
import static com.gameofthree.playerservice.util.Utils.PLAYER_1_ID;
import static com.gameofthree.playerservice.util.Utils.PLAYER_1_ROUTING_KEY;
import static com.gameofthree.playerservice.util.Utils.PLAYER_2_ID;
import static com.gameofthree.playerservice.util.Utils.PLAYER_2_ROUTING_KEY;

@Service
@Slf4j
public class PlayerService {

    private final GameRedisManager gameRedisManager;
    private final GameEventService gameEventService;
    private static String playerId;

    public PlayerService(GameEventService gameEventService, GameRedisManager gameRedisManager) {
        this.gameEventService = gameEventService;
        this.gameRedisManager = gameRedisManager;
        playerId = gameRedisManager.registerPlayer();
        log.info("Player registered as {}", playerId);
    }

    public void processMove(GameMoveEventDto eventDto) {
        if (!eventDto.getToPlayerId().equals(playerId) && !eventDto.getFromPlayerId().equals(GAME_MASTER_ID)) {
            log.info("Ignoring move. Not my turn.");
            return;
        }
        int numberResult = eventDto.getNumberResult();
        if (numberResult == 1) {
            log.info("Game Over! {} wins!", eventDto.getFromPlayerId());
            gameRedisManager.setWinner(eventDto.getFromPlayerId());
            return;
        }

        GameMoveEventDto nextGameMoveEvent = processNextMove(eventDto);
        String routingKey = nextGameMoveEvent.getToPlayerId().equals(PLAYER_1_ID) ? PLAYER_1_ROUTING_KEY : PLAYER_2_ROUTING_KEY;
        gameEventService.publishMove(routingKey, nextGameMoveEvent);
    }

    private GameMoveEventDto processNextMove(GameMoveEventDto eventDto) {
        int numberResult = eventDto.getNumberResult();
        int newNumberAdded = calculateNumberAdded(numberResult);
        int newNumberResult = (numberResult + newNumberAdded) / 3;
        String opponentId = playerId.equals(PLAYER_1_ID) ? PLAYER_2_ID : PLAYER_1_ID;
        return new GameMoveEventDto(newNumberAdded, newNumberResult, playerId, opponentId);
    }

    private int calculateNumberAdded(int numberResult) {
        if (numberResult % 3 == 0) return 0;
        if ((numberResult + 1) % 3 == 0) return 1;
        return -1;
    }

    public static String getPlayerId() { return playerId; }

}



