package com.mycompany.server;

import java.io.BufferedReader;
import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.servlet.http.HttpServletRequest;

public class JsonBuilder {

    public JsonObject getJsonObject(HttpServletRequest request)
    {
        JsonObject object;   
        StringBuffer jb = new StringBuffer();
        String line = null;
        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null)
                jb.append(line);
        } catch (Exception e) {}
        
        object = getJsonObject(jb.toString());
        return object;
    }
    
    public JsonObject getJsonObject(String string)
    {
        JsonObject object;

        JsonReader reader = Json.createReader(new StringReader(string));
        object = reader.readObject();
        return object;
    }
}
