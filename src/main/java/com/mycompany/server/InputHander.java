package com.mycompany.server;

import com.mycompany.data.game.Directions;

public interface InputHander<T>
{

    int[] getCoordinates(T source);

    Directions getDirection(T source);
}
