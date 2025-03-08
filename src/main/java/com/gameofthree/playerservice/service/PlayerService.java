package com.gameofthree.playerservice.service;

import com.gameofthree.playerservice.dto.GameMoveEventDto;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PlayerService {
    private final RabbitTemplate rabbitTemplate;
    @Getter
    private static String playerRole;
    private final PlayerRegistrationService playerRegistrationService;

    public PlayerService(RabbitTemplate rabbitTemplate, PlayerRegistrationService playerRegistrationService) {
        this.rabbitTemplate = rabbitTemplate;
        this.playerRegistrationService = playerRegistrationService;
        playerRole = playerRegistrationService.registerPlayer();
        log.info("✅ Player registered as {}", playerRole);
    }

    public void processMove(GameMoveEventDto eventDto) {
        log.info("Move received from player: {} with number: {}", eventDto.getPlayerId(), eventDto.getNumber());

        // 🔹 Validate if this instance should process the move
        if (!eventDto.getPlayerId().equals(playerRole)) {
            log.info("Ignoring move. Not my turn.");
            return;
        }

        int number = eventDto.getNumber();
        log.info("{} received: {}", playerRole, number);

        if (number == 1) {
            log.info("🎉 Game Over! {} wins!", playerRole);
            return;
        }

        int adjustment = (number % 3 == 0) ? 0 : ((number % 3 == 1) ? -1 : 1);
        int nextNumber = (number + adjustment) / 3;

        // 🔹 Get the opponent's ID
        String nextPlayerId = playerRegistrationService.getOpponent(playerRole);
        if (nextPlayerId == null) {
            log.warn("No opponent found. Game cannot continue.");
            return;
        }

        // ✅ Send message to correct queue
        String routingKey = nextPlayerId.equals("Player1") ? "player.1" : "player.2";
        rabbitTemplate.convertAndSend("game.exchange", routingKey, new GameMoveEventDto(nextNumber, nextPlayerId));
        log.info("📤 Published message to queue {} for next player: {} (ID: {}) with number: {}", routingKey, nextPlayerId, nextPlayerId, nextNumber);
    }

}



