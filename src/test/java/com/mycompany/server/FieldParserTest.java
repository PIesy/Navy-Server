package com.mycompany.server;

import com.mycompany.data.exceptions.ShipIsKilledException;
import com.mycompany.data.game.Bot;
import com.mycompany.data.game.GameRules;
import com.mycompany.data.game.Grid;
import com.mycompany.data.game.GridItemDescriptor;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class FieldParserTest extends TestCase
{

    public FieldParserTest( String testName )
    {
        super( testName );
    }

    public static Test suite()
    {
        return new TestSuite( FieldParserTest.class );
    }

    public void testFieldParser()
    {
        parser.parseField(getIntegerFieldDescriptor());
        assertTrue(parser.getShipData().size() == (rules.getBoatsCount() + rules.getCarriersCount() + rules.getDestroyersCount() + rules.getSchoonersCount()));
    }
    
    private int[][] getIntegerFieldDescriptor()
    {
        GridItemDescriptor[][] descriptors = grid.getState();
        int[][] result = new int[grid.getSizeVertical()][grid.getSizeHorizontal()];

        for (int i = 0; i < grid.getSizeVertical(); i++) {
            for (int j = 0; j < grid.getSizeHorizontal(); j++) {
                result[i][j] = descriptors[i][j].toInt();
            }
        }
        return result;
    }
        
    protected void setUp()
    {
        rules = new GameRules();
        grid = new Grid(rules.getFieldDimensions()[0], rules.getFieldDimensions()[1]);
        parser = new FieldParser(rules.getFieldDimensions());
        bot = new Bot(rules);
        bot.setShips(grid);
        for(int i = 0; i < 20; i++) {
            try {
                bot.hit(grid);
            } catch (ShipIsKilledException e) {}
        }
    }
    
    private GameRules rules;
    private Grid grid;
    private Bot bot;
    private FieldParser parser;
}
