package com.gameofthree.playerservice.listener;

import com.gameofthree.playerservice.dto.GameMoveEventDto;
import com.gameofthree.playerservice.service.PlayerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PlayerEventListener {

    private final PlayerService playerService;

    public PlayerEventListener(PlayerService playerService) {
        this.playerService = playerService;
    }

    @RabbitListener(queues = "#{@playerQueue}")
    public void processMove(GameMoveEventDto eventDto) {
        log.info("{} Received Move: {} with Result: {} from Player: {}", PlayerService.getPlayerId(), eventDto.getNumberAdded(), eventDto.getNumberResult(), eventDto.getFromPlayerId());
        playerService.processMove(eventDto);
    }
}

