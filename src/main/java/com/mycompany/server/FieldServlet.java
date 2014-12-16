package com.mycompany.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.json.*;
 
@WebServlet("/Field/")
public class FieldServlet extends HttpServlet
{
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("<h1>Hello Servlet</h1>");
        response.getWriter().println("session=" + request.getSession(true).getId());
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        StringBuffer jb = new StringBuffer();
        String line = null;
        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null)
                jb.append(line);
        } catch (Exception e) {}

        response.setCharacterEncoding("UTF8");
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        JsonReader reader = Json.createReader(new StringReader(jb.toString()));
        JsonObject jsonObject = reader.readObject();
        response.getWriter().println(jsonObject.toString());
    }
    
    private static final long serialVersionUID = -2652099533020368688L;
}