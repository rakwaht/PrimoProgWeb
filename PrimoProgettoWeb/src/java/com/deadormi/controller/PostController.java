/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deadormi.controller;

import com.deadormi.dbmanager.DbManager;
import com.deadormi.entity.Post;
import com.deadormi.util.CurrentDate;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

/**
 *
 * @author Timbu
 */
public class PostController {

    final static String FILE_RESOURCE_PATH = "/resource/files";

    public static List<Post> getPostByGruppoId(HttpServletRequest request, Integer id_gruppo) throws SQLException {
        List<Post> posts = new ArrayList<Post>();
        Post post = null;
        DbManager dbmanager = (DbManager) request.getServletContext().getAttribute("dbmanager");
        Connection connection = dbmanager.getConnection();
        PreparedStatement stm = connection.prepareStatement("SELECT * FROM ROOT.POST WHERE id_gruppo=?");
        ResultSet rs;
        try {
            stm.setInt(1, id_gruppo);
            rs = stm.executeQuery();
            try {
                while (rs.next()) {
                    post = new Post();
                    post.setId_post(rs.getInt("id_post"));
                    post.setId_scrivente(rs.getInt("id_scrivente"));
                    post.setId_gruppo(rs.getInt("id_gruppo"));
                    post.setData_creazione(rs.getString("data_creazione"));
                    post.setTesto("testo");
                    posts.add(post);
                }
            } finally {
                rs.close();
            }
        } finally {
            stm.close();
        }
        return posts;
    }

    public static Integer salvaPost(HttpServletRequest request, String group_id) throws FileUploadException, SQLException {
        HttpSession session = request.getSession();
        DbManager dbmanager = (DbManager) request.getServletContext().getAttribute("dbmanager");
        Connection connection = dbmanager.getConnection();
        PreparedStatement stm = connection.prepareStatement("INSERT INTO ROOT.POST (id_scrivente,id_gruppo,testo,data_creazione) VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
        //preparo l'estrazione del testo dal form
        String testo = null;
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
        List fileItems = upload.parseRequest(request);
        // Create a new file upload handler
        Iterator i = fileItems.iterator();
        //preparo le directory
        File file;
        String filePath = request.getServletContext().getRealPath(FILE_RESOURCE_PATH + "/" + group_id);
        File directory = new File(filePath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        //itero gli input
        while (i.hasNext()) {
            FileItem fi = (FileItem) i.next();
            if (fi.isFormField() && fi.getFieldName().equals("testo")) {
                testo = fi.getString();
            } else if (fi.getFieldName().equals("file") && fi.getSize()>0) {
                // Get the uploaded file parameters
                String fieldName = fi.getFieldName();
                String fileName = fi.getName();
                String contentType = fi.getContentType();
                boolean isInMemory = fi.isInMemory();
                long sizeInBytes = fi.getSize();
                // Write the file
                file = new File(filePath + "/" + sizeInBytes + fileName);
                try {
                    fi.write(file);
                } catch (Exception ex) {
                    Logger.getLogger(PostController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        try {
            stm.setInt(1, (Integer) session.getAttribute("user_id"));
            stm.setInt(2, Integer.parseInt(group_id));
            stm.setString(3, testo);
            stm.setString(4, CurrentDate.getCurrentDate());
            stm.executeUpdate();
            if (stm.getGeneratedKeys().next()) {
                return stm.getGeneratedKeys().getInt(1);
            } else {
                return -1;
            }
        } finally {
            stm.close();
        }
    }

    public static void creaPost(HttpServletRequest request) throws SQLException, FileUploadException {
        String id_gruppo = request.getParameter("id_gruppo");
        //salvo il post
        Integer post_id = PostController.salvaPost(request, id_gruppo);
        /*
         //salvo i file relativi al post appena creato
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
         List fileItems = upload.parseRequest(request);
         // Create a new file upload handler
         Iterator i = fileItems.iterator();
         // cerco il path in cui mettere il file nel web.xml
         Context env;
        
         //se non esiste creo la directory
        
         //itero i file
         */
    }
}
