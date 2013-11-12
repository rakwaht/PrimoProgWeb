/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deadormi.entity;

import java.io.Serializable;
import java.sql.Blob;

/**
 *
 * @author Davide
 */
public class File implements Serializable{
    
    private Integer id_file;
    private String nome_file;
    private Integer id_post;

    public Integer getId_file() {
        return id_file;
    }

    public void setId_file(Integer id_file) {
        this.id_file = id_file;
    }

    public String getNome_file() {
        return nome_file;
    }

    public void setNome_file(String nome_file) {
        this.nome_file = nome_file;
    }


    public Integer getId_post() {
        return id_post;
    }

    public void setId_post(Integer id_post) {
        this.id_post = id_post;
    }
}
