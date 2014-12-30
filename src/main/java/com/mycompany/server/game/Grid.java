package com.mycompany.server.game;

import com.mycompany.server.game.GridItem; 
import com.mycompany.server.game.exceptions.AlreadyHitException;
import com.mycompany.server.game.exceptions.ShipIsKilledException;
import com.mycompany.server.game.ships.Ship;

public class Grid {

	public Grid(){}
	
	public Grid(int sizeX, int sizeY){
		initializeGrid(sizeX , sizeY);
	}
	
	public void initializeGrid(int sizeX, int sizeY){
		dimensions[0] = sizeX;
		dimensions[1] = sizeY;
		grid = new GridItem[sizeY][sizeX];
		for (int i = 0; i < sizeY; i++){
			for (int j = 0; j < sizeX; j++){
				grid[i][j] = new GridItem();
			}
		}
	}
	
	public GridItemDescriptor[][] getState()
	{
		GridItemDescriptor[][] states = new GridItemDescriptor[dimensions[1]][dimensions[0]];
		for(int i = 0; i < dimensions[1]; i++){
			for (int j = 0; j < dimensions[0]; j++)
			{
				states[i][j] = new GridItemDescriptor();
				states[i][j].initialize(grid[i][j]);
			}
		}
		return states;
	}

	public boolean hit(int x, int y) throws AlreadyHitException, ShipIsKilledException
	{
		return grid[y][x].hit();
	}
	
	public boolean tryToHit(int x, int y)
	{
	    return grid[y][x].tryToHit();
	}
	
	public int[] getSize()
	{
		return dimensions;
	}
	
	public int getSizeHorizontal()
	{
		return dimensions[0];
	}
	
	public int getSizeVertical()
	{
		return dimensions[1];
	}
	
	public boolean tryToSetShip(Ship ship, int x, int y, Directions direction)
	{
	    int[] offset = direction.convertTo2DOffset();
        int[] startCoordinates = {x, y};
        int[] endCoordinates = new int[2];
        int[] temp;
        
        for(int i = 0; i < 2; i++){
            endCoordinates[i] = startCoordinates[i] + offset[i] * (ship.getSize() - 1);
        }
        if((offset[0] < 0) || (offset[1] < 0))
        {
            temp = startCoordinates;
            startCoordinates = endCoordinates;
            endCoordinates = temp;
        }
        if(isOutOfBounds(startCoordinates) || isOutOfBounds(endCoordinates)){
            return false;
        }
        if(isNearExistingShip(startCoordinates, endCoordinates)){
            return false;
        }
        return true;
	}
	
	public void setShip(Ship ship, int x, int y, Directions direction) throws IndexOutOfBoundsException
	{
		int[] offset = direction.convertTo2DOffset();
		int[] startCoordinates = {x, y};
		int[] endCoordinates = new int[2];
		int[] temp;
		
		for(int i = 0; i < 2; i++){
			endCoordinates[i] = startCoordinates[i] + offset[i] * (ship.getSize() - 1);
		}
		if((offset[0] < 0) || (offset[1] < 0))
		{
			temp = startCoordinates;
			startCoordinates = endCoordinates;
			endCoordinates = temp;
		}
		if(isOutOfBounds(startCoordinates) || isOutOfBounds(endCoordinates)){
			throw new IndexOutOfBoundsException("Ship doesn't fit in field");
		}
		if(isNearExistingShip(startCoordinates, endCoordinates)){
			throw new IndexOutOfBoundsException("Too close too your other ship");
		}
		setShipInGrid(startCoordinates, endCoordinates, ship);
	}
	
	public boolean isOutOfBounds(int[] coordinates)
	{
		for(int i = 0; i < 2; i++){
			if((coordinates[i] < 0) || (coordinates[i] > dimensions[i] - 1)){
				return true;
			}
		}
		return false;
	}
	
	public boolean isNearExistingShip(int[] startCoordinates, int[] endCoordinates)
	{
		for(int i = startCoordinates[1]; i <= endCoordinates[1]; i++ ){
			for(int j = startCoordinates[0]; j <= endCoordinates[0]; j++){
				if(searchCircullar(j, i)){
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean searchCircullar(int x, int y)
	{
		for(int i = y - 1; i <= y + 1; i++){
			for(int j = x - 1; j <= x + 1; j++)
			{
				if((i < 0) || (j < 0) || (i > dimensions[1] - 1) || (j > dimensions[0] - 1)){
					continue;
				}
				if(!grid[i][j].isEmpty()){
					return true;
				}
			}
		}
		return false;
	}
	
	private void setShipInGrid(int[] startCoordinates, int[] endCoordinates, Ship ship)
	{
		for(int i = startCoordinates[1]; i <= endCoordinates[1]; i++ ){
			for(int j = startCoordinates[0]; j <= endCoordinates[0]; j++){
				grid[i][j].setShip(ship);
			}
		}
	}
	
	private GridItem[][] grid;
	private int dimensions[] = new int[2];
}
