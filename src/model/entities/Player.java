package model.entities;

import model.services.PlayerSocketService;

public class Player {
    private String name;
    private Token token;
    private PlayerSocketService playerSocketService;
    private boolean isHost;
    private boolean isReady;

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

    @Override
    public String toString() {
        return name;
    }


}
