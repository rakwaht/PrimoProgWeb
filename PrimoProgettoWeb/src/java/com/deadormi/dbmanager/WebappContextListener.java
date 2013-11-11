/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deadormi.dbmanager;

import java.sql.SQLException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 *
 * @author Davide
 */
public class WebappContextListener implements ServletContextListener{

    @Override
    public void contextInitialized(ServletContextEvent sce) {
       String dburl = sce.getServletContext().getInitParameter("dburl");
       try{
           DbManager manager = new DbManager(dburl);
           sce.getServletContext().setAttribute("dbmanager", manager);
       }
       catch(SQLException e){
           
       }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
       DbManager.shutdown();
    }
    
}
