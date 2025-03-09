package com.gameofthree.playerservice.service;

import com.gameofthree.playerservice.dto.GameMoveEventDto;
import com.gameofthree.playerservice.util.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static com.gameofthree.playerservice.util.Utils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlayerServiceTest {

    @Mock
    private GameRedisManager gameRedisManager;

    @Mock
    private GameEventService gameEventService;

    @InjectMocks
    private PlayerService playerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        PlayerService.playerId = null;
        doReturn(PLAYER_1_ID).when(gameRedisManager).registerPlayer();
        playerService = new PlayerService(gameEventService, gameRedisManager);
    }

    @Test
    void testPlayerRegistration() {
        assertEquals(PLAYER_1_ID, playerService.getPlayerId());
        verify(gameRedisManager, times(1)).registerPlayer();
    }

    @Test
    void testProcessMove_ValidMove() {
        // given a valid move
        GameMoveEventDto moveEvent = new GameMoveEventDto(0, 15, PLAYER_2_ID, PLAYER_1_ID);

        // when processing the move
        playerService.processMove(moveEvent);

        // then it should publish a new move event
        ArgumentCaptor<GameMoveEventDto> eventCaptor = ArgumentCaptor.forClass(GameMoveEventDto.class);
        ArgumentCaptor<String> routingKeyCaptor = ArgumentCaptor.forClass(String.class);

        verify(gameEventService, times(1)).publishMove(routingKeyCaptor.capture(), eventCaptor.capture());

        GameMoveEventDto publishedEvent = eventCaptor.getValue();
        assertEquals(PLAYER_2_ID, publishedEvent.getToPlayerId()); // should send to player 2
        assertEquals(5, publishedEvent.getNumberResult()); // since (15 + 0) / 3 = 5
    }

    @Test
    void testProcessMove_GameOver() {
        // given a move that results in game over
        GameMoveEventDto moveEvent = new GameMoveEventDto(0, 1, Utils.PLAYER_2_ID, Utils.PLAYER_1_ID);

        // when processing the move
        playerService.processMove(moveEvent);

        // then it should set the winner
        verify(gameRedisManager, times(1)).setWinner(Utils.PLAYER_2_ID);
        verify(gameEventService, never()).publishMove(anyString(), any()); // no further moves should be published
    }

    @Test
    void testProcessMove_WrongTurn() {
        // given a move that's not meant for this player
        GameMoveEventDto moveEvent = new GameMoveEventDto(0, 27, Utils.PLAYER_2_ID, Utils.PLAYER_2_ID);

        // when processing the move
        playerService.processMove(moveEvent);

        // then it should ignore the move
        verify(gameEventService, never()).publishMove(anyString(), any());
    }

    @Test
    void testCalculateNumberAdded() {
        int added1 = playerService.calculateNumberAdded(10);
        int added2 = playerService.calculateNumberAdded(9);
        int added3 = playerService.calculateNumberAdded(11);

        assertEquals(-1, added1);
        assertEquals(0, added2);
        assertEquals(1, added3);
    }
}
