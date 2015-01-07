package com.mycompany.server;

import javax.json.JsonObject;

import com.mycompany.data.game.Directions;

public class JsonInputHandler implements InputHander<JsonObject>
{

    @Override
    public int[] getCoordinates(JsonObject source)
    {
        int[] coordinates = new int[2];

        coordinates[0] = source.getJsonArray("coordinates").getInt(0);
        coordinates[1] = source.getJsonArray("coordinates").getInt(1);
        return coordinates;
    }

    @Override
    public Directions getDirection(JsonObject source)
    {
        return Directions.parseString(source.getString("direction"));
    }

}
