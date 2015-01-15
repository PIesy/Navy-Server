package com.mycompany.server;

import java.io.IOException;
import java.util.List;

import com.mycompany.data.exceptions.AlreadyHitException;
import com.mycompany.data.exceptions.GameOverException;
import com.mycompany.data.exceptions.ShipIsKilledException;
import com.mycompany.data.game.Game;
import com.mycompany.data.game.GameRules;
import com.mycompany.data.game.Grid;
import com.mycompany.data.game.LocationData;
import com.mycompany.data.game.ships.ShipBuilder;

public class GameLoader
{
    public WebGame loadGame(int id)
    {
        GameInfo info;
        try {
             info = database.readGame(id);
             return buildGame(id, info);
        } catch (IOException e) {
            return null;
        }
    }
    
    public Game[] loadAllGames()
    {
        return null;
    }
    
    private WebGame buildGame(int id, GameInfo info)
    {
        FieldParser parser = new FieldParser( new int[]{info.player1Field.length, info.player2Field.length});
        WebGame game = new WebGame(id , new GameRules(id), false);
        
        parser.parseField(info.player1Field);
        setShips(game, parser.getShipData(), 0);
        setHits(game, parser.getHitData(), 0);
        parser.parseField(info.player2Field);
        setShips(game, parser.getShipData(), 1);
        setHits(game, parser.getHitData(), 1);
        return game;
    }
    
    private void setShips(Game game, List<LocationData> ships, int playerNum)
    {
        Grid grid = game.getPlayerGrid(playerNum);
        for(LocationData ship: ships) {
            grid.setShip(ShipBuilder.buildShip(ship.shipType), ship.coordinates[0], ship.coordinates[1], ship.direction);
        }
    }
    
    private void setHits(Game game, boolean[][] hitData, int playerNum)
    {
        Grid grid = game.getPlayerGrid(playerNum);
        
        for(int i = 0; i < hitData.length; i++) {
            for(int j = 0; j < hitData[0].length; j++) {
                try {
                    if(hitData[j][i]) {
                        grid.hit(j, i);
                    }
                } catch (AlreadyHitException | ShipIsKilledException e) {
                    try {
                        game.getPlayer(playerNum).destroyShip();
                    } catch (GameOverException e1) {}
                }
            }
        }
    }
    
    private final DatabaseInterface database = new SqlDatabaseInterface();
}
