/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deadormi.dbmanager;

import com.deadormi.entity.Gruppo;
import com.deadormi.entity.Invito;
import com.deadormi.entity.Utente;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Davide
 */
public class DbManager implements Serializable {

    private transient Connection connection;

    public DbManager(String dburl) throws SQLException {

        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver", true, getClass().getClassLoader());
        } catch (Exception e) {
            throw new RuntimeException(e.toString(), e);
        }

        Connection con = DriverManager.getConnection(dburl);
        this.connection = con;
    }

    public static void shutdown() {
        try {
            DriverManager.getConnection("jdbc:derby:;shutdown=true");
        } catch (SQLException ex) {
            //Logger.getLogger(DBManager.class.getName()).info(ex.getMessage());
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public Utente authenticate(String username, String password) throws SQLException {
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

    public List<Utente> getUtentiAbilitati() throws SQLException {
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

    public Integer creaGruppo(Gruppo gruppo) throws SQLException {
        PreparedStatement stm = connection.prepareStatement("INSERT INTO ROOT.GRUPPO (nome,id_proprietario,data_creazione,descrizione) VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
        ResultSet generated_keys;

        try {
            stm.setString(1, gruppo.getNome());
            stm.setInt(2, gruppo.getId_proprietario());
            stm.setString(3, gruppo.getData_creazione());
            stm.setString(4, gruppo.getDescrizione());
            stm.executeUpdate();
            generated_keys = stm.getGeneratedKeys();
            if (generated_keys.next()) {
                return generated_keys.getInt(1);
            } else {
                return 0;
            }
        } finally {
            stm.close();

        }
    }

    public Integer creaInvito(Invito invito) throws SQLException {
        PreparedStatement stm = connection.prepareStatement("INSERT INTO ROOT.INVITO (id_invitato,id_invitante,id_gruppo) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS);
        ResultSet generated_keys;

        try {
            stm.setInt(1, invito.getId_invitato());
            stm.setInt(2, invito.getId_invitante());
            stm.setInt(3, invito.getId_gruppo());
            stm.executeUpdate();
            generated_keys = stm.getGeneratedKeys();
            if (generated_keys.next()) {
                return generated_keys.getInt(1);
            } else {
                return 0;
            }
        } finally {
            stm.close();

        }
    }
}
