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
        String existingPlayer1 = redisTemplate.opsForValue().get(PLAYER_1_ID);
        String existingPlayer2 = redisTemplate.opsForValue().get(PLAYER_2_ID);
        if (existingPlayer1 == null) {
            redisTemplate.opsForValue().set(PLAYER_1_ID, PLAYER_1_ID);
            return PLAYER_1_ID;
        }
        if (existingPlayer2 == null) {
            redisTemplate.opsForValue().set(PLAYER_2_ID, PLAYER_2_ID);
            return PLAYER_2_ID;
        }
        log.info("Both player slots are taken! Registering as Spectator.");
        return "Spectator";
    }

    public String getPlayer(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public String getOpponent(String playerRole) {
        return playerRole.equals(PLAYER_1_ID) ? PLAYER_2_ID : PLAYER_1_ID;
    }
}


