package com.mycompany.server;

import javax.json.JsonObject;

import com.mycompany.server.game.Directions;

public class JsonInputHandler implements InputHander<JsonObject> {

    @Override
    public int[] getCoordinates(JsonObject source) {
        int[] coordinates = new int[2];
        
        coordinates[0] = source.getInt("x");
        coordinates[1] = source.getInt("y");
        return coordinates;
    }

    @Override
    public Directions getDirection(JsonObject source) {
        return Directions.parseString(source.getString("direction"));
    }

}
