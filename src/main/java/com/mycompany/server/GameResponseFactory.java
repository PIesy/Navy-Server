package com.mycompany.server;

import com.mycompany.server.game.Grid;
import com.mycompany.server.game.GridItemDescriptor;

public class GameResponseFactory {

    public static GameResponse makeSuccessResponse()
    {
        GameResponseBuilder builder = new GameResponseBuilder();
        return builder.addSuccess().build();
    }
    
    public static GameResponse makeErrorResponse(String errorMessage)
    {
        GameResponseBuilder builder = new GameResponseBuilder();
        return builder.addError(errorMessage).build();
    }
    
    public static GameResponse makeSuccessWithFieldResponse(Grid field)
    {
        GameResponseBuilder builder = new GameResponseBuilder();
        int[][] result = getIntegerFieldDescriptor(field);
        builder.addSuccess().build();
        return builder.addPlayerField(result).build();
    }
    
    public static GameResponse makeSuccessWithBothFieldsResponse(Grid playerField, Grid botField)
    {
        GameResponseBuilder builder = new GameResponseBuilder();
        int[][] field = getIntegerFieldDescriptor(playerField);
        builder.addPlayerField(field);
        field = getIntegerFieldDescriptor(botField);
        builder.addBotField(field);
        return builder.addSuccess().build();
    }
    
    public static GameResponse makeEndGameResponse(String message)
    {
        GameResponseBuilder builder = new GameResponseBuilder();
        return builder.addEndGameMessage(message).build();
    }
    
    private static int[][] getIntegerFieldDescriptor(Grid field)
    {
        GridItemDescriptor[][] descriptors = field.getState();
        int[][] result = new int[field.getSizeVertical()][field.getSizeHorizontal()];
        
        for(int i = 0; i < field.getSizeVertical(); i++){
            for(int j = 0; j  < field.getSizeHorizontal(); j++){
                result[i][j] = descriptors[i][j].toInt();
            }
        }
        return result;
    }
}
