package com.mycompany.server.actions.json;

import javax.json.JsonObject;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.server.Game;
import com.mycompany.server.GameResponse;
import com.mycompany.server.JsonBuilder;
import com.mycompany.server.JsonInputHandler;


public class JsonRequestHit implements JsonRequestAction 
{

    public JsonRequestHit() 
    {
        mapper.setSerializationInclusion(Include.NON_EMPTY);
    }
    
    @Override
    public JsonObject execute(JsonObject data, Game game)
    {
        int[] coordinates = jsonHandler.getCoordinates(data);
        GameResponse response = game.hit(coordinates);
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
