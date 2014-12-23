package com.mycompany.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.json.*;

import com.mycompany.server.exceptions.NotFoundException;
import com.mycompany.server.game.GameRules;
 
@WebServlet("/Game")
public class GameServlet extends HttpServlet
{

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        JsonObject obj;
        HttpSession session = request.getSession(true);
        int gameId;
        
        setResponseProperties(response);
        if(session.getAttribute("game") == null){
            session.setAttribute("game", manager.getGame().getId());
        }
        gameId = (int)session.getAttribute("game");
        try {
            obj = fillGameData(gameId, manager.findGame(gameId).getRules());
        } catch (NotFoundException e) {
            obj = Json.createObjectBuilder().build();
        }
        response.getWriter().print(obj.toString());
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        JsonObject object = (new JsonBuilder()).getJsonObject(request);
        JsonObject responceObject;
        int[] coordinates;
        Game game;
        try {
            getGameIfExists(request.getSession(), object);
            game = manager.findGame(object.getInt("gameId"));
            coordinates = game.handleInput(object);
            responceObject = Json.createObjectBuilder()
                    .add("x", coordinates[0])
                    .add("y", coordinates[1])
                    .build();
        } catch (NotFoundException e) {
            responceObject = Json.createObjectBuilder().add("error", e.toString()).build();
        }
        setResponseProperties(response);
        response.getWriter().println(responceObject.toString());
    }
    
    private void getGameIfExists(HttpSession session, JsonObject object) throws NotFoundException
    {
        Game game;
        if(object.containsKey("gameId"))
        {
            game = manager.findGame(object.getInt("gameId"));
            session.setAttribute("game", game.getId());
        }
        else {
            throw new NotFoundException("No game id provided");
        }
    }
    
    private void setResponseProperties(HttpServletResponse response)
    {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF8");
        response.setStatus(HttpServletResponse.SC_OK);
    }
    
    private JsonObject fillGameData(int gameId, GameRules properties)
    {
        JsonObject obj = Json.createObjectBuilder().add("gameId", gameId)
                .add("size_x", properties.fieldDimensions[0])
                .add("size_y", properties.fieldDimensions[1])
                .add("boatsCount", properties.boatscount)
                .add("schoonersCount", properties.schoonersCount)
                .add("destroyersCount", properties.destroyersCount)
                .add("carriersCount", properties.carriersCount)
                .add("gameId", gameId)
                .build();
        return obj;
    }
    
    private final GameManager manager = GameManager.INSTANCE;
    private static final long serialVersionUID = 6191308373440549493L;
}