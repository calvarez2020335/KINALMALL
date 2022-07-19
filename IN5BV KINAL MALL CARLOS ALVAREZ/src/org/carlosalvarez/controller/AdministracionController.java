/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.carlosalvarez.controller;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.carlosalvarez.db.Conexion;
import org.carlosalvarez.system.Principal;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.carlosalvarez.bean.Administracion;
import org.carlosalvarez.report.GenerarReporte;

/**
 *
 * @author Carlos Adolfo Alvarez Crúz
 * @date 26 may 2021
 * @time 15:39:01 Codigo tecnico: IN5BV
 */
public class AdministracionController implements Initializable {

    private Principal escenarioPrincipal;

    private ObservableList<Administracion> listaAdministracion;
    @FXML
    private ImageView imgNuevo;
    @FXML
    private ImageView imgEliminar;
    @FXML
    private ImageView imgEditar;
    @FXML
    private ImageView imgReporte;

    @FXML
    private void mostrarDepartamentos(ActionEvent event) {
        this.escenarioPrincipal.mostrarDepartamentos();
    }

    @FXML
    private void mostrarCargos(ActionEvent event) {
        this.escenarioPrincipal.mostrarCargos();
    }

    @FXML
    private void mostrarTipoCliente(ActionEvent event) {
        this.escenarioPrincipal.mostrarTipoCliente();
    }

    @FXML
    private void mostrarLocales(ActionEvent event) {
        this.escenarioPrincipal.mostrarLocales();
    }

    private enum Operaciones {
        NUEVO, GUARDAR, EDITAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO
    }

    private Operaciones operacion = Operaciones.NINGUNO;

    @FXML
    private Button btntNuevo;

    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnEditar;

    @FXML
    private Button btnReporte;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtDireccion;

    @FXML
    private TextField txtTelefono;

    @FXML
    private TableView tblAdministracion;

    @FXML
    private TableColumn colId;

    @FXML
    private TableColumn colDireccion;

    @FXML
    private TableColumn colTelefono;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatos();
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    @FXML
    public void mostrarVistaMenuPrincipal() {
        this.escenarioPrincipal.mostrarMenuPrincipal();
    }

