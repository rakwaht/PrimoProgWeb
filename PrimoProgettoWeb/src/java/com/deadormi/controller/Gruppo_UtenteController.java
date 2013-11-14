/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deadormi.controller;

import com.deadormi.dbmanager.DbManager;
import com.deadormi.entity.Gruppo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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

    public static List<Gruppo> getGruppiByUserId(HttpServletRequest request) throws SQLException {
        DbManager dbmanager = (DbManager) request.getServletContext().getAttribute("dbmanager");
        Connection connection = dbmanager.getConnection();
        HttpSession session = request.getSession();
        List<Gruppo> miei_gruppi = new ArrayList<Gruppo>();
        Gruppo gruppo;
        ResultSet rs;
        PreparedStatement stm = connection.prepareStatement("SELECT * FROM ROOT.GRUPPO_UTENTE NATURAL JOIN ROOT.GRUPPO WHERE id_utente=? ");
        try {
            stm.setInt(1, (Integer)session.getAttribute("user_id"));
            rs = stm.executeQuery();
            try {
                while (rs.next()) {
                    gruppo = new Gruppo();
                    gruppo.setData_creazione(rs.getString("data_creazione"));
                    gruppo.setId_gruppo(rs.getInt("id_gruppo"));
                    gruppo.setDescrizione(rs.getString("descrizione"));
                    gruppo.setGruppo_abilitato(rs.getBoolean("gruppo_abilitato"));
                    gruppo.setId_proprietario(rs.getInt("id_proprietario"));
                    gruppo.setNome(rs.getString("nome"));
                    miei_gruppi.add(gruppo);
                }
            } finally {
                   rs.close();
            }
        } finally {
            stm.close();
        }
        return miei_gruppi;
    }

    public static Boolean checkUser_Group(HttpServletRequest request, Integer user_id, Integer gruppo_id) throws SQLException {
       DbManager dbmanager = (DbManager) request.getServletContext().getAttribute("dbmanager");
       Connection connection = dbmanager.getConnection();
       PreparedStatement stm = connection.prepareStatement("SELECT * FROM ROOT.GRUPPO_UTENTE WHERE id_utente=? AND id_gruppo=?");
       ResultSet rs; 
       try {
            stm.setInt(1, (Integer)user_id);
            stm.setInt(2, (Integer)gruppo_id);
            rs = stm.executeQuery();
            try {
                if(rs.next()) {
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
