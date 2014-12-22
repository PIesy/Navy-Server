package com.mycompany.server.game;

import com.mycompany.server.game.Grid;
import com.mycompany.server.game.exceptions.AllShipsSetException;
import com.mycompany.server.game.exceptions.GameOverException;
import com.mycompany.server.game.ships.Ship;
import com.mycompany.server.game.ships.ShipBuilder;
import com.mycompany.server.game.ships.ShipBuilder.ShipType;

public class Player 
{
	public Player(Grid field)
	{
		this.field = field;
		initShips();
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
		return ships[unsetShipCount];
	}
	
	public void unsetShip()
	{
		unsetShipCount++;
	}
	
	public void destroyShip() throws GameOverException
	{
		remainingShips--;
		if(remainingShips == 0){
			throw new GameOverException();
		}
	}
	
	private void initShips()
	{
		int i;
		for(i = 0; i < 4; i++){
			ships[i] = ShipBuilder.buildShip(ShipType.Boat);
		}
		for(i = 4; i < 7; i++){
			ships[i] = ShipBuilder.buildShip(ShipType.Schooner);
		}
		for(i = 7; i < 9; i++){
			ships[i] = ShipBuilder.buildShip(ShipType.Destroyer);
		}
		ships[i] = ShipBuilder.buildShip(ShipType.Carrier);
	}
	
	private final static int SHIP_COUNT = 10;
	private Ship[] ships = new Ship[SHIP_COUNT];
	private int unsetShipCount = SHIP_COUNT;
	private int remainingShips = SHIP_COUNT;
	private String name = "";
	private Grid field;
}