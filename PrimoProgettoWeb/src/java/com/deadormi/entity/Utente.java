/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deadormi.entity;

import java.io.Serializable;

/**
 *
 * @author Davide
 */
public class Utente implements Serializable {
    private Integer id_utente;
    private String username;
    private String password;
    private Boolean utente_abilitato;
    private String nome_avatar;
    
    public String getNome_avatar() {
        return nome_avatar;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setNome_avatar(String nome_avatar) {
        this.nome_avatar = nome_avatar;
    }

    public void setId_utente(Integer id_utente) {
        this.id_utente = id_utente;
    }

   

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUtente_abilitato(Boolean utente_abilitato) {
        this.utente_abilitato = utente_abilitato;
    }

    

    public Integer getId_utente() {
        return id_utente;
    }

    

    public String getPassword() {
        return password;
    }

    public Boolean isUtente_abilitato() {
        return utente_abilitato;
    }

    
    
}