    public ObservableList<Administracion> getAdministracion() {

        ArrayList<Administracion> listado = new ArrayList<Administracion>();

        PreparedStatement stmt = null;
        ResultSet resultado = null;

        try {
            stmt = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarAdministracion()}");
            resultado = stmt.executeQuery();

            while (resultado.next()) {
                listado.add(new Administracion(resultado.getInt("id"),
                        resultado.getString("direccion"),
                        resultado.getNString("telefono")));
            }
            resultado.close();
            stmt.close();
            listaAdministracion = FXCollections.observableArrayList(listado);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            try {
                resultado.close();
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return listaAdministracion;
    }

    public void cargarDatos() {
        tblAdministracion.setItems(getAdministracion());
        colId.setCellValueFactory(new PropertyValueFactory<Administracion, Integer>("id"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Administracion, String>("direccion"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Administracion, String>("telefono"));

    }

    public boolean existeElementoSeleccionado() {
        if (tblAdministracion.getSelectionModel().getSelectedItem() == null) {
            return false;
        } else {
            return true;
        }
    }

    private void agregarAdministracion() {

        Administracion registro = new Administracion();
        registro.setDireccion(txtDireccion.getText());
        registro.setTelefono(txtTelefono.getText());

        try {

            //CallableStatement 
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarAdministracion(?,?)}");
            stmt.setString(1, registro.getDireccion());
            stmt.setString(2, registro.getTelefono());
            //stmt.executeUpdate();
            stmt.execute();
            System.out.println(stmt.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void eliminarAdministracion() {
        try {
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarAdministracion(?)}");
            // stmt.setInt(1, Integer.parseInt(txtId.getText()));
            stmt.setInt(1, ((Administracion) tblAdministracion.getSelectionModel().getSelectedItem()).getId());
            stmt.execute();
            System.out.println(stmt.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void editarAdministracion() {

        ArrayList<TextField> listaTetxField = new ArrayList<>();

        listaTetxField.add(txtId);
        listaTetxField.add(txtDireccion);
        listaTetxField.add(txtTelefono);

        if (escenarioPrincipal.validarDatos(listaTetxField)) {

            Administracion registro = (Administracion) tblAdministracion.getSelectionModel().getSelectedItem();
            registro.setId(Integer.parseInt(txtId.getText()));
            registro.setDireccion(txtDireccion.getText());
            registro.setTelefono(txtTelefono.getText());

            try {
                PreparedStatement stmt;
                stmt = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarAdministracion(?,?,?)}");
                stmt.setInt(1, registro.getId());
                stmt.setString(2, registro.getDireccion());
                stmt.setString(3, registro.getTelefono());
                stmt.execute();
                System.out.println(stmt.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setTitle("KINAL MALL");
            alert.setContentText("Falta agregar datos");
            alert.showAndWait();
        }
    }

    @FXML
    public void seleccionarElemento() {
        if (existeElementoSeleccionado()) {
            txtId.setText(String.valueOf(((Administracion) tblAdministracion.getSelectionModel().getSelectedItem()).getId()));
            txtDireccion.setText(((Administracion) tblAdministracion.getSelectionModel().getSelectedItem()).getDireccion());
            txtTelefono.setText(((Administracion) tblAdministracion.getSelectionModel().getSelectedItem()).getTelefono());
        }
    }

    private void habilitarCampos() {
        txtId.setEditable(false);
        txtDireccion.setEditable(true);
        txtTelefono.setEditable(true);

    }

    private void deshabilitarCampos() {
        txtId.setEditable(false);
        txtDireccion.setEditable(false);
        txtTelefono.setEditable(false);

    }

    private void limpiarCampos() {
        txtId.clear();
        txtDireccion.clear();
        txtTelefono.clear();
    }

    public boolean ValidarTelefono(String numero) {
        Pattern pattern = Pattern.compile("^[0-9]{8}$");
        Matcher matcher = pattern.matcher(numero);
        return matcher.matches();
    }

    @FXML
    private void nuevo(ActionEvent event) {
        System.out.println("Operacion actual " + operacion);

        switch (operacion) {
            case NINGUNO:
                limpiarCampos();
                habilitarCampos();
                btntNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                imgNuevo.setImage(new Image("/org/carlosalvarez/resource/images/guardar.png"));
                imgEliminar.setImage(new Image("/org/carlosalvarez/resource/images/cancelar.png"));
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                operacion = Operaciones.GUARDAR;
                break;
            case GUARDAR:
                if (txtDireccion.getText().length() != 0 && txtTelefono.getText().length() != 0) {
                    if (ValidarTelefono(txtTelefono.getText())) {
                        agregarAdministracion();
                        cargarDatos();
                        deshabilitarCampos();
                        limpiarCampos();
                        btntNuevo.setText("Nuevo");
                        btnEliminar.setText("Eliminar");
                        imgNuevo.setImage(new Image("/org/carlosalvarez/resource/images/nuevo.png"));
                        imgEliminar.setImage(new Image("/org/carlosalvarez/resource/images/eliminar.png"));
                        btnEditar.setDisable(false);
                        btnReporte.setDisable(false);
                        operacion = Operaciones.NINGUNO;
                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setHeaderText(null);
                        alert.setTitle("KINAL MALL");
                        alert.setContentText("El numero ingresado es invalido");
                        alert.showAndWait();

                    }
                    break;
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setHeaderText(null);
                    alert.setTitle("KINAL MALL");
                    alert.setContentText("Debe rellenar los campos correspondientes");
                    alert.showAndWait();
                }
        }
    }

    @FXML
    private void eliminar(ActionEvent event) {

        switch (operacion) {
            case GUARDAR:
                btntNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                imgNuevo.setImage(new Image("/org/carlosalvarez/resource/images/nuevo.png"));
                imgEliminar.setImage(new Image("/org/carlosalvarez/resource/images/eliminar.png"));
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                limpiarCampos();
                deshabilitarCampos();
                operacion = Operaciones.NINGUNO;
                break;
            case NINGUNO://Elimanación
                if (tblAdministracion.getSelectionModel().getSelectedItem() != null) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setHeaderText(null);
                    alert.setTitle("Confirmación");
                    alert.setContentText("¿Estas seguro de eliminar el elemento seleccionado?");
                    Optional<ButtonType> result = alert.showAndWait();

                    if (result.get() == ButtonType.OK) {
                        try {
                            eliminarAdministracion();
                            limpiarCampos();
                            cargarDatos();
                            break;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                } else {

                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setHeaderText(null);
                    alert.setTitle("KINAL MALL");
                    alert.setContentText("Debe seleccionar un elemento");
                    alert.showAndWait();
                }
        }
    }

    @FXML
    private void editar(ActionEvent event) {
        System.out.println("Operacion: " + operacion);

        switch (operacion) {
            case NINGUNO:
                if (tblAdministracion.getSelectionModel().getSelectedItem() != null) {
                    habilitarCampos();
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btntNuevo.setDisable(true);
                    imgEditar.setImage(new Image("/org/carlosalvarez/resource/images/actualizar.png"));
                    imgReporte.setImage(new Image("/org/carlosalvarez/resource/images/cancelar.png"));
                    btnEliminar.setDisable(true);
                    operacion = Operaciones.ACTUALIZAR;
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setHeaderText(null);
                    alert.setTitle("KINAL MALL");
                    alert.setContentText("Debe seleccionar un elemento");
                    alert.showAndWait();
                }
                break;
            case ACTUALIZAR:
                if (ValidarTelefono(txtTelefono.getText())) {
                    editarAdministracion();
                    limpiarCampos();
                    cargarDatos();
                    deshabilitarCampos();
                    btntNuevo.setDisable(false);
                    btnEliminar.setDisable(false);
                    imgEditar.setImage(new Image("/org/carlosalvarez/resource/images/editar.png"));
                    imgReporte.setImage(new Image("/org/carlosalvarez/resource/images/reporte.png"));
                    btnEditar.setText("Editar");
                    btnReporte.setText("Reporte");
                    operacion = Operaciones.NINGUNO;
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setHeaderText(null);
                    alert.setTitle("KINAL MALL");
                    alert.setContentText("El numero ingresado es invalido");
                    alert.showAndWait();
                }
                break;
        }
    }

    @FXML
    private void reporte(ActionEvent event) {

        switch (operacion) {
            case ACTUALIZAR:
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                imgEditar.setImage(new Image("/org/carlosalvarez/resource/images/editar.png"));
                imgReporte.setImage(new Image("/org/carlosalvarez/resource/images/reporte.png"));
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                btntNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                limpiarCampos();
                deshabilitarCampos();
                operacion = Operaciones.NINGUNO;
            case NINGUNO:
                Map parametros = new HashMap();
              //  if (existeElementoSeleccionado()) {

                  //  int idAdministracio = ((Administracion) tblAdministracion.getSelectionModel().getSelectedItem()).getId();

                    //parametros.put("IDADMIN", idAdministracio);

                    //GenerarReporte.getInstance().mostrarReporte("ReporteAdministracionEmpleados.jasper", "Reporte Administracion", parametros);
                //} else {
                    GenerarReporte.getInstance().mostrarReporte("ReporteAdministracion.jasper", "Reporte Administracion", parametros);
                //}
        }

    }

}
