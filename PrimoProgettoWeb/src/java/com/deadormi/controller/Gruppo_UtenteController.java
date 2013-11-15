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
        Integer user_id = (Integer) session.getAttribute("user_id");

        if (!Gruppo_UtenteController.checkUserNotInGroup(request, user_id, Integer.parseInt(id_gruppo))) {
            PreparedStatement stm = connection.prepareStatement("INSERT INTO ROOT.GRUPPO_UTENTE (id_gruppo,id_utente) VALUES (?,?)");
            try {
                stm.setInt(1, Integer.parseInt(id_gruppo));
                stm.setInt(2, user_id);
                stm.executeUpdate();
            } finally {
                stm.close();
            }
        }
        else{
           PreparedStatement stm = connection.prepareStatement("UPDATE ROOT.GRUPPO_UTENTE SET gruppo_utente_abilitato='true' WHERE id_gruppo=? AND id_utente=?");
            try {
                stm.setInt(1, Integer.parseInt(id_gruppo));
                stm.setInt(2, user_id);
                stm.executeUpdate();
            } finally {
                stm.close();
            } 
        }
        
        
    }

    

    public static List<Gruppo> getGruppiByUserId(HttpServletRequest request) throws SQLException {
        DbManager dbmanager = (DbManager) request.getServletContext().getAttribute("dbmanager");
        Connection connection = dbmanager.getConnection();
        HttpSession session = request.getSession();
        List<Gruppo> miei_gruppi = new ArrayList<Gruppo>();
        Gruppo gruppo;
        ResultSet rs;
        PreparedStatement stm = connection.prepareStatement("SELECT * FROM ROOT.GRUPPO_UTENTE NATURAL JOIN ROOT.GRUPPO WHERE id_utente=? AND gruppo_utente_abilitato='true' ");
        try {
            stm.setInt(1, (Integer) session.getAttribute("user_id"));
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
        PreparedStatement stm = connection.prepareStatement("SELECT * FROM ROOT.GRUPPO_UTENTE WHERE id_utente=? AND id_gruppo=? AND gruppo_utente_abilitato='true' ");
        ResultSet rs;
        try {
            stm.setInt(1, (Integer) user_id);
            stm.setInt(2, (Integer) gruppo_id);
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

    public static void eliminaUser(HttpServletRequest request, Integer gruppo_id) throws SQLException {
        DbManager dbmanager = (DbManager) request.getServletContext().getAttribute("dbmanager");
        Connection connection = dbmanager.getConnection();
        String[] utenti_selezionati = request.getParameterValues("utenti_selezionati");
        PreparedStatement stm = connection.prepareStatement("UPDATE ROOT.GRUPPO_UTENTE SET gruppo_utente_abilitato='false' WHERE id_gruppo=? AND id_utente=?");
        
        if (utenti_selezionati != null) {
            for (int i = 0; i < utenti_selezionati.length; i++) {
                
                try {
                    stm.setInt(1, (Integer) gruppo_id);
                    stm.setInt(2, Integer.parseInt(utenti_selezionati[i]));
                    stm.executeUpdate();

                } finally {
                    stm.close();
                }
            }
        }
    }

    private static boolean checkUserNotInGroup(HttpServletRequest request, Integer user_id, Integer gruppo_id) throws SQLException {
        DbManager dbmanager = (DbManager) request.getServletContext().getAttribute("dbmanager");
        Connection connection = dbmanager.getConnection();
        PreparedStatement stm = connection.prepareStatement("SELECT * FROM ROOT.GRUPPO_UTENTE WHERE id_utente=? AND id_gruppo=? AND gruppo_utente_abilitato='false'");
        ResultSet rs;
        try {
            stm.setInt(1, (Integer) user_id);
            stm.setInt(2, (Integer) gruppo_id);
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
