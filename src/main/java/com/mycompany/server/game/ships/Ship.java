package com.mycompany.server.game.ships;

import com.mycompany.server.game.exceptions.ShipIsKilledException;

public class Ship {
	
	public Ship()
	{
		size = 0;
		shipType = "";
		hitpoints = 0;
	}
	
	public Ship(String shipType, int size)
	{
		this.shipType = shipType;
		this.size = size;
		hitpoints = size;
	}
	
	public String getType()
	{
		return shipType;
	}
	
	public int getSize()
	{
		return size;
	}
	
	public void hit() throws ShipIsKilledException
	{
		hitpoints--;
		if(hitpoints == 0){
			throw new ShipIsKilledException(shipType);
		}
	}
		
	private final int size;
	private final String shipType;
	private int hitpoints;
}
