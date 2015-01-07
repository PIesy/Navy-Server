package com.mycompany.server.actions.json;

import com.mycompany.data.ContentMapper;
import com.mycompany.data.game.GameRequest;
import com.mycompany.server.WebGame;

public interface RequestAction
{
    String execute(GameRequest data, WebGame game, ContentMapper mapper) throws Exception;
}
