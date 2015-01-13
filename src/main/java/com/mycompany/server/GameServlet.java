package com.mycompany.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mycompany.server.controllers.RequestController;
import com.mycompany.server.exceptions.NotFoundException;

@WebServlet("/Game")
public class GameServlet extends HttpServlet
{

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        HttpSession session = request.getSession(true);
        WebGame game;

        try {
            if (session.getAttribute("game") == null)
                throw new NotFoundException();
            game = manager.findGame((int) session.getAttribute("game"));
        } catch (NotFoundException e) {
            game = manager.newGame();
            session.setAttribute("game", game.getId());
        }
        try {
            controller.getInfo(response, game.getId(), request.getHeader("Accept"));
        } catch (Exception e) {
            setResponseError(response);
        }
        (new SqlDatabaseInterface()).writeGame(game.getInfo());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String contentType = request.getContentType().split(";")[0];
        try {
            controller.parseRequest(response, request, contentType);
        } catch (Exception e) {
            setResponseError(response);
        }
    }

    private void setResponseError(HttpServletResponse response)
    {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    private RequestController controller = new RequestController();
    private final GameManager manager = GameManager.INSTANCE;
    private static final long serialVersionUID = 6191308373440549493L;
}