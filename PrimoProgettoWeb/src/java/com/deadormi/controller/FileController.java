/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deadormi.controller;

import static com.deadormi.controller.PostController.log;
import com.deadormi.dbmanager.DbManager;
import com.deadormi.entity.FileApp;
import com.deadormi.entity.Utente;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

/**
 *
 * @author Davide
 */
public class FileController {

    static Logger log = Logger.getLogger(FileController.class);
    final static String AVATAR_RESOURCE_PATH = "/resource/avatar";

    static Integer salvaFile(HttpServletRequest request, String fileName, Integer post_id) throws SQLException {
        DbManager dbmanager = (DbManager) request.getServletContext().getAttribute("dbmanager");
        Connection connection = dbmanager.getConnection();
        HttpSession session = request.getSession();
        PreparedStatement stm = connection.prepareStatement("INSERT INTO ROOT.FILE (nome_file,id_post) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS);
        log.debug("salvo file " + fileName);
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

    public static Integer cambiaAvatar(HttpServletRequest request) throws SQLException {
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
        if (request.getContentLength() >= maxFileSize) {
            log.debug("Troppo grande " + request.getContentLength());
            return 1; //file troppo grande
        }
        try {
            fileItems = upload.parseRequest(request);
        } catch (FileUploadException ex) {
            log.error(ex);
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
                fileName = fileName.replace("'", "-");
                String contentType = fi.getContentType();
                boolean isInMemory = fi.isInMemory();
                long sizeInBytes = fi.getSize();
                //check if is an image
                String mimeType = request.getServletContext().getMimeType(fileName);
                if (mimeType.startsWith("image")) {
                    UtenteController.updateAvatar(request, fileName);
                    file = new File(avatar_path + "/" + user_id + "_" + fi.getName());
                    try {
                        fi.write(file);
                    } catch (Exception ex) {
                        log.error(ex);
                    }
                    return 0;
                } else {
                    return 2; // file non è un immagine
                }
            }
        }
        return 3; //nessun campo nel form
    }

    public static List<FileApp> getFilesByPostId(HttpServletRequest request, Integer id_post) throws SQLException {
        List<FileApp> files = new ArrayList<FileApp>();
        DbManager dbmanager = (DbManager) request.getServletContext().getAttribute("dbmanager");
        Connection connection = dbmanager.getConnection();
        ResultSet rs;
        FileApp file;
        PreparedStatement stm = connection.prepareStatement("SELECT * FROM ROOT.FILE NATURAL JOIN ROOT.POST WHERE id_post=?");
        try {
            stm.setInt(1, id_post);
            rs = stm.executeQuery();
            try {
                while (rs.next()) {
                    file = new FileApp();
                    file.setId_file(rs.getInt("id_file"));
                    file.setId_post(rs.getInt("id_post"));
                    file.setNome_file(rs.getString("nome_file"));
                    files.add(file);
                }
            } finally {
                rs.close();
            }
        } finally {
            stm.close();
        }
        return files;
    }

    public static List<FileApp> getFilesByGroupId(HttpServletRequest request, Integer id_gruppo) throws SQLException {
        List<FileApp> files = new ArrayList<FileApp>();
        DbManager dbmanager = (DbManager) request.getServletContext().getAttribute("dbmanager");
        Connection connection = dbmanager.getConnection();
        ResultSet rs;
        FileApp file;
        PreparedStatement stm = connection.prepareStatement("SELECT * FROM ROOT.FILE NATURAL JOIN ROOT.POST WHERE id_gruppo=?");
        try {
            stm.setInt(1, id_gruppo);
            rs = stm.executeQuery();
            try {
                while (rs.next()) {
                    file = new FileApp();
                    file.setId_file(rs.getInt("id_file"));
                    file.setId_post(rs.getInt("id_post"));
                    file.setNome_file(rs.getString("nome_file"));
                    files.add(file);
                }
            } finally {
                rs.close();
            }
        } finally {
            stm.close();
        }
        return files;

    }

    static boolean isFile(HttpServletRequest request, String trovata, Integer id_gruppo) throws SQLException {
        DbManager dbmanager = (DbManager) request.getServletContext().getAttribute("dbmanager");
        Connection connection = dbmanager.getConnection();
        PreparedStatement stm = connection.prepareStatement("SELECT * FROM ROOT.POST NATURAL JOIN ROOT.FILE WHERE id_gruppo=? AND nome_file=? AND id_file=?");
        ResultSet rs;
        if (!trovata.contains("-")) {
            return false;
        } else {
            String nome_file = trovata.substring(trovata.indexOf("-") + 1, trovata.length());
            String id_file = trovata.substring(0, trovata.indexOf("-"));
            try {
                Integer.parseInt(id_file);
            } catch (NumberFormatException e) {
                return false;
            }
            // only got here if we didn't return false


            try {
                stm.setInt(1, id_gruppo);
                stm.setString(2, nome_file);
                stm.setInt(3, Integer.parseInt(id_file));
                rs = stm.executeQuery();
                try {
                    if (rs.next()) {
                        return true;
                    }
                } finally {
                    rs.close();
                }
            } finally {
                stm.close();
            }
            return false;
        }
    }
}
