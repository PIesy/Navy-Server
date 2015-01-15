package com.mycompany.server;

import java.util.ArrayList;
import java.util.List;

import com.mycompany.data.game.Directions;
import com.mycompany.data.game.LocationData;
import com.mycompany.data.game.ships.ShipBuilder.ShipType;

public class FieldParser
{
    
    public FieldParser(int[] fieldDimensions)
    {
        hitData = new boolean[fieldDimensions[1]][fieldDimensions[0]];
    }
    
    public void parseField(int[][] field)
    {
        for(int i = 0; i < field.length; i++) {
            for(int j = 0; j < field[0].length; j++) 
            {
                parseIfShip(j, i, field);
                parseIfHit(j, i, field);
            }
        }
        fillShipData();
    }
    
    public List<LocationData> getShipData()
    {
        return ships;
    }
    
    public boolean[][] getHitData()
    {
        return hitData;
    }
    
    private void parseIfShip(int x, int y, int[][] field)
    {
        if(field[y][x] > 1) 
        {
            if(!isPresentShip(new int[]{ x, y }))
            {
                List<int[]> tmp = new ArrayList<>();
                tmp.add(new int[]{ x, y });
                shipsCoordinates.add(tmp);
            }
        }
    }
    
    private void parseIfHit(int x, int y, int[][] field)
    {
        if((field[y][x] == 1) || (field[y][x] == 3)) {
            hitData[y][x] = true;
        } else {
            hitData[y][x] = false;
        }
    }
    
    private boolean isPresentShip(int[] coordinates)
    {
        List<int[]> temp = null;
        
        for(List<int[]> ship: shipsCoordinates) {
            for(int[] loc: ship) {
                if(areAdjacentCoordinates(loc, coordinates)) {
                    temp = ship;
                }
            }
        }
        if(temp != null) {
            temp.add(coordinates);
            return true;
        }
        return false;
    }
    
    private boolean areAdjacentCoordinates(int[] a, int[] b)
    {
        for(int i = 0; i < 2; i++){
            if(Math.abs(a[i] - b[i]) > 1) {
                return false;
            }
        }
        return true;
    }
    
    private void fillShipData()
    {
        LocationData data;
        
        for(List<int[]> ship: shipsCoordinates) 
        {
            data = new LocationData();
            data.shipType = ShipType.parseSize(ship.size());
            if(ship.size() > 1) {
                data.direction = getDirection(ship.get(0), ship.get(1));
            } else {
                data.direction = Directions.South;
            }
            data.coordinates = ship.get(0);
            ships.add(data);
        }
    }
    
    private Directions getDirection(int[] a, int[] b)
    {
        if((a[0] - b[0]) == 0) { 
            return Directions.South;
        }
        return Directions.East;
    }
    
    private List<List<int[]>> shipsCoordinates = new ArrayList<>();
    private List<LocationData> ships = new ArrayList<>();
    private boolean[][] hitData;
}
