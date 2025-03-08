package com.gameofthree.playerservice.service;

import com.gameofthree.playerservice.dto.GameMoveEventDto;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import static com.gameofthree.playerservice.util.PlayerUtil.*;

@Service
@Slf4j
public class PlayerService {
    private final RabbitTemplate rabbitTemplate;
    @Getter
    private static String playerId;
    private final PlayerRegistrationService playerRegistrationService;

    public PlayerService(RabbitTemplate rabbitTemplate, PlayerRegistrationService playerRegistrationService) {
        this.rabbitTemplate = rabbitTemplate;
        this.playerRegistrationService = playerRegistrationService;
        playerId = playerRegistrationService.registerPlayer();
        log.info("✅ Player registered as {}", playerId);
    }

    public void processMove(GameMoveEventDto eventDto) {
        if (!eventDto.getToPlayerId().equals(playerId) && !eventDto.getToPlayerId().equals(GAME_MASTER_ID)) {
            log.info("Ignoring move. Not my turn.");
            return;
        }

        int numberResult = eventDto.getNumberResult();
        log.info("{} received: {}", playerId, numberResult);

        if (numberResult == 1) {
            log.info("🎉 Game Over! {} wins!", playerId);
            return;
        }

        int nextNumberAdded = (numberResult % 3 == 0) ? 0 : ((numberResult % 3 == 1) ? -1 : 1);
        int nextNumberResult = (numberResult + nextNumberAdded) / 3;

        // Get the opponent's ID
        String nextPlayerId = playerRegistrationService.getOpponent(playerId);
        if (nextPlayerId == null) {
            log.warn("No opponent found. Game cannot continue.");
            return;
        }

        // Send message to correct queue
        String routingKey = nextPlayerId.equals(PLAYER_1_ID) ? "player.1" : "player.2";
        rabbitTemplate.convertAndSend("game.exchange", routingKey, new GameMoveEventDto(nextNumberAdded, nextNumberResult, playerId, nextPlayerId));
        log.info("📤 Published message to queue {} for next player: {} (ID: {}) with number: {}", routingKey, nextPlayerId, nextPlayerId, nextNumberResult);
    }

}



