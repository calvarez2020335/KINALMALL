/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.carlosalvarez.bean;

/**
 *
 * @author Carlos Adolfo Alvarez Crúz
 * @date 28 may 2021
 * @time 16:01:08
 * Codigo tecnico: IN5BV
 */
public class TipoCliente {

    private int id;
    private String descripcion;

    public TipoCliente() {
    }

    public TipoCliente(int id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return  id + " | " + descripcion  ;
    }
    
    
    
}
