package com.gameofthree.playerservice.controller;

import com.gameofthree.playerservice.dto.GameMoveEventDto;
import com.gameofthree.playerservice.service.PlayerRegistrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/game")
@Slf4j
public class PlayerController {

    private final RabbitTemplate rabbitTemplate;
    private final PlayerRegistrationService playerRegistrationService;

    public PlayerController(RabbitTemplate rabbitTemplate, PlayerRegistrationService playerRegistrationService) {
        this.rabbitTemplate = rabbitTemplate;
        this.playerRegistrationService = playerRegistrationService;
    }

    @PostMapping("/start")
    public String startGame() {
        int initialNumber = (int) (Math.random() * 100) + 10;
        String player1Id = playerRegistrationService.getPlayer1();
        if (player1Id == null) {
            return "Error: Player1 has not registered yet!";
        }

        // ✅ Send the message to the correct queue using "player.1" routing key
        rabbitTemplate.convertAndSend("game.exchange", "player.1", new GameMoveEventDto(initialNumber, "Player1"));
        return "Game Started with number: " + initialNumber;
    }
}

