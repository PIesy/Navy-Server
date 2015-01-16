package com.mycompany.server;

import java.io.IOException;

import com.mycompany.data.game.GameRules;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class GameLoaderTest extends TestCase
{

    public GameLoaderTest( String testName )
    {
        super( testName );
    }

    public static Test suite()
    {
        return new TestSuite( GameLoaderTest.class );
    }

    public void testBot()
    {
        GameInfo infoSource;
        infoSource = game.getInfo();
        try {
            handler.writeGame(infoSource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        loader.loadGame(-1);
        handler.deleteGame(-1);
        System.out.println(handler.getMaxID());
        assertTrue( true );
    }
        
    protected void setUp()
    {
        rules = new GameRules();
        game = new WebGame(-1, rules);
    }
    
    private GameLoader loader = new GameLoader();
    private SqlDatabaseInterface handler = new SqlDatabaseInterface();
    private GameRules rules;
    private WebGame game;
}
