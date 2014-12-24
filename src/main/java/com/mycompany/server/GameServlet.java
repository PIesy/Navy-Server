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
 
@WebServlet("/Game")
public class GameServlet extends HttpServlet
{

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        JsonObject obj;
        HttpSession session = request.getSession(true);
        Game game;
        
        setResponseProperties(response);
        try {
            if(session.getAttribute("game") == null)
                throw new NotFoundException();
            game = manager.findGame((int)session.getAttribute("game"));
        } catch (NotFoundException e) {
            game = manager.getGame();
            session.setAttribute("game", game.getId());
        }
        obj = game.fillGameData();
  
        response.getWriter().print(obj.toString());
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        JsonObject object = (new JsonBuilder()).getJsonObject(request);
        JsonObject responceObject;
        Game game;
        try {
            game = getGameIfExists(request.getSession(), object);
            responceObject = game.parseRequest(object);
        } catch (NotFoundException e) {
            responceObject = Json.createObjectBuilder().add("error", e.toString()).build();
        }
        setResponseProperties(response);
        response.getWriter().println(responceObject.toString());
    }
    
    private Game getGameIfExists(HttpSession session, JsonObject object) throws NotFoundException
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
        return game;
    }
    
    private void setResponseProperties(HttpServletResponse response)
    {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF8");
        response.setStatus(HttpServletResponse.SC_OK);
    }
     
    private final GameManager manager = GameManager.INSTANCE;
    private static final long serialVersionUID = 6191308373440549493L;
}