/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deadormi.controller;

import com.deadormi.dbmanager.DbManager;
import com.deadormi.entity.Invito;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Davide
 */
public class InvitoController {

    static void creaInvito(HttpServletRequest request) throws SQLException {
        DbManager dbmanager = (DbManager) request.getServletContext().getAttribute("dbmanager");
        Connection connection = dbmanager.getConnection();
        String[] utenti_selezionati = request.getParameterValues("utenti_selezionati");
        HttpSession session = request.getSession();
        Integer gruppo_id = (Integer) request.getAttribute("gruppo_id");
        for (int i = 0; i < utenti_selezionati.length; i++) {
            PreparedStatement stm = connection.prepareStatement("INSERT INTO ROOT.INVITO (id_invitato,id_invitante,id_gruppo) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS);
            try {
                stm.setInt(1, Integer.parseInt(utenti_selezionati[i]));
                stm.setInt(2, (Integer) session.getAttribute("user_id"));
                stm.setInt(3, gruppo_id);
                stm.executeUpdate();
            } finally {
                stm.close();

            }
        }
    }

    public static List<Invito> getInvitiByUserId(HttpServletRequest request) throws SQLException {
        DbManager dbmanager = (DbManager) request.getServletContext().getAttribute("dbmanager");
        Connection connection = dbmanager.getConnection();
        HttpSession session = request.getSession();
        Integer user_id = (Integer) session.getAttribute("user_id");        
        PreparedStatement stm = connection.prepareStatement("SELECT * FROM ROOT.INVITO WHERE id_invitato=? AND invito_abilitato=true");
        ResultSet rs;
        Invito invito;
        List<Invito> inviti = new ArrayList<Invito>();
        try {
            stm.setInt(1, user_id);
            rs = stm.executeQuery();
            try {
                while (rs.next()) {
                    invito = new Invito();
                    invito.setId_invito(rs.getInt("id_invito"));
                    invito.setId_invitato(rs.getInt("id_invitato"));
                    invito.setId_invitante(rs.getInt("id_invitante"));
                    invito.setId_gruppo(rs.getInt("id_gruppo"));
                    invito.setInvito_abilitato(rs.getBoolean("invito_abilitato"));
                    inviti.add(invito);
                }
            } finally {
                rs.close();
            }
        } finally {
            stm.close();
        }
        return inviti;
    
    }

}
