/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deadormi.dbmanager;

import java.io.File;
import java.sql.SQLException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author Davide
 */
public class WebappContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        //init loggersce
        String homeDir = sce.getServletContext().getRealPath("/");
        File propertiesFile = new File(homeDir, "WEB-INF/log4j.properties");
        PropertyConfigurator.configure(propertiesFile.toString());
        //end init logger

        String dburl = sce.getServletContext().getInitParameter("dburl");
        try {
            DbManager manager = new DbManager(dburl);
            sce.getServletContext().setAttribute("dbmanager", manager);
        } catch (SQLException e) {
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        DbManager.shutdown();
    }
}
