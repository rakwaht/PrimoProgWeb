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
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

/**
 *
 * @author Timbu
 */
public class PostController {

    static Logger  log = Logger.getLogger(PostController.class);
    final static String FILE_RESOURCE_PATH = "/resource/files";

    public static List<Post> getPostByGruppoId(HttpServletRequest request, Integer id_gruppo) throws SQLException {
        List<Post> posts = new ArrayList<Post>();
        Post post = null;
        DbManager dbmanager = (DbManager) request.getServletContext().getAttribute("dbmanager");
        Connection connection = dbmanager.getConnection();
        PreparedStatement stm = connection.prepareStatement("SELECT * FROM ROOT.POST WHERE id_gruppo=? ORDER BY data_creazione DESC");
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
                    post.setTesto(rs.getString("testo"));
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

    public static Integer creaPost(HttpServletRequest request) throws SQLException, FileUploadException {
        String group_id = request.getParameter("id_gruppo");
        HttpSession session = request.getSession();
        DbManager dbmanager = (DbManager) request.getServletContext().getAttribute("dbmanager");
        Connection connection = dbmanager.getConnection();
        PreparedStatement stm = connection.prepareStatement("INSERT INTO ROOT.POST (id_scrivente,id_gruppo,testo,data_creazione) VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
        //preparo l'estrazione del testo dal form
        String testo = null;
        Integer post_id;
        int maxFileSize = 10 * 1024 * 1024;
        if(request.getContentLength() >= maxFileSize){
            log.debug("Troppo grande " + request.getContentLength());
            return 1;
        }
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // maximum size that will be stored in memory
        factory.setSizeThreshold(maxFileSize); //max file size 10 mega
        // Location to save data that is larger than maxMemSize.
        factory.setRepository(new File("c:\\temp"));
        // Create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setHeaderEncoding("UTF-8");
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
                try {
                    testo = fi.getString("UTF-8");
                } catch (UnsupportedEncodingException ex) {
                    log.error(ex);
                }
            }
        }
        testo = PostController.checkTesto(request, testo, Integer.parseInt(group_id));

        if (!testo.trim().equals("")) {
            try {

                stm.setInt(1, (Integer) session.getAttribute("user_id"));
                stm.setInt(2, Integer.parseInt(group_id));
                stm.setString(3, testo);
                stm.setString(4, CurrentDate.getCurrentDate());
                stm.executeUpdate();
                if (stm.getGeneratedKeys().next()) {
                    post_id = stm.getGeneratedKeys().getInt(1);
                } else {
                    post_id = -1;
                }
            } finally {
                stm.close();
            }
            ListIterator i2 = fileItems.listIterator();
            // Create a new file upload handler
            while (i2.hasNext()) {
                FileItem fi = (FileItem) i2.next();
                if (!fi.isFormField() && fi.getFieldName().equals("file") && fi.getSize() > 0) {
                    // Get the uploaded file parameters
                    String fieldName = fi.getFieldName();
                    String fileName = fi.getName();
                    String contentType = fi.getContentType();
                    boolean isInMemory = fi.isInMemory();
                    long sizeInBytes = fi.getSize();
                    // Write the file
                    Integer file_id = FileController.salvaFile(request, fileName, post_id);
                    file = new File(filePath + "/" + file_id + "-" + fileName);
                    try {
                        fi.write(file);
                    } catch (Exception ex) {
                        log.error(ex);
                    }
                    
                }
            }
        }
        return 0;
    }

