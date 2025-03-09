package com.gameofthree.playerservice.dto;

import java.io.Serializable;

public class GameMoveEventDto implements Serializable {
    private int numberAdded;
    private int numberResult;
    private String fromPlayerId;
    private String toPlayerId;

    public GameMoveEventDto(int numberAdded, int numberResult, String fromPlayerId, String toPlayerId) {
        this.numberAdded = numberAdded;
        this.numberResult = numberResult;
        this.fromPlayerId = fromPlayerId;
        this.toPlayerId = toPlayerId;
    }

    public GameMoveEventDto() {}

    public int getNumberAdded() {
        return numberAdded;
    }

    public void setNumberAdded(int numberAdded) {
        this.numberAdded = numberAdded;
    }

    public int getNumberResult() {
        return numberResult;
    }

    public void setNumberResult(int numberResult) {
        this.numberResult = numberResult;
    }

    public String getFromPlayerId() {
        return fromPlayerId;
    }

    public void setFromPlayerId(String fromPlayerId) {
        this.fromPlayerId = fromPlayerId;
    }

    public String getToPlayerId() {
        return toPlayerId;
    }

    public void setToPlayerId(String toPlayerId) {
        this.toPlayerId = toPlayerId;
    }
}
