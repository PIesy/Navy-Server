package com.mycompany.server.actions.json;

import javax.json.JsonObject;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.server.Game;
import com.mycompany.server.GameResponse;
import com.mycompany.server.JsonBuilder;
import com.mycompany.server.JsonInputHandler;
import com.mycompany.server.game.Directions;

public class JsonRequestSetShip implements JsonRequestAction 
{

    public JsonRequestSetShip()
    {
        mapper.setSerializationInclusion(Include.NON_EMPTY);
    }
    
    @Override
    public JsonObject execute(JsonObject data, Game game)
    {
        int[] coordinates = jsonHandler.getCoordinates(data);
        Directions direction = jsonHandler.getDirection(data);
        GameResponse response = game.setShip(coordinates, direction);
        try {
            return builder.getJsonObject(mapper.writeValueAsString(response));
        } catch (JsonProcessingException e) {
            return builder.getJsonObject("");
        }
    }

    private final JsonBuilder builder = new JsonBuilder();
    private final ObjectMapper mapper = new ObjectMapper();
    private final JsonInputHandler jsonHandler = new JsonInputHandler();
}
