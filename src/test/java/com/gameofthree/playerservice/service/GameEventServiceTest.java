package com.gameofthree.playerservice.service;

import com.gameofthree.playerservice.dto.GameMoveEventDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static com.gameofthree.playerservice.util.Utils.*;
import static org.mockito.Mockito.*;

class GameEventServiceTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private GameEventService gameEventService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPublishMove() {
        // given
        GameMoveEventDto gameMoveEventDto = new GameMoveEventDto(1, 10, PLAYER_1_ID, PLAYER_2_ID);

        // when
        gameEventService.publishMove(PLAYER_2_ROUTING_KEY, gameMoveEventDto);

        // then
        verify(rabbitTemplate, times(1)).convertAndSend(GAME_EXCHANGE, PLAYER_2_ROUTING_KEY, gameMoveEventDto);
    }
}

