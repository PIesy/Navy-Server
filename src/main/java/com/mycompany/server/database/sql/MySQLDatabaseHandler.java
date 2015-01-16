package com.mycompany.server.database.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLDatabaseHandler
{
    
    public MySQLDatabaseHandler()
    {
        try {
            Class.forName(jdbcDriver);
            isDriverRegistered = true;
        } catch (ClassNotFoundException e) {}
    }

    public void write(String preparedStatement, Object... parameters)
    {
        PreparedStatement statement = null;
        
        if(!isDriverRegistered) {
            return;
        }  
        try {
            openConnection();
            statement = connection.prepareStatement(preparedStatement);
            fillParameters(statement, parameters);
            statement.executeUpdate();
        } catch(SQLException e){} 
        try {
            if(statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeConnection();
    }
    
    public ResultSet read(String preparedStatement, Object... parameters)
    {
        PreparedStatement statement = null;
        
        if(!isDriverRegistered) {
            return null;
        }
        try {
            openConnection();
            statement = connection.prepareStatement(preparedStatement, 
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            fillParameters(statement, parameters);
            return statement.executeQuery();
        } catch(SQLException e) {} 
        try {
            if(statement != null) {
                statement.close();
            }
        } catch (SQLException e) {}
        closeConnection();
        return null;
    }
    
    public void delete(String preparedStatement, Object... parameters)
    {
        PreparedStatement statement = null;
        
        if(!isDriverRegistered) {
            return;
        }
        try {
            openConnection();
            statement = connection.prepareStatement(preparedStatement);
            fillParameters(statement, parameters);
            statement.execute();
        } catch(SQLException e) {} 
        try {
            if(statement != null) {
                statement.close();
            }
        } catch (SQLException e) {}
        closeConnection();
    }
     
    private void fillParameters(PreparedStatement statement, Object... parameters) throws SQLException
    {
        int i = 1;
        
        for(Object parameter: parameters)
        {
            statement.setObject(i, parameter);
            i++;
        }
    }
    
    private void openConnection() throws SQLException
    {
        connection = DriverManager.getConnection(dbUrl, userName, password);
    }
    
    private void closeConnection()
    {
        try {
            connection.close();
        } catch (SQLException e) {}
    }
    
    private boolean isDriverRegistered = false;
    private Connection connection = null;
    private final String jdbcDriver = "com.mysql.jdbc.Driver";
    private final String dbUrl = "jdbc:mysql://localhost:3306/temp";
    private final String userName = "user";
    private final String password = "qwerty";
}
