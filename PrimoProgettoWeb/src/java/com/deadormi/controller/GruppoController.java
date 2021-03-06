/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deadormi.controller;

import com.deadormi.dbmanager.DbManager;
import com.deadormi.entity.Gruppo;
import com.deadormi.entity.Invito;
import com.deadormi.util.CurrentDate;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

/**
 *
 * @author Davide
 */
public class GruppoController {

    static Logger  log = Logger.getLogger(GruppoController.class);
    
    public static Integer creaGruppo(HttpServletRequest request) throws SQLException, Exception {
        DbManager dbmanager = (DbManager) request.getServletContext().getAttribute("dbmanager");
        Connection connection = dbmanager.getConnection();
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException ex) {
            log.error(ex);
        }
        String titolo = request.getParameter("titolo");
        if(titolo == null || titolo.trim().length() == 0){
            throw new Exception("titolo vuoto");
        }
        titolo = titolo.replaceAll("<[^>]*>", "");
        String descrizione = request.getParameter("descrizione");
         if(descrizione == null || descrizione.trim().length() == 0){
            throw new Exception("descrizione vuota");
        }
        descrizione = descrizione.replaceAll("<[^>]*>", "");
        if (!titolo.trim().equals("") && !descrizione.trim().equals("")) {
            PreparedStatement stm = connection.prepareStatement("INSERT INTO ROOT.GRUPPO (nome,id_proprietario,data_creazione,descrizione) VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            ResultSet generated_keys;
            HttpSession session = request.getSession();
            Invito invito;
            try {
                stm.setString(1, titolo);
                stm.setInt(2, (Integer) session.getAttribute("user_id"));
                stm.setString(3, CurrentDate.getCurrentDate());
                stm.setString(4, descrizione);
                stm.executeUpdate();
                generated_keys = stm.getGeneratedKeys();
                if (generated_keys.next()) {
                    //mando gli inviti
                    Integer id_gruppo = generated_keys.getInt(1);
                    Gruppo_UtenteController.creaGruppo_utente(request, id_gruppo.toString());
                    request.setAttribute("gruppo_id", id_gruppo);
                    InvitoController.creaInvito(request);
                    return id_gruppo;
                } else {
                    return -1;
                }
            } finally {
                stm.close();

            }
        } else {
            return 0;
        }
    }

    public static Gruppo getGruppoById(HttpServletRequest request, Integer gruppo_id) throws SQLException {
        DbManager dbmanager = (DbManager) request.getServletContext().getAttribute("dbmanager");
        Connection connection = dbmanager.getConnection();
        PreparedStatement stm = connection.prepareStatement("SELECT * FROM ROOT.GRUPPO WHERE id_gruppo=?");
        ResultSet rs;
        Gruppo gruppo = null;
        try {
            stm.setInt(1, gruppo_id);
            rs = stm.executeQuery();
            try {
                while (rs.next()) {
                    gruppo = new Gruppo();
                    gruppo.setNome(rs.getString("nome"));
                    gruppo.setDescrizione(rs.getString("descrizione"));
                    gruppo.setData_creazione(rs.getString("data_creazione"));
                    gruppo.setId_gruppo(gruppo_id);
                    gruppo.setId_proprietario(rs.getInt("id_proprietario"));
                }
            } finally {
                rs.close();
            }
        } finally {
            stm.close();
        }
        return gruppo;
    }

    public static void modificaGruppo(HttpServletRequest request, Integer gruppo_id) throws SQLException, Exception {
        DbManager dbmanager = (DbManager) request.getServletContext().getAttribute("dbmanager");
        Connection connection = dbmanager.getConnection();
        String titolo = request.getParameter("titolo");
        titolo = titolo.replaceAll("<[^>]*>", "");
        String descrizione = request.getParameter("descrizione");
        descrizione = descrizione.replaceAll("<[^>]*>", "");
        //check titolo e descrizione validi
        if(titolo == null || titolo.trim().length() == 0){
            throw new Exception("titolo vuoto");
        }
        else if(descrizione == null || descrizione.trim().length() == 0){
            throw new Exception("descrizione vuota");
        }
        log.debug("salvo");
        PreparedStatement stm = connection.prepareStatement("UPDATE ROOT.GRUPPO SET nome=?, descrizione=? WHERE id_gruppo=?");
        ResultSet rs;
        try {
            stm.setString(1, titolo);
            stm.setString(2, descrizione);
            stm.setInt(3, gruppo_id);
            stm.executeUpdate();

        } finally {
            stm.close();
        }
    }
}
