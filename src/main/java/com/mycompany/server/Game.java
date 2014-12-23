package com.mycompany.server;

import javax.json.JsonObject;

import com.mycompany.server.game.GameRules;

public class Game {

    public Game()
    {
        id = -1;
        gameRules = null;
    }
    
    public Game(int id, GameRules rules)
    {
        this.id = id;
        this.gameRules = rules;
    }
    
    public int getId()
    {
        return id;
    }
    
    public int[] handleInput(JsonObject object)
    {
        return inputHandler.getCoordinates(object);
    }
    
    public GameRules getRules()
    {
        return gameRules;
    }
    
    private final GameRules gameRules;
    private final InputHander<JsonObject> inputHandler = new JsonInputHandler(); 
    private final int id;
}
