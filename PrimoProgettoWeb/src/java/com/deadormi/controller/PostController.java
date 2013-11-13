/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.deadormi.controller;

import com.deadormi.dbmanager.DbManager;
import com.deadormi.entity.Gruppo;
import com.deadormi.entity.Post;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Timbu
 */
public class PostController {
    
    public static List<Post> getPostByGruppoId(HttpServletRequest request, Integer id_gruppo) throws SQLException{
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
    
}
