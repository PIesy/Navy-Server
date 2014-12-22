package com.mycompany.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.json.*;
 
@WebServlet("/Game")
public class GameServlet extends HttpServlet
{

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        HttpSession session = request.getSession(true);
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        if(session.getAttribute("game") == null){
            session.setAttribute("game", manager.getGame().getId());
        }
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        JsonObject object = (new JsonBuilder()).getJsonObject(request);
        response.setCharacterEncoding("UTF8");
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(object.toString());
    }
    
    private final GameManager manager = GameManager.INSTANCE;
    private static final long serialVersionUID = 6191308373440549493L;
}