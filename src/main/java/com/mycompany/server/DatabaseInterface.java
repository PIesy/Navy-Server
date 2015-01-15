package com.mycompany.server;

import java.io.IOException;

public interface DatabaseInterface
{
    void writeGame(GameInfo info) throws IOException;
    void deleteGame(int id);
    void deleteAll();
    GameInfo readGame(int id) throws IOException;
    GameInfo[] readAll() throws IOException;
    int getMaxID();
}
