package com.mycompany.server.database.nosql;

import java.io.IOException;

import com.mycompany.server.GameInfo;
import com.mycompany.server.database.DatabaseInterface;

public class MongoDbDatabaseInterface implements DatabaseInterface
{

    @Override
    public void updateGame(GameInfo info)
    {
        handler.update(info, GameInfo.class, info.id);
    }

    @Override
    public void writeGame(GameInfo info) throws IOException
    {
        handler.write(info, GameInfo.class);
    }

    @Override
    public void deleteGame(int id)
    {
        handler.delete(id);
    }

    @Override
    public void deleteAll()
    {
        handler.deleteAll();
    }

    @Override
    public GameInfo readGame(int id)
    {
        return handler.read(GameInfo.class, id);
    }

    @Override
    public GameInfo[] readAll()
    {
        return handler.readAll(GameInfo.class);
    }

    @Override
    public int getMaxID()
    {
        return handler.getMaxId();
    }

    private final MongoDbDatabaseHandler handler = new MongoDbDatabaseHandler();
}
