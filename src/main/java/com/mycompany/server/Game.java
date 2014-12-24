package com.mycompany.server;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import com.mycompany.server.game.Directions;
import com.mycompany.server.game.Grid;
import com.mycompany.server.game.GridItemDescriptor;
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
        bot = new Player(fields[1], gameRules);
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
        JsonObject response;
        int[] coordinates = inputHandler.getCoordinates(data);
        JsonObjectBuilder builder = Json.createObjectBuilder();
        
        try {
            fields[1].hit(coordinates[0], coordinates[1]);
            builder.add("state", "success");
            addField(builder);
            response = builder.build();
        } catch (AlreadyHitException e) {
            response = Json.createObjectBuilder().add("error", "already hit this node").build();
        } catch (ShipIsKilledException e) {
            try {
                bot.destroyShip();
                builder.add("state", "success");
                addField(builder);
                response = builder.build();
            } catch (GameOverException e1) {
                response = Json.createObjectBuilder().add("gameEnd", "YOU ARE WICTORIOUS!").build();
            }
        }
        return response;
    }
    
    private JsonObject setShip(JsonObject data)
    {
        JsonObject response;
        int[] coordinates = inputHandler.getCoordinates(data);
        Directions direction = inputHandler.getDirection(data);
        JsonObjectBuilder builder = Json.createObjectBuilder();
        
        Ship ship;
        try {
            ship = player.setShip();
            fields[0].setShip(ship, coordinates[0], coordinates[1], direction);
            builder.add("state", "success");
            addField(builder);
            response = builder.build();
        } catch (AllShipsSetException e) {
            response = Json.createObjectBuilder().add("state", "allSet").build();
        } catch (IndexOutOfBoundsException e) {
            response = Json.createObjectBuilder().add("error", e.toString()).build();
        }
        return response;
    }
    
    private void initBot()
    {
        Ship ship;
        
        try {
            ship = bot.setShip();
            fields[1].setShip(ship, 0, 0, Directions.East);
            ship = bot.setShip();
            fields[1].setShip(ship, 6, 0, Directions.East);
            ship = bot.setShip();
            fields[1].setShip(ship, 0, 2, Directions.East);
            ship = bot.setShip();
            fields[1].setShip(ship, 6, 2, Directions.East);
            ship = bot.setShip();
            fields[1].setShip(ship, 0, 4, Directions.East);
            ship = bot.setShip();
            fields[1].setShip(ship, 6, 4, Directions.East);
            ship = bot.setShip();
            fields[1].setShip(ship, 0, 6, Directions.East);
            ship = bot.setShip();
            fields[1].setShip(ship, 6, 6, Directions.East);
            ship = bot.setShip();
            fields[1].setShip(ship, 0, 8, Directions.East);
            ship = bot.setShip();
            fields[1].setShip(ship, 8, 8, Directions.East);
        } catch (AllShipsSetException e){}
    }
    
    private void addField(JsonObjectBuilder builder)
    {
        JsonArrayBuilder rowBuilder = Json.createArrayBuilder();
        GridItemDescriptor[][] field = fields[0].getState();
        
        for(int i = 0; i < gameRules.fieldDimensions[1]; i++) {
            JsonArrayBuilder columnBuilder = Json.createArrayBuilder();
            for (int j = 0; j < gameRules.fieldDimensions[0]; j++) {
                columnBuilder.add(field[i][j].toInt());
            }
            rowBuilder.add(columnBuilder);
        }
        builder.add("field", rowBuilder.build());
    }
     
    private final Grid[] fields = new Grid[2];
    private final Player player;
    private final Player bot;
    private final GameRules gameRules;
    private final InputHander<JsonObject> inputHandler = new JsonInputHandler(); 
    private final int id;
}
