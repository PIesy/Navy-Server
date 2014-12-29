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
        this(-1, new GameRules());
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
    
    public JsonObject parseRequest(JsonObject request)
    {
        switch(request.getString("type")){
        case "setName": 
            return setPlayerName(request);
        case "hit":
            return hit(request);
        case "setShip":
            return setShip(request);
        }
        return Json.createObjectBuilder().add("state", "Wup^_^Wup").build();
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
                .add("boatsCount", gameRules.boatscount)
                .add("schoonersCount", gameRules.schoonersCount)
                .add("destroyersCount", gameRules.destroyersCount)
                .add("carriersCount", gameRules.carriersCount)
                .add("gameId", id)
                .build();
        return obj;
    }
    
    private JsonObject setPlayerName(JsonObject data)
    {
        JsonObject response = Json.createObjectBuilder()
                .add("state", "success").build();
        player.setName(data.getString("name"));
        return response;
        
    }
    
    private JsonObject hit(JsonObject data)
    {
        int[] coordinates = inputHandler.getCoordinates(data);
        boolean switchTurn = false;
        
        try {
            switchTurn = !fields[1].hit(coordinates[0], coordinates[1]);
        } catch (AlreadyHitException e) {
            return responseBuilder.errorResponse("Already hit this node");
        } catch (ShipIsKilledException e) {
            try {
                bot.destroyShip();
            } catch (GameOverException e1) {
                return responseBuilder.victoryResponse();
            }
        }
        if(switchTurn){
            return botHit();
        }
        return responseBuilder.successWithBothFieldsResponse(fields[0].getState(), fields[1].getState(), gameRules.fieldDimensions);
    }
    
    private JsonObject setShip(JsonObject data)
    {
        int[] coordinates = inputHandler.getCoordinates(data);
        Directions direction = inputHandler.getDirection(data);
        
        Ship ship;
        try {
            ship = player.setShip();
            fields[0].setShip(ship, coordinates[0], coordinates[1], direction);
        } catch (AllShipsSetException e) {
        } catch (IndexOutOfBoundsException e) {
            player.unsetShip();
            return responseBuilder.errorWithFieldResponse(e.getMessage(), fields[0].getState(), gameRules.fieldDimensions);
        }
        return responseBuilder.successWithFieldResponse(fields[0].getState(), gameRules.fieldDimensions);
    }
      
    private void initBot()
    {
        bot.setShips();
    }
    
    private JsonObject botHit()
    {
        boolean end = false;
        
        while(!end){
            try {
                end = !bot.hit(fields[0]); 
            } catch (ShipIsKilledException e) {
                try {
                    player.destroyShip();
                } catch (GameOverException e1) {
                    return responseBuilder.defeatResponse();
                }
            }
        }
        return responseBuilder.successWithBothFieldsResponse(fields[0].getState(), fields[1].getState(), gameRules.fieldDimensions);
    }
    
    private final JsonResponseBuilder responseBuilder = new JsonResponseBuilder();
    private final Grid[] fields = new Grid[2];
    private final Player player;
    private final Bot bot;
    private final GameRules gameRules;
    private final InputHander<JsonObject> inputHandler = new JsonInputHandler(); 
    private final int id;
}
