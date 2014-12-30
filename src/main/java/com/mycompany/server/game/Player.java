package com.mycompany.server.game;

import java.util.LinkedList;

import com.mycompany.server.game.Grid;
import com.mycompany.server.game.exceptions.AllShipsSetException;
import com.mycompany.server.game.exceptions.GameOverException;
import com.mycompany.server.game.ships.Ship;
import com.mycompany.server.game.ships.ShipBuilder;
import com.mycompany.server.game.ships.ShipBuilder.ShipType;

public class Player 
{
	public Player(Grid field, GameRules rules)
	{
		this.field = field;
		initRules(rules);
		buildShips();
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}
	
	public Grid getField()
	{
		return field;
	}
	
	public Ship setShip() throws AllShipsSetException
	{
		if(unsetShipCount == 0){
			throw new AllShipsSetException();
		}
		unsetShipCount--;
		lastSetShip = ships.pollLast();
		return lastSetShip;
	}
	
	public void unsetShip()
	{
	    if(lastSetShip != null)
	    {
	        ships.addLast(lastSetShip);
	        lastSetShip = null;
	        unsetShipCount++;
	    }
	}
	
	public void destroyShip() throws GameOverException
	{
		remainingShips--;
		if(remainingShips == 0){
			throw new GameOverException();
		}
	}
	
	private void initRules(GameRules rules)
	{
	    carriersCount = rules.carriersCount;
	    destroyersCount = rules.destroyersCount;
	    schoonersCount = rules.schoonersCount;
	    boatsCount = rules.boatsCount;
	    unsetShipCount += rules.carriersCount
	            + rules.destroyersCount
	            + rules.schoonersCount
	            + rules.boatsCount;
	    remainingShips = unsetShipCount;
	}
	
	private void buildShips()
    {
        buildBoats();
        buildSchooners();
        buildDestroyers();
        buildCarriers();
    }

    private void buildBoats()
    {
        for(int i = 0; i < boatsCount; i++){
            ships.add(ShipBuilder.buildShip(ShipType.Boat));
        }
    }
    
    private void buildSchooners()
    {
        for(int i = 0; i < schoonersCount; i++){
            ships.add(ShipBuilder.buildShip(ShipType.Schooner));
        }
    }
    
    private void buildDestroyers()
    {
        for(int i = 0; i < destroyersCount; i++){
            ships.add(ShipBuilder.buildShip(ShipType.Destroyer));
        }
    }
    
    private void buildCarriers()
    {
        for(int i = 0; i < carriersCount; i++){
            ships.add(ShipBuilder.buildShip(ShipType.Carrier));
        }
    }

    protected Ship lastSetShip = null;
    protected LinkedList<Ship> ships = new LinkedList<>();
    protected int carriersCount;
    protected int destroyersCount;
    protected int schoonersCount;
    protected int boatsCount;
    protected int unsetShipCount = 0;
    protected int remainingShips = 0;
    protected String name = "";
    protected Grid field;
}