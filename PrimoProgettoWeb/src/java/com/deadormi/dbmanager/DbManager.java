/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deadormi.dbmanager;

import com.deadormi.servlet.LoginServlet;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.log4j.Logger;

/**
 *
 * @author Davide
 */
public class DbManager implements Serializable {

    private transient Connection connection;
    static Logger log = Logger.getLogger(LoginServlet.class);

    public DbManager(String dburl) throws SQLException {

        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver", true, getClass().getClassLoader());
        } catch (Exception e) {
            log.warn(e.toString(), e);
        }

        Connection con = DriverManager.getConnection(dburl);
        this.connection = con;
    }

    public static void shutdown() {
        try {
            DriverManager.getConnection("jdbc:derby:;shutdown=true");
        } catch (SQLException ex) {
            log.warn("DBManager shutdown"+ ex.toString());
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
