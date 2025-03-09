package com.gameofthree.playerservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static com.gameofthree.playerservice.util.Utils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameRedisManagerTest {

    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private GameRedisManager gameRedisManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void testRegisterPlayer_Player1() {
        when(valueOperations.get(PLAYER_1_ID)).thenReturn(null);
        when(valueOperations.get(PLAYER_2_ID)).thenReturn(null);

        String registeredPlayer = gameRedisManager.registerPlayer();

        assertEquals(PLAYER_1_ID, registeredPlayer);
        verify(valueOperations, times(1)).set(PLAYER_1_ID, PLAYER_1_ID);
    }

    @Test
    void testRegisterPlayer_Player2() {
        when(valueOperations.get(PLAYER_1_ID)).thenReturn(PLAYER_1_ID);
        when(valueOperations.get(PLAYER_2_ID)).thenReturn(null);

        String registeredPlayer = gameRedisManager.registerPlayer();

        assertEquals(PLAYER_2_ID, registeredPlayer);
        verify(valueOperations, times(1)).set(PLAYER_2_ID, PLAYER_2_ID);
    }

    @Test
    void testRegisterPlayer_Spectator() {
        when(valueOperations.get(PLAYER_1_ID)).thenReturn(PLAYER_1_ID);
        when(valueOperations.get(PLAYER_2_ID)).thenReturn(PLAYER_2_ID);

        String registeredPlayer = gameRedisManager.registerPlayer();

        assertEquals("Spectator", registeredPlayer);
        verify(valueOperations, never()).set(eq(PLAYER_1_ID), anyString());
        verify(valueOperations, never()).set(eq(PLAYER_2_ID), anyString());
    }

    @Test
    void testSetWinner() {
        gameRedisManager.setWinner(PLAYER_1_ID);
        verify(valueOperations, times(1)).set(WINNER_ID, PLAYER_1_ID);
    }
}
