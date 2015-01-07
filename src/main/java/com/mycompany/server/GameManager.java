package com.mycompany.server;

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

    public WebGame findGame(int id) throws NotFoundException
    {
        WebGame result = games.get(id);

        if (result == null)
            throw new NotFoundException("Requested game doesn't exist");
        return result;
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

    private int generatedId = 0;
    private ConcurrentHashMap<Integer, WebGame> games = new ConcurrentHashMap<>();
}
