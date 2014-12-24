package com.mycompany.server;

import com.mycompany.server.game.Directions;

public interface InputHander<T> {

    int[] getCoordinates(T source);
    Directions getDirection(T source);
}
