package com.mycompany.server.actions.json;

import com.mycompany.data.ContentMapper;
import com.mycompany.data.game.GameRequest;
import com.mycompany.data.game.GameResponse;
import com.mycompany.server.WebGame;

public class RequestSetShip implements RequestAction
{
    @Override
    public String execute(GameRequest data, WebGame game, ContentMapper mapper) throws Exception
    {
        GameResponse response = game.setShip(data.getCoordinates(), data.getDirection());
        return mapper.serialize(response, GameResponse.class);
    }
}
