/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deadormi.controller;

import com.deadormi.dbmanager.DbManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Davide
 */
public class Gruppo_UtenteController {

    public static void creaGruppo_utente(HttpServletRequest request, String id_gruppo) throws SQLException {
        DbManager dbmanager = (DbManager) request.getServletContext().getAttribute("dbmanager");
        Connection connection = dbmanager.getConnection();
        HttpSession session = request.getSession();
        PreparedStatement stm = connection.prepareStatement("INSERT INTO ROOT.GRUPPO_UTENTE (id_gruppo,id_utente) VALUES (?,?)");
        try {
            stm.setInt(1, Integer.parseInt(id_gruppo));
            stm.setInt(2, (Integer) session.getAttribute("user_id"));
            stm.executeUpdate();
        } finally {
            stm.close();
        }
    }

    
    
    
}
