package com.mycompany.server.controllers;

import java.io.IOException;
import java.util.HashMap;

import javax.json.Json;
import javax.json.JsonObject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mycompany.server.Game;
import com.mycompany.server.GameManager;
import com.mycompany.server.JsonBuilder;
import com.mycompany.server.actions.json.JsonRequestAction;
import com.mycompany.server.actions.json.JsonRequestGetInfo;
import com.mycompany.server.actions.json.JsonRequestHit;
import com.mycompany.server.actions.json.JsonRequestSetName;
import com.mycompany.server.actions.json.JsonRequestSetShip;
import com.mycompany.server.exceptions.NotFoundException;

public class JsonRequestController implements RequestController {

    public JsonRequestController()
    {
        actions.put("setShip", new JsonRequestSetShip());
        actions.put("setName", new JsonRequestSetName());
        actions.put("hit", new JsonRequestHit());
        actions.put("getInfo", new JsonRequestGetInfo());
    }
    
    @Override
    public void getInfo(HttpServletResponse response, int gameId) 
    {
        Game game;
        JsonObject data;
        try {
            game = GameManager.INSTANCE.findGame(gameId);
            data = actions.get("getInfo").execute(null, game);
        } catch(Exception e) {
            data = Json.createObjectBuilder().add("state", "error").add("error", e.getMessage()).build();
        }
        writeResponse(response, data);
    }
    
    public void parseRequest(HttpServletResponse response, HttpServletRequest request)
    {
        JsonObject data = (new JsonBuilder()).getJsonObject(request);
        try {
            Game game = getGameIfExists(request, data);
            data = actions.get(data.getString("type")).execute(data, game);
        } catch (Exception e) {
            data = Json.createObjectBuilder().add("state", "error").add("error", e.getMessage()).build();
        }
        writeResponse(response, data);
    }
    
    private Game getGameIfExists(HttpServletRequest request, JsonObject data) throws NotFoundException
    {
        Game game;
        try {
            game = GameManager.INSTANCE.findGame(data.getInt("gameId"));
            request.getSession(true).setAttribute("game", data.getInt("gameId"));
        } catch (NullPointerException e) {
            throw new NotFoundException("No game id specified");
        }
        return game;
    }
    
    private void writeResponse(HttpServletResponse response, JsonObject data)
    {
        setResponseProperties(response);
        try {
            response.getWriter().print(data.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void setResponseProperties(HttpServletResponse response)
    {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF8");
        response.setStatus(HttpServletResponse.SC_OK);
    }
    
    private HashMap<String, JsonRequestAction> actions = new HashMap<>();
}
