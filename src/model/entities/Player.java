package model.entities;

import model.services.PlayerSocketService;

public class Player implements  Cloneable{
    private String name;
    private Token token;
    private PlayerSocketService playerSocketService;
    private boolean isHost;
    private boolean isReady;
    private boolean winner;

    public Player(String name, PlayerSocketService playerSocketService) {
        this.name = name;
        this.playerSocketService = playerSocketService;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public PlayerSocketService getPlayerSocketService() {
        return playerSocketService;
    }

    public boolean getIsHost() {
        return isHost;
    }

    public void setIsHost(boolean isHost) {
        this.isHost = isHost;
    }

    public boolean isReady() {
        return isReady;
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }

    public boolean getIsWinner() {
        return winner;
    }

    public void setWinner(boolean winner) {
        this.winner = winner;
    }

    @Override
    public String toString() {
        return name;
    }


    @Override
    public Player clone() {
        try {
            Player clone = (Player) super.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
