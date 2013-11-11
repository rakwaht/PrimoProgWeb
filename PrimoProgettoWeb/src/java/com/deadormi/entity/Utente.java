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
    private String nome;
    private String password;
    private Boolean utente_abilitato;
    private Integer id_avatar;

    public void setId_utente(Integer id_utente) {
        this.id_utente = id_utente;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUtente_abilitato(Boolean utente_abilitato) {
        this.utente_abilitato = utente_abilitato;
    }

    public void setId_avatar(Integer id_avatar) {
        this.id_avatar = id_avatar;
    }

    public Integer getId_utente() {
        return id_utente;
    }

    public String getNome() {
        return nome;
    }

    public String getPassword() {
        return password;
    }

    public Boolean isUtente_abilitato() {
        return utente_abilitato;
    }

    public Integer getId_avatar() {
        return id_avatar;
    }
    
}
