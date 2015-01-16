package com.mycompany.server;

import java.io.IOException;
import java.util.Arrays;

import com.mycompany.data.game.GameRules;
import com.mycompany.server.database.sql.SqlDatabaseInterface;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class SqlDatabaseInterfaceTest extends TestCase
{

    public SqlDatabaseInterfaceTest( String testName )
    {
        super( testName );
    }

    public static Test suite()
    {
        return new TestSuite( SqlDatabaseInterfaceTest.class );
    }

    public void testBot() throws IOException
    {
        GameInfo infoResult, infoSource;
        infoSource = game.getInfo();
        handler.writeGame(infoSource);
        infoResult = handler.readGame(-1);
        handler.deleteGame(-1);
        System.out.println(handler.getMaxID());
        assertTrue(Arrays.deepEquals(infoSource.player2Field, infoResult.player2Field));
    }
        
    protected void setUp()
    {
        rules = new GameRules();
        game = new WebGame(-1, rules);
    }
    
    private SqlDatabaseInterface handler = new SqlDatabaseInterface();
    private GameRules rules;
    private WebGame game;
}
