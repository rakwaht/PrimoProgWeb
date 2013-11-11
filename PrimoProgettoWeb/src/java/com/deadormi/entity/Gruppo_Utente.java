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
public class Gruppo_Utente implements Serializable{
    private Integer id_gruppo;
    private Integer id_utente;

    public Integer getId_gruppo() {
        return id_gruppo;
    }

    public void setId_gruppo(Integer id_gruppo) {
        this.id_gruppo = id_gruppo;
    }

    public Integer getId_utente() {
        return id_utente;
    }

    public void setId_utente(Integer id_utente) {
        this.id_utente = id_utente;
    }
    
}
