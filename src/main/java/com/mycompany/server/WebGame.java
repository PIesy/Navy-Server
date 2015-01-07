package com.mycompany.server;

import com.mycompany.data.game.Game;
import com.mycompany.data.game.GameRules;

public class WebGame extends Game
{

    public WebGame()
    {
        this(-1, new GameRules());
    }

    public WebGame(int id, GameRules rules)
    {
        super(rules);
        this.id = id;
        this.gameRules = rules;
    }

    public int getId()
    {
        return id;
    }

    public GameRules getRules()
    {
        return gameRules;
    }

    private final GameRules gameRules;
    private final int id;
}
