package com.mycompany.server;

import javax.json.JsonObject;

public class Game {

    public Game()
    {
        id = -1;
    }
    
    public Game(int id)
    {
        this.id = id;
    }
    
    public int getId()
    {
        return id;
    }
    
    public int[] handleInput(JsonObject object)
    {
        return inputHandler.getCoordinates(object);
    }
    
    private final InputHander<JsonObject> inputHandler = new JsonInputHandler(); 
    private final int id;
}
