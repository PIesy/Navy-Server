package com.mycompany.server.game.ships;

public class ShipBuilder {

	public static Ship buildShip(ShipType type)
	{
		return new Ship(type.toString(), type.getSize());
	}
	
	public enum ShipType { 
		Boat(1), Schooner(2), Destroyer(3), Carrier(4);
		
		public int getSize()
		{
			return size;
		}
		
		private ShipType(int size)
		{
			this.size = size;
		}
		
		private final int size;
	}
}
