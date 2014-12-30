package com.mycompany.server;

import javax.json.Json;
import javax.json.JsonObject;

import com.mycompany.server.game.Bot;
import com.mycompany.server.game.Directions;
import com.mycompany.server.game.Grid;
import com.mycompany.server.game.Player;
import com.mycompany.server.game.GameRules;
import com.mycompany.server.game.exceptions.AllShipsSetException;
import com.mycompany.server.game.exceptions.AlreadyHitException;
import com.mycompany.server.game.exceptions.GameOverException;
import com.mycompany.server.game.exceptions.ShipIsKilledException;
import com.mycompany.server.game.ships.Ship;

public class Game {

    public Game()
    {
        this(-1, new GameRules(-1));
    }
    
    public Game(int id, GameRules rules)
    {
        this.id = id;
        this.gameRules = rules;
        fields[0] = new Grid(gameRules.fieldDimensions[0], gameRules.fieldDimensions[1]);
        fields[1] = new Grid(gameRules.fieldDimensions[0], gameRules.fieldDimensions[1]);
        bot = new Bot(fields[1], gameRules);
        player = new Player(fields[0], gameRules);
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
    
    public JsonObject fillGameData()
    {
        JsonObject obj = Json.createObjectBuilder()
                .add("size_x", gameRules.fieldDimensions[0])
                .add("size_y", gameRules.fieldDimensions[1])
                .add("boatsCount", gameRules.boatsCount)
                .add("schoonersCount", gameRules.schoonersCount)
                .add("destroyersCount", gameRules.destroyersCount)
                .add("carriersCount", gameRules.carriersCount)
                .add("gameId", id)
                .build();
        return obj;
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
        if(switchTurn){
            return botHit();
        }
        return GameResponseFactory.makeSuccessWithBothFieldsResponse(fields[0], fields[1]);
    }
    
    public GameResponse setShip(int[] coordinates, Directions direction)
    {  
        Ship ship;
        try {
            ship = player.setShip();
            fields[0].setShip(ship, coordinates[0], coordinates[1], direction);
        } catch (AllShipsSetException e) {
        } catch (IndexOutOfBoundsException e) {
            player.unsetShip();
            return GameResponseFactory.makeErrorResponse(e.getMessage());
        }
        return GameResponseFactory.makeSuccessWithFieldResponse(fields[0]);
    }
      
    private void initBot()
    {
        bot.setShips();
    }
    
    private GameResponse botHit()
    {
        boolean end = false;
        
        while(!end){
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
    private final Player player;
    private final Bot bot;
    private final GameRules gameRules;
    private final int id;
}
