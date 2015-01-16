package com.mycompany.server;

import javax.persistence.Id;

public class GameInfo
{
    @Id
    public int id = 0;
    public int[][] player1Field = null;
    public int[][] player2Field = null;
    public String[] playerNames = new String[2];
}
