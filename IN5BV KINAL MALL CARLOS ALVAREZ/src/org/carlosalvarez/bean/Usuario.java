/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.carlosalvarez.bean;

/**
 *
 * @author Carlos Adolfo Alvarez Crúz
 * @date 4 ago 2021
 * @time 17:26:08
 * Codigo tecnico: IN5BV
 */
public class Usuario {
    
    private String user;
    private String pass;
    private String nombre;
    private int rolid;

    public Usuario() {
    }

    public Usuario(String usuario, String contrasenia, String nombre, int rolid) {
        this.user = usuario;
        this.pass = contrasenia;
        this.nombre = nombre;
        this.rolid = rolid;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getRolid() {
        return rolid;
    }

    public void setRolid(int rolid) {
        this.rolid = rolid;
    }
    
    

}
