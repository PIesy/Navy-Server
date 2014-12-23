package com.mycompany.server;

import java.util.HashMap;

import com.mycompany.server.Game;
import com.mycompany.server.exceptions.NotFoundException;
import com.mycompany.server.game.GameRules;

public enum GameManager {
    INSTANCE;
    
    public static GameManager getInstance()
    {
        return INSTANCE;
    }
    
    public Game findGame(int id) throws NotFoundException
    {
        Game result = games.get(id);
        
        if(result == null)
            throw new NotFoundException("Requested game doesn't exist");
        return result;
    }
    
    public Game getGame()
    {
        Game result = new Game(generatedId, new GameRules());
        
        games.put(generatedId, result);
        generatedId++;
        return result;
    }
    
    public void endGame(int id)
    {
        games.remove(id);
    }
    
    public Game[] getAllGames()
    {
        return games.values().toArray(new Game[0]);
    }
    
    public int getGamesCount()
    {
        return games.size();
    }
    
    private int generatedId = 0;
    private HashMap<Integer, Game> games = new HashMap<>();
}
