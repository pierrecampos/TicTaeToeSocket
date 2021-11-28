package model.entities;

import model.constants.GameConstants;
import model.constants.Token;
import model.services.PlayerSocketService;

public class Player {
    private String name;
    private Token token;
    private PlayerSocketService playerSocketService;
    private boolean isHost;
    private boolean isReady;
    private GameConstants result;

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

    public boolean getIsReady() {
        return isReady;
    }

    public void setIsReady(boolean isReady) {
        this.isReady = isReady;
    }

    public GameConstants getResult() {
        return result;
    }

    public void setResult(GameConstants result) {
       this.result = result;
    }



    @Override
    public String toString() {
        return name;
    }

}
