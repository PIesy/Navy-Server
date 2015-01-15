package com.mycompany.server;

import java.io.IOException;
import java.util.Arrays;

import com.mycompany.data.exceptions.GameOverException;
import com.mycompany.data.exceptions.ShipIsKilledException;
import com.mycompany.data.game.Bot;
import com.mycompany.data.game.GameRules;
import com.mycompany.data.game.Grid;
import com.mycompany.data.game.GridItemDescriptor;

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
    
    private int[][] getIntDescriptor(Grid field)
    {
        GridItemDescriptor[][] descriptors = field.getState();
        int[][] result = new int[field.getSizeVertical()][field.getSizeHorizontal()];

        for (int i = 0; i < field.getSizeVertical(); i++) {
            for (int j = 0; j < field.getSizeHorizontal(); j++) {
                result[i][j] = descriptors[i][j].toInt();
            }
        }
        return result;
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
