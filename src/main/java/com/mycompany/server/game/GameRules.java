package com.mycompany.server.game;

import java.util.Arrays;

public class GameRules implements Cloneable{

    public GameRules(int gameId)
    {
        this(new int[]{ 12, 10 }, 1, 2, 3, 4, gameId);
    }
    
    public GameRules(int[] dimensions, int carriersCount, int destroyersCount, int schoonersCount, int boatsCount, int gameId)
    {
        fieldDimensions = Arrays.copyOf(dimensions, 2);
        this.carriersCount = carriersCount;
        this.destroyersCount = destroyersCount;
        this.schoonersCount = schoonersCount;
        this.boatsCount = boatsCount;
        this.gameId = gameId;
    }
    
    public final int[] fieldDimensions;
    public final int carriersCount;
    public final int destroyersCount;
    public final int schoonersCount;
    public final int boatsCount;
    public final int gameId;
}
