package com.mycompany.server;

import com.mycompany.data.GameResponseFactory;
import com.mycompany.data.exceptions.AlreadyHitException;
import com.mycompany.data.exceptions.GameOverException;
import com.mycompany.data.exceptions.ShipIsKilledException;
import com.mycompany.data.game.Bot;
import com.mycompany.data.game.Directions;
import com.mycompany.data.game.GameResponse;
import com.mycompany.data.game.GameRules;
import com.mycompany.data.game.Grid;
import com.mycompany.data.game.LocalPlayer;
import com.mycompany.data.game.ships.Ship;

public class Game
{

    public Game()
    {
        this(-1, new GameRules());
    }

    public Game(int id, GameRules rules)
    {
        this.id = id;
        this.gameRules = rules;
        fields[0] = new Grid(gameRules.getFieldDimensions()[0], gameRules.getFieldDimensions()[1]);
        fields[1] = new Grid(gameRules.getFieldDimensions()[0], gameRules.getFieldDimensions()[1]);
        bot = new Bot(gameRules);
        player = new LocalPlayer(gameRules);
        initBot();
    }

    public int getId()
    {
        return id;
    }

    public GameRules getRules()
    {
        return gameRules;
    }

    public GameResponse setPlayerName(String name)
    {
        GameResponse response = GameResponseFactory.makeSuccessResponse();
        player.setName(name);
        return response;
    }

    public GameResponse hit(int[] coordinates)
    {
        boolean switchTurn = false;

        try {
            switchTurn = !fields[1].hit(coordinates[0], coordinates[1]);
        } catch (AlreadyHitException e) {
            return GameResponseFactory.makeErrorResponse(e.getMessage());
        } catch (ShipIsKilledException e) {
            try {
                bot.destroyShip();
            } catch (GameOverException e1) {
                return GameResponseFactory.makeEndGameResponse("YOU ARE VICTORIOUS!");
            }
        }
        if (switchTurn) {
            return botHit();
        }
        return GameResponseFactory.makeSuccessWithBothFieldsResponse(fields[0], fields[1]);
    }

    public GameResponse setShip(int[] coordinates, Directions direction)
    {
        Ship ship;
        try {
            ship = player.getShip();
            fields[0].setShip(ship, coordinates[0], coordinates[1], direction);
        } catch (IndexOutOfBoundsException e) {
            player.unsetShip();
            return GameResponseFactory.makeErrorResponse(e.getMessage());
        }
        return GameResponseFactory.makeSuccessWithFieldResponse(fields[0]);
    }

    private void initBot()
    {
        bot.setShips(fields[1]);
    }

    private GameResponse botHit()
    {
        boolean end = false;

        while (!end) {
            try {
                end = !bot.hit(fields[0]);
            } catch (ShipIsKilledException e) {
                try {
                    player.destroyShip();
                } catch (GameOverException e1) {
                    return GameResponseFactory.makeEndGameResponse("NOT SO EASY!");
                }
            }
        }
        return GameResponseFactory.makeSuccessWithBothFieldsResponse(fields[0], fields[1]);
    }

    private final Grid[] fields = new Grid[2];
    private final LocalPlayer player;
    private final Bot bot;
    private final GameRules gameRules;
    private final int id;
}
