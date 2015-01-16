package com.mycompany.server.database;

import java.io.IOException;

import com.mycompany.server.GameInfo;

public interface DatabaseInterface
{
    void updateGame(GameInfo info) throws IOException;
    void writeGame(GameInfo info) throws IOException;
    void deleteGame(int id);
    void deleteAll();
    GameInfo readGame(int id) throws IOException;
    GameInfo[] readAll() throws IOException;
    int getMaxID();
}
