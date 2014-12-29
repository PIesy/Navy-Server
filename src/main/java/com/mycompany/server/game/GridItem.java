package com.mycompany.server.game;

import com.mycompany.server.game.exceptions.AlreadyHitException;
import com.mycompany.server.game.exceptions.ShipIsKilledException;
import com.mycompany.server.game.ships.Ship;

public class GridItem {
	
	public GridItem(){}
	
	public boolean hit() throws AlreadyHitException, ShipIsKilledException
	{
		if(hit){
			throw new AlreadyHitException();
		}
		hit = true;
		if(!empty)
		{
			ship.hit();
			return true;
		}
		return false;
	}
	
	public boolean tryToHit()
	{
	    if(hit)
	        return false;
	    return true;
	}
    
	public boolean isHit()
	{
		return hit;
	}
	
	public boolean isEmpty()
	{
		return empty;
	}
	
	public void setShip(Ship ship)
	{
		this.ship = ship;
		empty = false;
	}
	
	public void removeShip()
	{
		ship = null;
	}
	
	private Ship ship = null;
	private boolean empty = true;
	private boolean hit = false;
}
