package com.mycompany.server;

import com.mycompany.data.GameResponseFactory;
import com.mycompany.data.game.Game;
import com.mycompany.data.game.GameResponse;
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
    
    public GameInfo getInfo()
    {
        GameResponse response = GameResponseFactory.makeInfoResponse(new String[]{player.getName(), bot.getName()}, fields);
        GameInfo result = new GameInfo();
        result.player1Field = response.getPlayerField();
        result.player2Field = response.getBotField();
        result.playerNames[0] = response.getPlayer1Name();
        result.playerNames[1] = response.getPlayer2Name();
        result.id = id;
        return result;
    }

    private final GameRules gameRules;
    private final int id;
}
