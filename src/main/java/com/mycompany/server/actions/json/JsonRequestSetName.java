package com.mycompany.server.actions.json;

import javax.json.JsonObject;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.data.game.GameResponse;
import com.mycompany.server.Game;
import com.mycompany.server.JsonBuilder;

public class JsonRequestSetName implements JsonRequestAction
{

    public JsonRequestSetName()
    {
        mapper.setSerializationInclusion(Include.NON_EMPTY);
    }

    @Override
    public JsonObject execute(JsonObject data, Game game)
    {
        String name = data.getString("name");
        GameResponse response = game.setPlayerName(name);
        try {
            return builder.getJsonObject(mapper.writeValueAsString(response));
        } catch (JsonProcessingException e) {
            return builder.getJsonObject("");
        }
    }

    private final JsonBuilder builder = new JsonBuilder();
    private final ObjectMapper mapper = new ObjectMapper();
}
