/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deadormi.entity;

import java.io.Serializable;
import java.sql.Date;

/**
 *
 * @author Davide
 */
public class Gruppo implements Serializable{
    
     
    private Integer id_gruppo;
    private String nome;
    private Integer id_proprietario;
    private Date data_creazione;
    private Boolean gruppo_abilitato;

    public Integer getId_gruppo() {
        return id_gruppo;
    }

    public void setId_gruppo(Integer id_gruppo) {
        this.id_gruppo = id_gruppo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getId_proprietario() {
        return id_proprietario;
    }

    public void setId_proprietario(Integer id_proprietario) {
        this.id_proprietario = id_proprietario;
    }

    public Date getData_creazione() {
        return data_creazione;
    }

    public void setData_creazione(Date data_creazione) {
        this.data_creazione = data_creazione;
    }

    public Boolean getGruppo_abilitato() {
        return gruppo_abilitato;
    }

    public void setGruppo_abilitato(Boolean gruppo_abilitato) {
        this.gruppo_abilitato = gruppo_abilitato;
    }
}
