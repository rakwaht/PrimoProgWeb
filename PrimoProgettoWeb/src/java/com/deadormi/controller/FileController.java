/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deadormi.controller;

import com.deadormi.dbmanager.DbManager;
import com.deadormi.util.CurrentDate;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Davide
 */
public class FileController {

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
}
