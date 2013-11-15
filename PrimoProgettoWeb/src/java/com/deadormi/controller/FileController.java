/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deadormi.controller;

import com.deadormi.dbmanager.DbManager;
import com.deadormi.servlet.CreaPostServlet;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

/**
 *
 * @author Davide
 */
public class FileController {

    public static void inserisciFiles(HttpServletRequest request,int post_id) throws NamingException {
        // Get the base naming context
        Context env = (Context) new InitialContext().lookup("java:comp/env");
        // Get a single value
        String filePath = (String) env.lookup("file-upload");
        int maxFileSize = 4 * 1024;
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // maximum size that will be stored in memory
        factory.setSizeThreshold(maxFileSize); //max file size 4 mega
        // Location to save data that is larger than maxMemSize.
        factory.setRepository(new File("c:\\temp"));
        // Create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload(factory);
        // maximum file size to be uploaded.
        upload.setSizeMax(maxFileSize);
        // Create a new file upload handler
        try {
            File file;
            // Parse the request to get file items.
            List fileItems = upload.parseRequest(request);
            // Process the uploaded file items
            Iterator i = fileItems.iterator();
            while (i.hasNext()) {
                FileItem fi = (FileItem) i.next();
                if (!fi.isFormField()) {
                    // Get the uploaded file parameters
                    //String fieldName = fi.getFieldName();
                    String fileName = fi.getName();
                    String contentType = fi.getContentType();
                    System.out.println(contentType);
                   // FileController.mappaFile(request, fileName.toString(),post_id);
                    //boolean isInMemory = fi.isInMemory();
                    //long sizeInBytes = fi.getSize();
                    // Write the file
                    String filecompletepath;
                    if (fileName.lastIndexOf("\\") >= 0) {
                        filecompletepath = filePath + fileName.substring(fileName.lastIndexOf("\\"));
                    } else {
                        filecompletepath = filePath + fileName.substring(fileName.lastIndexOf("\\") + 1);
                    }
                    file = new File(filecompletepath);
                    fi.write(file);
                    
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(CreaPostServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static Integer mappaFile(HttpServletRequest request, String nome, Integer post_id) throws SQLException {
        com.deadormi.entity.File file;
        ResultSet generated_keys;
        DbManager dbmanager = (DbManager) request.getServletContext().getAttribute("dbmanager");
        Connection connection = dbmanager.getConnection();
        PreparedStatement stm = connection.prepareStatement("INSERT INTO ROOT.FILE (nome_file,id_post) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS);
        try {
            stm.setString(1, nome);
            stm.setInt(2, post_id);
            stm.executeUpdate();
            generated_keys = stm.getGeneratedKeys();
            return generated_keys.getInt(1);
        } finally {
            stm.close();
        }
    }
}
