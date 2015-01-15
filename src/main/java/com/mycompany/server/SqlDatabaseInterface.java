package com.mycompany.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;

public class SqlDatabaseInterface implements DatabaseInterface
{
    @Override
    public void writeGame(GameInfo info) throws IOException
    {
        String str1 = encodeInBase64(info.player1Field);
        String str2 = encodeInBase64(info.player2Field);
        
        handler.write(GAME_SQL_WRITE_STRING, info.id, info.playerNames[0],
                info.playerNames[1], str1, str2);
    }
    
    @Override
    public GameInfo readGame(int id) throws IOException
    {
        ResultSet data = handler.read(GAME_SQL_READ_STRING + GAME_SQL_READ_BY_ID_EXTENSION, id);
        GameInfo result = new GameInfo();
        
        result.id = id;
        try {
            data.next();
            result = getInfoFromRow(data);
        } catch (SQLException e) {}
        return result;
    }
    
    @Override
    public GameInfo[] readAll() throws IOException
    {
        ResultSet data = handler.read(GAME_SQL_READ_STRING);
        List<GameInfo> result = new ArrayList<>();
        try {
            data.last();
            data.beforeFirst();
            while(data.next()) {
                result.add(getInfoFromRow(data));
            }
        } catch (SQLException e) { throw new IOException("Can't get games from database"); }
        return result.toArray( new GameInfo[]{});
    }
    
    @Override
    public void deleteGame(int id)
    {
        handler.delete(GAME_SQL_DELETE_STRING + GAME_SQL_DELETE_BY_ID_EXTENSION, id);
    }

    @Override
    public void deleteAll()
    {
        handler.delete(GAME_SQL_DELETE_STRING);
    }
    
    private GameInfo getInfoFromRow(ResultSet row) throws IOException
    {
        GameInfo result = new GameInfo();
        
        try {
        result.playerNames[0] = row.getString("firstPlayerName");
        result.playerNames[1] = row.getString("secondPlayerName");
        result.player1Field = decodeFromBase64(row.getString("firstPlayerField"), new int[][]{});
        result.player2Field = decodeFromBase64(row.getString("secondPlayerField"), new int[][]{});
        } catch(SQLException e){}
        return result;
    }
    
    private String encodeInBase64(Object object) throws IOException
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        new ObjectOutputStream(out).writeObject(object);
        return new String(Base64.encodeBase64(out.toByteArray()));
    }
    
    @SuppressWarnings("unchecked")
    private <T> T decodeFromBase64(String str, T a) throws IOException
    {
        ByteArrayInputStream in = new ByteArrayInputStream(Base64.decodeBase64(str));
        try {
            return (T) new ObjectInputStream(in).readObject();
        } catch (ClassNotFoundException e) { 
            throw new IOException("Cannot decode since no resulting class found"); 
        }
    }
    
    private static final String GAME_SQL_WRITE_STRING  = "INSERT INTO games (id, firstPlayerName,"
            + " secondPlayerName, firstPlayerField, secondPlayerField)"
            + " VALUES (?, ?, ?, ?, ?)";
    private static final String GAME_SQL_READ_STRING = "SELECT * FROM games ";
    private static final String GAME_SQL_READ_BY_ID_EXTENSION = "WHERE id = ?";
    private static final String GAME_SQL_DELETE_STRING = "DELETE FROM games ";
    private static final String GAME_SQL_DELETE_BY_ID_EXTENSION = "WHERE id = ?";
    private MySQLDatabaseHandler handler = new MySQLDatabaseHandler();
}