    public static List<Post> getPostByGruppoIdAndUserId(HttpServletRequest request, Integer gruppo_id, Integer id_utente) throws SQLException {
        List<Post> posts = new ArrayList<Post>();
        Post post = null;
        DbManager dbmanager = (DbManager) request.getServletContext().getAttribute("dbmanager");
        Connection connection = dbmanager.getConnection();
        PreparedStatement stm = connection.prepareStatement("SELECT * FROM ROOT.POST WHERE id_gruppo=? AND id_scrivente=? ORDER BY data_creazione");
        ResultSet rs;
        try {
            stm.setInt(1, gruppo_id);
            stm.setInt(2, id_utente);
            rs = stm.executeQuery();
            try {
                while (rs.next()) {
                    post = new Post();
                    post.setId_post(rs.getInt("id_post"));
                    post.setId_scrivente(rs.getInt("id_scrivente"));
                    post.setId_gruppo(rs.getInt("id_gruppo"));
                    post.setData_creazione(rs.getString("data_creazione"));
                    post.setTesto(rs.getString("testo"));
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

    public static List<Post> getAllPosts(HttpServletRequest request) throws SQLException {
        List<Post> posts = new ArrayList<Post>();
        Post post = null;
        DbManager dbmanager = (DbManager) request.getServletContext().getAttribute("dbmanager");
        Connection connection = dbmanager.getConnection();
        PreparedStatement stm = connection.prepareStatement("SELECT * FROM ROOT.POST ORDER BY data_creazione");
        ResultSet rs;
        try {
            rs = stm.executeQuery();
            try {
                while (rs.next()) {
                    post = new Post();
                    post.setId_post(rs.getInt("id_post"));
                    post.setId_scrivente(rs.getInt("id_scrivente"));
                    post.setId_gruppo(rs.getInt("id_gruppo"));
                    post.setData_creazione(rs.getString("data_creazione"));
                    post.setTesto(rs.getString("testo"));
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

    public static List<Post> getMyGroupsPosts(HttpServletRequest request) throws SQLException {
        List<Post> posts = new ArrayList<Post>();
        Post post = null;
        Cookie[] cookies = request.getCookies();
        Cookie old_cookie = null;

        Integer user_id = (Integer) request.getSession().getAttribute("user_id");
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                if (cookies[i].getName().equals("old_cookie" + user_id)) {
                    old_cookie = cookies[i];
                }
            }
        }
        if (cookies != null && old_cookie != null) {
            DbManager dbmanager = (DbManager) request.getServletContext().getAttribute("dbmanager");
            Connection connection = dbmanager.getConnection();
            PreparedStatement stm = connection.prepareStatement("SELECT * FROM ROOT.POST WHERE id_gruppo IN (SELECT id_gruppo FROM ROOT.GRUPPO_UTENTE WHERE id_utente=? AND gruppo_utente_abilitato='true') AND data_creazione>=?  AND data_creazione<=? ORDER BY data_creazione DESC");
            ResultSet rs;
            try {
                stm.setInt(1, user_id);
                stm.setString(2, old_cookie.getValue());
                stm.setString(3, CurrentDate.getCurrentDate());
                rs = stm.executeQuery();
                try {
                    while (rs.next()) {
                        post = new Post();
                        post.setId_post(rs.getInt("id_post"));
                        post.setId_scrivente(rs.getInt("id_scrivente"));
                        post.setId_gruppo(rs.getInt("id_gruppo"));
                        post.setData_creazione(rs.getString("data_creazione"));
                        post.setTesto(rs.getString("testo"));
                        posts.add(post);
                    }
                } finally {
                    rs.close();
                }
            } finally {
                stm.close();
            }
        }
        return posts;
    }

    private static String checkTesto(HttpServletRequest request, String testo, Integer id_gruppo) throws SQLException {
        testo = testo.replaceAll("<[^>]*>", "");
        Pattern p = Pattern.compile("\\$\\$(.*?)\\$\\$");
        Matcher m = p.matcher(testo);
        String inizio;
        String trovata;
        String target = testo;
        while (m.find()) {
            inizio = testo.substring(m.start(), m.end());
            trovata = testo.substring(m.start() + 2, m.end() - 2);

            String website = PostController.checkWeb(trovata);

            if (website != null) {
                target = target.replace(inizio, "<a href='" + website + "' target='_blank'>" + trovata + "</a>");
            } else if (FileController.isFile(request, trovata, id_gruppo)) {
                target = target.replace(inizio, "<a href='" + request.getContextPath() + "/resource/files/" + id_gruppo + "/" + trovata + "' target='_blank'> " + trovata + "</a>");
            } else {
                target = target.replace(inizio, trovata);

            }

        }
        return target;
    }

    private static String checkWeb(String trovata) {        
        if (trovata != null) {
            int dotOccurences = trovata.length()-trovata.replace(".", "").length();
            if (trovata.length() >= 12 && trovata.substring(0, 7).equals("http://") && dotOccurences >= 1) {
                return trovata;
            } else if (trovata.length() >= 9 && trovata.substring(0, 4).equals("www.") && dotOccurences >= 2) {
                return "http://" + trovata;
            }
        }
        return null;
    }
}
