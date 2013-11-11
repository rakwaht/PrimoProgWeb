/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deadormi.util;

import com.deadormi.dbmanager.DbManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 *
 * @author Davide
 */
public class LoginController {
    
    private transient Connection connection;
    
    public boolean authenticate(String username,String password) throws SQLException{
        
        return true;
    }
    
}
