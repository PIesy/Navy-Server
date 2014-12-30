package com.mycompany.server.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface RequestController {

    public void getInfo(HttpServletResponse response, int gameId);
    public void parseRequest(HttpServletResponse response, HttpServletRequest request);
}
