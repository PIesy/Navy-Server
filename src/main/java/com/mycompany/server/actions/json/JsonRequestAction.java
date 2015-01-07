package com.mycompany.server.actions.json;

import javax.json.JsonObject;
import com.mycompany.server.Game;

public interface JsonRequestAction
{

    JsonObject execute(JsonObject data, Game game);
}
