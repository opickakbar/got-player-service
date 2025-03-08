package com.gameofthree.playerservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import static com.gameofthree.playerservice.util.PlayerUtil.*;

@Service
@Slf4j
public class PlayerRegistrationService {

    private final StringRedisTemplate redisTemplate;

    public PlayerRegistrationService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public synchronized String registerPlayer() {
        String existingPlayer1 = redisTemplate.opsForValue().get(PLAYER_1_KEY_AND_VALUE);
        String existingPlayer2 = redisTemplate.opsForValue().get(PLAYER_2_KEY_AND_VALUE);
        if (existingPlayer1 == null) {
            redisTemplate.opsForValue().set(PLAYER_1_KEY_AND_VALUE, PLAYER_1_KEY_AND_VALUE);
            return "Player1";
        }
        if (existingPlayer2 == null) {
            redisTemplate.opsForValue().set(PLAYER_2_KEY_AND_VALUE, PLAYER_2_KEY_AND_VALUE);
            return "Player2";
        }
        log.info("Both player slots are taken! Registering as Spectator.");
        return "Spectator";
    }

    public String getPlayer1() {
        return redisTemplate.opsForValue().get(PLAYER_1_KEY_AND_VALUE);
    }

    public String getPlayer2() {
        return redisTemplate.opsForValue().get(PLAYER_2_KEY_AND_VALUE);
    }

    public String getOpponent(String playerRole) {
        return playerRole.equals("Player1") ? "Player2" : "Player1";
    }
}


