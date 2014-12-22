package com.mycompany.server.game;

public enum Directions {
	North(0, -1), East(1, 0), South(0, 1), West(-1, 0), None(5, 5);
	
	private Directions(int x, int y) {
		offset[0] = x;
		offset[1] = y;
	}
	
	public int[] convertTo2DOffset()
	{
		return offset;
	}
	
	private int[] offset = {0,0};
}
