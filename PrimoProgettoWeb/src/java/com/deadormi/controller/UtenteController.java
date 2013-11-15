/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deadormi.controller;

import com.deadormi.dbmanager.DbManager;
import com.deadormi.entity.Utente;
import com.deadormi.util.CookiesManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Davide
 */
public class UtenteController {

    public static Utente authenticate(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        DbManager dbmanager = (DbManager) request.getServletContext().getAttribute("dbmanager");
        Connection connection = dbmanager.getConnection();
        HttpSession session = request.getSession();
        PreparedStatement stm = connection.prepareStatement("SELECT * FROM ROOT.UTENTE WHERE username = ? AND password = ?");
        try {
            stm.setString(1, username);
            stm.setString(2, password);
            ResultSet rs = stm.executeQuery();
            try {
                if (rs.next()) {
                    Utente utente = new Utente();
                    utente.setUsername(username);
                    utente.setId_utente(Integer.parseInt(rs.getString("id_utente")));

                    Boolean logged = true;
                    session.setAttribute("logged", logged);
                    session.setAttribute("user_id", utente.getId_utente());

                    CookiesManager.createNewDateCookie(request, response);

                    return utente;
                } else {
                    return null;
                }
            } finally {
                rs.close();
            }
        } finally {
            stm.close();
        }
    }

    public static Utente getUserById(HttpServletRequest request, Integer user_id) throws SQLException {
        DbManager dbmanager = (DbManager) request.getServletContext().getAttribute("dbmanager");
        Connection connection = dbmanager.getConnection();
        PreparedStatement stm = connection.prepareStatement("SELECT * FROM ROOT.UTENTE WHERE id_utente=?");
        ResultSet rs;
        Utente utente = null;
        try {
            stm.setInt(1, user_id);
            rs = stm.executeQuery();
            try {
                while (rs.next()) {
                    utente = new Utente();
                    utente.setUsername(rs.getString("username"));
                    utente.setId_utente(user_id);
                }
            } finally {
                rs.close();
            }
        } finally {
            stm.close();
        }
        return utente;
    }

    public static List<Utente> getUtenti(HttpServletRequest request) throws SQLException {
        DbManager dbmanager = (DbManager) request.getServletContext().getAttribute("dbmanager");
        Connection connection = dbmanager.getConnection();
        PreparedStatement stm = connection.prepareStatement("SELECT * FROM ROOT.UTENTE WHERE utente_abilitato=true");
        List<Utente> list = new ArrayList<Utente>();
        try {
            ResultSet rs = stm.executeQuery();
            try {
                while (rs.next()) {
                    Utente utente = new Utente();
                    utente.setUsername(rs.getString("username"));
                    utente.setId_utente(Integer.parseInt(rs.getString("id_utente")));
                    list.add(utente);
                }
            } finally {
                rs.close();
            }
        } finally {
            stm.close();
        }
        return list;
    }

    public static List<Utente> getUserByGroupId(HttpServletRequest request, Integer gruppo_id) throws SQLException {
        DbManager dbmanager = (DbManager) request.getServletContext().getAttribute("dbmanager");
        Connection connection = dbmanager.getConnection();
        PreparedStatement stm = connection.prepareStatement("SELECT * FROM ROOT.GRUPPO_UTENTE NATURAL JOIN ROOT.UTENTE WHERE utente_abilitato=true AND id_gruppo=? AND gruppo_utente_abilitato='true'");
        List<Utente> list = new ArrayList<Utente>();
        try {
            stm.setInt(1, gruppo_id);
            ResultSet rs = stm.executeQuery();
            try {
                while (rs.next()) {
                    Utente utente = new Utente();
                    utente.setUsername(rs.getString("username"));
                    utente.setId_utente(Integer.parseInt(rs.getString("id_utente")));
                    list.add(utente);
                }
            } finally {
                rs.close();
            }
        } finally {
            stm.close();
        }
        return list;
    }
}
