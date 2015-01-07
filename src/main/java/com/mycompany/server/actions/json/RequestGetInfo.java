package com.mycompany.server.actions.json;

import com.mycompany.data.ContentMapper;
import com.mycompany.data.game.GameRequest;
import com.mycompany.data.game.GameRules;
import com.mycompany.server.WebGame;

public class RequestGetInfo implements RequestAction
{
    @Override
    public String execute(GameRequest data, WebGame game, ContentMapper mapper) throws Exception
    {
        return mapper.serialize(game.getRules(), GameRules.class);
    }

}
