/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deadormi.controller;

import com.deadormi.dbmanager.DbManager;
import com.deadormi.entity.Utente;
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 *
 * @author Davide
 */
public class FileController {

    final static String AVATAR_RESOURCE_PATH = "/resource/avatar";

    static Integer salvaFile(HttpServletRequest request, String fileName, Integer post_id) throws SQLException {
        DbManager dbmanager = (DbManager) request.getServletContext().getAttribute("dbmanager");
        Connection connection = dbmanager.getConnection();
        HttpSession session = request.getSession();
        PreparedStatement stm = connection.prepareStatement("INSERT INTO ROOT.FILE (nome_file,id_post) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS);
        try {
            stm.setString(1, fileName);
            stm.setInt(2, post_id);
            stm.executeUpdate();
            ResultSet generated_keys = stm.getGeneratedKeys();
            if (generated_keys.next()) {
                return generated_keys.getInt(1);
            } else {
                return -1;
            }
        } finally {
            stm.close();
        }
    }

    public static void cambiaAvatar(HttpServletRequest request) throws SQLException {
        String avatar_path = request.getServletContext().getRealPath(AVATAR_RESOURCE_PATH + "/");
        HttpSession session = request.getSession();
        String user_id = session.getAttribute("user_id").toString();
        Utente utente = UtenteController.getUserById(request, Integer.parseInt(user_id));
        int maxFileSize = 4 * 1024 * 1024;
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // maximum size that will be stored in memory
        factory.setSizeThreshold(maxFileSize); //max file size 4 mega
        // Location to save data that is larger than maxMemSize.
        factory.setRepository(new File("c:\\temp"));
        // Create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload(factory);
        // maximum file size to be uploaded.
        upload.setSizeMax(maxFileSize);
        List fileItems = null;
        try {
            fileItems = upload.parseRequest(request);
        } catch (FileUploadException ex) {
            Logger.getLogger(FileController.class.getName()).log(Level.SEVERE, null, ex);
        }
        // Create a new file upload handler
        Iterator i = fileItems.iterator();
        //preparo le directory
        File file;
        File directory = new File(avatar_path);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        //itero gli input
        while (i.hasNext()) {
            FileItem fi = (FileItem) i.next();
            if (!fi.isFormField() && fi.getFieldName().equals("avatar") && fi.getSize() > 0) {
                // Get the uploaded file parameters
                String fieldName = fi.getFieldName();
                String fileName = fi.getName();
                String contentType = fi.getContentType();
                boolean isInMemory = fi.isInMemory();
                long sizeInBytes = fi.getSize();
                // Write the file
                System.out.println("Ciao cazzo");
                UtenteController.updateAvatar(request,fileName);
                file = new File(avatar_path + "/" + user_id + "_" + fi.getName());
                try {
                    fi.write(file);
                } catch (Exception ex) {
                    Logger.getLogger(PostController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
