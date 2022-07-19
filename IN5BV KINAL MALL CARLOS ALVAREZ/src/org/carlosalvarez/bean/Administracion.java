/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.carlosalvarez.bean;

/**
 *
 * @author Carlos Adolfo Alvarez Crúz
 * @date 27 may 2021
 * @time 17:20:50
 * Codigo tecnico: IN5BV
 */
public class Administracion {
    
    private int id;
    private String direccion;
    private String telefono;

    public Administracion() {
    }

    public Administracion(int id, String direccion, String telefono) {
        this.id = id;
        this.direccion = direccion;
        this.telefono = telefono;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Override
    public String toString() {
        return  id +" "+"|" +" "+ direccion ;
    }
    
    
}
