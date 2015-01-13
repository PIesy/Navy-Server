package com.mycompany.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.apache.commons.codec.binary.Base64;

public class SqlDatabaseInterface
{
    public void writeGame(GameInfo info) throws IOException
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        new ObjectOutputStream(out).writeObject(info.player1Field);
        String str1 = new String(Base64.encodeBase64(out.toByteArray()));
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        new ObjectOutputStream(out1).writeObject(info.player2Field);
        String str2 = new String(Base64.encodeBase64(out1.toByteArray()));
        
        handler.write(gameSqlString, info.id, info.playerNames[0], info.shipsCount[0],
                info.playerNames[1], info.shipsCount[1], str1, str2);
    }
    
    private static final String gameSqlString  = "INSERT INTO games (id, firstPlayerName, firstPlayerShipsCount,"
            + " secondPlayerName, secondPlayershipsCount, firstPlayerField, secondPlayerField)"
            + " VALUES (?, ?, ?, ?, ?, ?, ?)";
    private DatabaseHandler handler = new DatabaseHandler();
}
