package com.mycompany.server;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mycompany.server.controllers.JsonRequestController;
import com.mycompany.server.controllers.RequestController;
import com.mycompany.server.exceptions.NotFoundException;
 
@WebServlet("/Game")
public class GameServlet extends HttpServlet
{
    @Override
    public void init() throws ServletException
    {
        controllers.put("application/json", new JsonRequestController());
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        HttpSession session = request.getSession(true);
        Game game;
        
        try {
            if(session.getAttribute("game") == null)
                throw new NotFoundException();
            game = manager.findGame((int)session.getAttribute("game"));
        } catch (NotFoundException e) {
            game = manager.newGame();
            session.setAttribute("game", game.getId());
        }
        try{
            controllers.get(request.getHeader("Accept")).getInfo(response, game.getId());
        } catch(Exception e) {
            setResponseError(response);
        }
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String contentType = request.getContentType().split(";")[0];
        try{
            controllers.get(contentType).parseRequest(response, request);
        } catch(Exception e) {
            setResponseError(response);
        }
    }
    
    private void setResponseError(HttpServletResponse response)
    {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
    
    private HashMap<String, RequestController> controllers = new HashMap<>();
    private final GameManager manager = GameManager.INSTANCE;
    private static final long serialVersionUID = 6191308373440549493L;
}