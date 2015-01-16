package com.mycompany.server;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import com.mycompany.data.game.GameRules;
import com.mycompany.server.exceptions.NotFoundException;

public enum GameManager
{
    INSTANCE;

    public static GameManager getInstance()
    {
        return INSTANCE;
    }
    
    private GameManager()
    {
        generatedId = database.getMaxID() + 1;
    }

    public WebGame findGame(int id) throws NotFoundException
    {
        WebGame result = games.get(id);

        if (result == null) 
        {
            result = loader.loadGame(id);
            games.put(id, result);
        }
        if (result == null) {
            throw new NotFoundException("Requested game doesn't exist");
        }
        return result;
    }
    
    public void saveGame(int id)
    {
        WebGame result = games.get(id);
        
        if(result == null) {
            return;
        }
        try {
            database.deleteGame(id);
            database.writeGame(result.getInfo());
        } catch (IOException e) {}
    }

    public synchronized WebGame newGame()
    {
        WebGame result = new WebGame(generatedId, new GameRules(generatedId));

        games.put(generatedId, result);
        generatedId++;
        return result;
    }

    public void endGame(int id)
    {
        games.remove(id);
    }

    public WebGame[] getAllGames()
    {
        return games.values().toArray(new WebGame[0]);
    }

    public int getGamesCount()
    {
        return games.size();
    }
    
    private GameLoader loader = new GameLoader();
    private DatabaseInterface database = new SqlDatabaseInterface();
    private int generatedId = 0;
    private ConcurrentHashMap<Integer, WebGame> games = new ConcurrentHashMap<>();
}
