package com.gameofthree.playerservice.listener;

import com.gameofthree.playerservice.dto.GameMoveEventDto;
import com.gameofthree.playerservice.service.PlayerService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PlayerEventListener {

    private final PlayerService playerService;

    public PlayerEventListener(PlayerService playerService) {
        this.playerService = playerService;
    }

    @RabbitListener(queues = "#{@playerQueue}")
    public void processMove(GameMoveEventDto eventDto) {
        System.out.println("🐇 Player " + PlayerService.getPlayerRole() + " Received Move: " + eventDto.getNumber());
        playerService.processMove(eventDto);
    }
}

