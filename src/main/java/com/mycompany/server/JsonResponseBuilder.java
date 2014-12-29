package com.mycompany.server;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import com.mycompany.server.game.GridItemDescriptor;

public class JsonResponseBuilder {

    public JsonObject customStateWithFieldResponse(String state, GridItemDescriptor[][] field, int[] dimensions)
    {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("state", state);
        return addField(builder, field, dimensions, "field").build();
    }
    
    public JsonObject victoryResponse()
    {
        return Json.createObjectBuilder().add("gameEnd", "YOU ARE VICTORIOUS!").build();
    }
    
    public JsonObject defeatResponse()
    {
        return Json.createObjectBuilder().add("gameEnd", "NOT SO EASY!").build();
    }
    
    public JsonObject successResponse()
    {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        return addSuccess(builder).build();
    }
    
    public JsonObject successWithFieldResponse(GridItemDescriptor[][] field, int[] dimensions)
    {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        addSuccess(builder);
        return addField(builder, field, dimensions, "field").build();
    }
    
    public JsonObject successWithBothFieldsResponse(GridItemDescriptor[][] field, GridItemDescriptor[][] field1, int[] dimensions)
    {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        addSuccess(builder);
        addField(builder, field1, dimensions, "field1");
        return addField(builder, field, dimensions, "field").build();
    }
    
    public JsonObject errorResponse(String errorMessage)
    {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        return addError(builder, errorMessage).build();
    }
    
    public JsonObject errorWithFieldResponse(String errorMessage, GridItemDescriptor[][] field, int[] dimensions)
    {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        addError(builder, errorMessage);
        return addField(builder, field, dimensions, "field").build();
    }
    
    private JsonObjectBuilder addSuccess(JsonObjectBuilder builder)
    {
        builder.add("state", "success");
        return builder;
    }
 
    private JsonObjectBuilder addError(JsonObjectBuilder builder, String errorString)
    {
        builder.add("state", "error").add("error", errorString);
        return builder;
    }
    
    private JsonObjectBuilder addField(JsonObjectBuilder builder, GridItemDescriptor[][] field, int[] dimensions, String fieldName)
    {
        JsonArrayBuilder rowBuilder = Json.createArrayBuilder();
        
        for(int i = 0; i < dimensions[1]; i++) {
            JsonArrayBuilder columnBuilder = Json.createArrayBuilder();
            for (int j = 0; j < dimensions[0]; j++) {
                columnBuilder.add(field[i][j].toInt());
            }
            rowBuilder.add(columnBuilder);
        }
        builder.add(fieldName, rowBuilder.build());
        return builder;
    }
}
