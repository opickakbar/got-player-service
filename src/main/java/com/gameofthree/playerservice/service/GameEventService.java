package com.gameofthree.playerservice.service;

import com.gameofthree.playerservice.dto.GameMoveEventDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GameEventService {

    private final RabbitTemplate rabbitTemplate;

    public GameEventService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishMove(String routingKey, GameMoveEventDto gameMoveEventDto) {
        rabbitTemplate.convertAndSend("game.exchange", routingKey, gameMoveEventDto);
        log.info("Published message to queue {} for next player: {} with numberAdded: {} and result: {}", routingKey, gameMoveEventDto.getToPlayerId(), gameMoveEventDto.getNumberAdded(), gameMoveEventDto.getNumberResult());
    }
}
