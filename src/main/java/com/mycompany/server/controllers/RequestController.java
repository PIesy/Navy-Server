package com.mycompany.server.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mycompany.data.ContentMapper;
import com.mycompany.data.ContentMapperFactory;
import com.mycompany.data.ContentMapperFactory.MapperType;
import com.mycompany.data.GameResponseFactory;
import com.mycompany.data.game.GameRequest;
import com.mycompany.data.game.GameRequest.GameRequestType;
import com.mycompany.data.game.GameResponse;
import com.mycompany.server.GameManager;
import com.mycompany.server.WebGame;
import com.mycompany.server.actions.json.RequestAction;
import com.mycompany.server.actions.json.RequestGetInfo;
import com.mycompany.server.actions.json.RequestHit;
import com.mycompany.server.actions.json.RequestSetName;
import com.mycompany.server.actions.json.RequestSetShip;
import com.mycompany.server.exceptions.NotFoundException;

public class RequestController
{

    public RequestController()
    {
        actions.put(GameRequestType.SetShip, new RequestSetShip());
        actions.put(GameRequestType.SetName, new RequestSetName());
        actions.put(GameRequestType.Hit, new RequestHit());
        actions.put(GameRequestType.GetInfo, new RequestGetInfo());
    }

    public void getInfo(HttpServletResponse response, int gameId, String contentType) throws Exception
    {
        WebGame game;
        String data;
        ContentMapper mapper = ContentMapperFactory.getMapper(MapperType.parseString(contentType));
        
        try {
            game = GameManager.INSTANCE.findGame(gameId);
            data = actions.get(GameRequestType.GetInfo).execute(null, game, mapper);
            saveGame(game.getId());
        } catch (Exception e) {
            data = mapper.serialize(GameResponseFactory.makeErrorResponse(e.getMessage()), GameResponse.class);
        }
        writeResponse(response, data, contentType);
    }

    public void parseRequest(HttpServletResponse response, HttpServletRequest request, String contentType) throws Exception
    {
        String data = getDataFromRequest(request);
        ContentMapper mapper = ContentMapperFactory.getMapper(MapperType.parseString(contentType));
        GameRequest req = mapper.deserialize(data, GameRequest.class);
        
        try {
            WebGame game = getGameIfExists(request, req);
            data = actions.get(req.getType()).execute(req, game, mapper);
            saveGame(game.getId());
        } catch (Exception e) {
            data = mapper.serialize(GameResponseFactory.makeErrorResponse(e.getMessage()), GameResponse.class);
        }
        writeResponse(response, data, contentType);
    }

    private WebGame getGameIfExists(HttpServletRequest request, GameRequest data) throws NotFoundException
    {
        WebGame game;
        try {
            game = GameManager.INSTANCE.findGame(data.getGameId());
            request.getSession(true).setAttribute("game", data.getGameId());
        } catch (NullPointerException e) {
            e.printStackTrace();
            throw new NotFoundException("No game id specified");
        }
        return game;
    }

    private void writeResponse(HttpServletResponse response, String data, String contentType)
    {
        setResponseProperties(response, contentType);
        try {
            response.getWriter().print(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void setResponseProperties(HttpServletResponse response, String contentType)
    {
        response.setContentType(contentType);
        response.setCharacterEncoding("UTF8");
        response.setStatus(HttpServletResponse.SC_OK);
    }
    
    public String getDataFromRequest(HttpServletRequest request)
    {
        StringBuffer jb = new StringBuffer();
        String line = null;
        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null) {
                jb.append(line);
            }
        } catch (Exception e) {
        }

        return jb.toString();
    }
    
    private void saveGame(int gameId)
    {
        GameManager.INSTANCE.saveGame(gameId);
    }

    private HashMap<GameRequestType, RequestAction> actions = new HashMap<>();
}
