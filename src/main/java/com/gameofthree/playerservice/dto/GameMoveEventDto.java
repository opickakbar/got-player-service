package com.gameofthree.playerservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameMoveEventDto implements Serializable {
    private int number;
    private String playerId;
}
