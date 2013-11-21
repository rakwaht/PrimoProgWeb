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
import java.util.Enumeration;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

/**
 *
 * @author Davide
 */
public class InvitoController {

    static Logger  log = Logger.getLogger(InvitoController.class);
    
    static void creaInvito(HttpServletRequest request) throws SQLException {
        DbManager dbmanager = (DbManager) request.getServletContext().getAttribute("dbmanager");
        Connection connection = dbmanager.getConnection();
        String[] utenti_selezionati = request.getParameterValues("utenti_selezionati");
        HttpSession session = request.getSession();
        Integer gruppo_id = (Integer) request.getAttribute("gruppo_id");
        if (utenti_selezionati != null) {
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

    public static void processaInviti(HttpServletRequest request) throws SQLException {
        Enumeration<String> id_gruppi = request.getParameterNames();
        while (id_gruppi.hasMoreElements()) {
            String id_gruppo = id_gruppi.nextElement();
            String value = request.getParameter(id_gruppo);
            if (value.equals("true")) {
                Gruppo_UtenteController.creaGruppo_utente(request, id_gruppo);
            }
            InvitoController.eliminaInvitoById(request, id_gruppo);
        }
    }

    private static void eliminaInvitoById(HttpServletRequest request, String id_gruppo) throws SQLException {
        DbManager dbmanager = (DbManager) request.getServletContext().getAttribute("dbmanager");
        Connection connection = dbmanager.getConnection();
        HttpSession session = request.getSession();
        PreparedStatement stm = connection.prepareStatement("UPDATE ROOT.INVITO SET invito_abilitato=false WHERE id_gruppo=? AND id_invitato=?");
        try {
            stm.setInt(1, Integer.parseInt(id_gruppo));
            stm.setInt(2, (Integer) session.getAttribute("user_id"));
            stm.executeUpdate();
        } finally {
            stm.close();
        }
    }

    public static void processaRe_Inviti(HttpServletRequest request, Integer gruppo_id) throws SQLException {
        DbManager dbmanager = (DbManager) request.getServletContext().getAttribute("dbmanager");
        Connection connection = dbmanager.getConnection();
        String[] utenti_selezionati = request.getParameterValues("non_utenti_selezionati");
        HttpSession session = request.getSession();

        if (utenti_selezionati != null) {
            for (int i = 0; i < utenti_selezionati.length; i++) {
                if (!InvitoController.checkInvitoByUserId(request, Integer.parseInt(utenti_selezionati[i]), gruppo_id)) {
                    PreparedStatement stm = connection.prepareStatement("INSERT INTO ROOT.INVITO (id_invitato,id_invitante,id_gruppo) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS);
                    try {
                        stm.setInt(1, Integer.parseInt(utenti_selezionati[i]));
                        stm.setInt(2, (Integer) session.getAttribute("user_id"));
                        stm.setInt(3, gruppo_id);
                        stm.executeUpdate();
                    } finally {
                        stm.close();

                    }
                } else {
                    PreparedStatement stm = connection.prepareStatement("UPDATE ROOT.INVITO SET invito_abilitato='true' WHERE id_invitato=? AND id_gruppo=?", Statement.RETURN_GENERATED_KEYS);
                    try {
                        stm.setInt(1, Integer.parseInt(utenti_selezionati[i]));
                        stm.setInt(2, gruppo_id);
                        
                        stm.executeUpdate();
                    } finally {
                        stm.close();

                    }
                }
            }
        }

    }

    public static boolean checkInvitoByUserId(HttpServletRequest request, Integer user_id, Integer gruppo_id) throws SQLException {
        DbManager dbmanager = (DbManager) request.getServletContext().getAttribute("dbmanager");
        Connection connection = dbmanager.getConnection();
        PreparedStatement stm = connection.prepareStatement("SELECT * FROM ROOT.INVITO WHERE id_invitato=? AND invito_abilitato='true' AND id_gruppo=?", Statement.RETURN_GENERATED_KEYS);
        ResultSet rs;
        try {
            stm.setInt(1, user_id);
            stm.setInt(2, gruppo_id);
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
