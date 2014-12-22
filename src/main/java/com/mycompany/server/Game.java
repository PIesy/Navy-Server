package com.mycompany.server;

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
    
    private final int id;
}
