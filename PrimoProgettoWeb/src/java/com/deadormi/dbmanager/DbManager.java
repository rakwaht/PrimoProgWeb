/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deadormi.dbmanager;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Davide
 */
public class DbManager implements Serializable{
    
    private transient Connection connection;
    
    public DbManager() throws SQLException{
        String dburl = "jdbc:derby://localhost:1527/PrimoProgetto_Local";
        
        try{
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver", true, getClass().getClassLoader());
        }
        catch(Exception e){
            throw new RuntimeException(e.toString(), e);
        }
        
        Connection con = DriverManager.getConnection(dburl);
        this.connection = con;
    }
    
    public static void shutdown(){
        try{
            DriverManager.getConnection("jdbc:derby:;shutdown=true");
        }
        catch(SQLException e){
            
        }
    }
    
}
