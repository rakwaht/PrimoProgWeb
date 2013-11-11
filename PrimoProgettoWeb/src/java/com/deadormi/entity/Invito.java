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
public class Invito implements Serializable {
    private Integer id_invito;
    private Integer id_invitato;
    private Integer id_invitante;
    private Integer id_gruppo;

    public Integer getId_invito() {
        return id_invito;
    }

    public void setId_invito(Integer id_invito) {
        this.id_invito = id_invito;
    }

    public Integer getId_invitato() {
        return id_invitato;
    }

    public void setId_invitato(Integer id_invitato) {
        this.id_invitato = id_invitato;
    }

    public Integer getId_invitante() {
        return id_invitante;
    }

    public void setId_invitante(Integer id_invitante) {
        this.id_invitante = id_invitante;
    }

    public Integer getId_gruppo() {
        return id_gruppo;
    }

    public void setId_gruppo(Integer id_gruppo) {
        this.id_gruppo = id_gruppo;
    }
    
}
