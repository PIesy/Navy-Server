package com.mycompany.server.actions.json;

import javax.json.JsonObject;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.server.Game;
import com.mycompany.server.JsonBuilder;

public class JsonRequestGetInfo implements JsonRequestAction 
{

    public JsonRequestGetInfo() 
    {
        mapper.setSerializationInclusion(Include.NON_EMPTY);
    }
    
    @Override
    public JsonObject execute(JsonObject data, Game game) 
    {
        try {
            return builder.getJsonObject(mapper.writeValueAsString(game.getRules()));
        } catch (JsonProcessingException e) {
            return builder.getJsonObject("");
        }
    }
    
    private final JsonBuilder builder = new JsonBuilder();
    private final ObjectMapper mapper = new ObjectMapper();
}
