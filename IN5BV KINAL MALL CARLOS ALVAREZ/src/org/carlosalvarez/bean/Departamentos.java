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
 * @time 15:48:38
 * Codigo tecnico: IN5BV
 */
public class Departamentos {

    private int id;
    private String nombre;

    public Departamentos() {
    }

    public Departamentos(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return  id + " | " + nombre;
    }
    
    
    
}
