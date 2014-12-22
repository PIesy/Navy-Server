package com.mycompany.server.game.exceptions;

public class GameOverException extends Exception {

	public GameOverException(){}
	
	public GameOverException(String string)
	{
		super(string);
	}
	
	private static final long serialVersionUID = 4312135835537284555L;
}
