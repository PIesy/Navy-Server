package com.mycompany.server.game;

import com.mycompany.server.game.GridItem;

enum ItemState {EmptyNonHit, EmptyHit, ShipNonHit, ShipHit}

public class GridItemDescriptor {

	public GridItemDescriptor(){}
	
	public GridItemDescriptor(GridItem item)
	{
		initialize(item);
	}
	
	public int toInt()
	{
		return state.ordinal();
	}
	
	public void initialize(GridItem item)
	{
		if (!item.isHit() && item.isEmpty()){
			state = ItemState.EmptyNonHit;
		}
		else if (item.isHit() && item.isEmpty()){
			state = ItemState.EmptyHit;
		}
		else if (!item.isHit() && !item.isEmpty()){
			state = ItemState.ShipNonHit;
		}
		else 
			state = ItemState.ShipHit;
	}
	
	private ItemState state = ItemState.EmptyNonHit;
}
