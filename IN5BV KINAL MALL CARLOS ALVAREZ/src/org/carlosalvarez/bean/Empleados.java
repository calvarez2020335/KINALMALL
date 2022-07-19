/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.carlosalvarez.bean;

import java.sql.Date;
import java.math.BigDecimal;

/**
 *
 * @author Carlos Adolfo Alvarez Crúz
 * @date 9 jul 2021
 * @time 12:21:21
 * Codigo tecnico: IN5BV
 */
public class Empleados {
    
    private int id;
    private String nombres;
    private String apellidos;
    private String email;
    private String telefono;
    private Date fechaContratacion;
    private BigDecimal sueldo;
    private int idDepartamento;
    private int idCargo;
    private int idHorario;
    private int idAdministracion;

    public Empleados() {
    }

    public Empleados(int id, String nombres, String apellidos, String email, String telefono, Date fechaContratacion, BigDecimal sueldo, int idDepartamento, int idCargo, int idHorario, int idAdministracion) {
        this.id = id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.email = email;
        this.telefono = telefono;
        this.fechaContratacion = fechaContratacion;
        this.sueldo = sueldo;
        this.idDepartamento = idDepartamento;
        this.idCargo = idCargo;
        this.idHorario = idHorario;
        this.idAdministracion = idAdministracion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Date getFechaContratacion() {
        return fechaContratacion;
    }

    public void setFechaContratacion(Date fechaContratacion) {
        this.fechaContratacion = fechaContratacion;
    }

    public BigDecimal getSueldo() {
        return sueldo;
    }

    public void setSueldo(BigDecimal sueldo) {
        this.sueldo = sueldo;
    }

    public int getIdDepartamento() {
        return idDepartamento;
    }

    public void setIdDepartamento(int idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    public int getIdCargo() {
        return idCargo;
    }

    public void setIdCargo(int idCargo) {
        this.idCargo = idCargo;
    }

    public int getIdHorario() {
        return idHorario;
    }

    public void setIdHorario(int idHorario) {
        this.idHorario = idHorario;
    }

    public int getIdAdministracion() {
        return idAdministracion;
    }

    public void setIdAdministracion(int idAdministracion) {
        this.idAdministracion = idAdministracion;
    }

    @Override
    public String toString() {
        return  id + " | " + nombres + " " + apellidos;
    }
    
    
    
}
