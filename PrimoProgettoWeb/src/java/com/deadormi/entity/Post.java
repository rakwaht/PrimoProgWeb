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
public class Post implements Serializable {
    private Integer id_post;
    private Integer id_scrivente;
    private Integer id_gruppo;
    private String testo;

    public void setId_post(Integer id_post) {
        this.id_post = id_post;
    }

    public void setId_scrivente(Integer id_scrivente) {
        this.id_scrivente = id_scrivente;
    }

    public void setId_gruppo(Integer id_gruppo) {
        this.id_gruppo = id_gruppo;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }

    public Integer getId_post() {
        return id_post;
    }

    public Integer getId_scrivente() {
        return id_scrivente;
    }

    public Integer getId_gruppo() {
        return id_gruppo;
    }

    public String getTesto() {
        return testo;
    }
    
}
