package com.mycompany.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.json.*;
 
@WebServlet("/Admin")
public class AdminServlet extends HttpServlet
{
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("<h1>Hello Servlet</h1>");
        response.getWriter().println("session=" + request.getSession(true).getId());
        response.getWriter().println("Games count " + manager.getGamesCount());
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
    private static final long serialVersionUID = -2652099533020368688L;
}