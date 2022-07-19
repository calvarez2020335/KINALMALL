/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.carlosalvarez.controller;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import org.carlosalvarez.bean.Proveedores;
import org.carlosalvarez.db.Conexion;
import org.carlosalvarez.report.GenerarReporte;
import org.carlosalvarez.system.Principal;

/**
 *
 * @author Carlos Adolfo Alvarez Crúz
 * @date 21 jun 2021
 * @time 13:02:24 Codigo tecnico: IN5BV
 */
public class ProveedoresController implements Initializable {

    private Principal escenarioPrincipal;

    private ObservableList<Proveedores> listaProveedores;
    @FXML
    private TextField txtSaldoLiquido;

    private enum Operaciones {
        NUEVO, GUARDAR, EDITAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO
    }

    private Operaciones operacion = Operaciones.NINGUNO;

    @FXML
    private Button btntNuevo;
    @FXML
    private ImageView imgNuevo;
    @FXML
    private Button btnEliminar;
    @FXML
    private ImageView imgEliminar;
    @FXML
    private Button btnEditar;
    @FXML
    private ImageView imgEditar;
    @FXML
    private Button btnReporte;
    @FXML
    private ImageView imgReporte;
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtNit;
    @FXML
    private TextField txtServicioPrestado;
    @FXML
    private TableView tblProveedores;
    @FXML
    private TableColumn colId;
    @FXML
    private TableColumn colNit;
    @FXML
    private TableColumn colServicioPrestado;
    @FXML
    private TableColumn colTelefono;
    @FXML
    private TableColumn colDireccion;
    @FXML
    private TableColumn colSaldoFavor;
    @FXML
    private TableColumn colSaldoContra;
    @FXML
    private TextField txtTelefono;
    @FXML
    private TextField txtDireccion;
    @FXML
    private TextField txtSaldoContra;
    @FXML
    private TextField txtSaldoFavor;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatos();
        txtSaldoLiquido.setDisable(true);
    }

    public ObservableList<Proveedores> getProveedores() {
        ArrayList<Proveedores> listado = new ArrayList<Proveedores>();
        PreparedStatement stmt = null;
        ResultSet resultado = null;

        try {
            
            stmt = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarProveedores}");
            resultado = stmt.executeQuery();

            while (resultado.next()) {
                listado.add(new Proveedores(resultado.getInt("id"),
                        resultado.getString("nit"),
                        resultado.getString("servicioPrestado"),
                        resultado.getString("telefono"),
                        resultado.getString("direccion"),
                        resultado.getBigDecimal("saldoFavor"),
                        resultado.getBigDecimal("saldoContra")));
            }
            listaProveedores = FXCollections.observableArrayList(listado);
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

        return listaProveedores;
    }

    public void cargarDatos() {
        tblProveedores.setItems(getProveedores());
        colId.setCellValueFactory(new PropertyValueFactory<Proveedores, Integer>("id"));
        colNit.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("nit"));
        colServicioPrestado.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("servicioPrestado"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("telefono"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("direccion"));
        colSaldoFavor.setCellValueFactory(new PropertyValueFactory<Proveedores, BigDecimal>("saldoFavor"));
        colSaldoContra.setCellValueFactory(new PropertyValueFactory<Proveedores, BigDecimal>("saldoContra"));
    }

    public boolean existeElementoSeleccionado() {
        if (tblProveedores.getSelectionModel().getSelectedItem() == null) {
            return false;
        } else {
            return true;
        }
    }

    public boolean ValidarTelefono(String numero) {
        Pattern pattern = Pattern.compile("^[0-9]{8}$");
        Matcher matcher = pattern.matcher(numero);
        return matcher.matches();
    }

    public boolean ValidarDecimal(String numero) {
        Pattern pattern = Pattern.compile("^[0-9]{1,8}+([.][0-9]{1,2})?$");
        Matcher matcher = pattern.matcher(numero);
        return matcher.matches();
    }

    private void agregarProveedores() {
        Proveedores registro = new Proveedores();
        registro.setNit(txtNit.getText());
        registro.setServicioPrestado(txtServicioPrestado.getText());
        registro.setTelefono(txtTelefono.getText());
        registro.setDireccion(txtDireccion.getText());
        BigDecimal a = new BigDecimal(txtSaldoFavor.getText());
        registro.setSaldoFavor(a);
        BigDecimal b = new BigDecimal(txtSaldoContra.getText());
        registro.setSaldoContra(b);

        try {
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarProveedores(?,?,?,?,?,?)}");
            stmt.setString(1, registro.getNit());
            stmt.setString(2, registro.getServicioPrestado());
            stmt.setString(3, registro.getTelefono());
            stmt.setString(4, registro.getDireccion());
            stmt.setBigDecimal(5, registro.getSaldoFavor());
            stmt.setBigDecimal(6, registro.getSaldoContra());
            stmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void eliminarProveedores() {
        try {
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarProveedores(?)}");
            stmt.setInt(1, ((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getId());
            stmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void editarProveedores() {

        ArrayList<TextField> listaTexfield = new ArrayList<>();

        listaTexfield.add(txtId);
        listaTexfield.add(txtNit);
        listaTexfield.add(txtServicioPrestado);
        listaTexfield.add(txtTelefono);
        listaTexfield.add(txtDireccion);
        listaTexfield.add(txtSaldoFavor);
        listaTexfield.add(txtSaldoContra);

        if (escenarioPrincipal.validarDatos(listaTexfield)) {

            Proveedores registro = new Proveedores();

            registro.setId(Integer.parseInt(txtId.getText()));
            registro.setNit(txtNit.getText());
            registro.setServicioPrestado(txtServicioPrestado.getText());
            registro.setTelefono(txtTelefono.getText());
            registro.setDireccion(txtDireccion.getText());
            BigDecimal a = new BigDecimal(txtSaldoFavor.getText());
            registro.setSaldoFavor(a);
            BigDecimal b = new BigDecimal(txtSaldoContra.getText());
            registro.setSaldoContra(b);

            try {
                PreparedStatement stmt;
                stmt = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarProveedores(?,?,?,?,?,?,?)}");
                stmt.setInt(1, registro.getId());
                stmt.setString(2, registro.getNit());
                stmt.setString(3, registro.getServicioPrestado());
                stmt.setString(4, registro.getTelefono());
                stmt.setString(5, registro.getDireccion());
                stmt.setBigDecimal(6, registro.getSaldoFavor());
                stmt.setBigDecimal(7, registro.getSaldoContra());
                stmt.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setTitle("KINAL MALLA");
            alert.setContentText("Falta Agregar Datos");
            alert.showAndWait();
        }
    }

    @FXML
    private void seleccionarElemento() {
        if (existeElementoSeleccionado()) {
            txtId.setText(String.valueOf(((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getId()));
            txtNit.setText(((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getNit());
            txtServicioPrestado.setText(((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getServicioPrestado());
            txtTelefono.setText(((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getTelefono());
            txtDireccion.setText(((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getDireccion());
            txtSaldoFavor.setText(String.valueOf(((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getSaldoFavor()));
            txtSaldoContra.setText(String.valueOf(((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getSaldoContra()));
            saldoLiquido();
        }

    }

    private void saldoLiquido() {

        try {
            BigDecimal a = new BigDecimal(txtSaldoFavor.getText());
            BigDecimal op1 = a;
            BigDecimal b = new BigDecimal(txtSaldoContra.getText());
            BigDecimal op2 = b;
            txtSaldoLiquido.setText(String.valueOf(a.subtract(b)));

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

    }

    private void habilitarCampos() {
        txtId.setEditable(false);
        txtNit.setEditable(true);
        txtServicioPrestado.setEditable(true);
        txtTelefono.setEditable(true);
        txtDireccion.setEditable(true);
        txtSaldoFavor.setEditable(true);
        txtSaldoContra.setEditable(true);
    }

    private void deshabilitarCampos() {
        txtId.setEditable(false);
        txtNit.setEditable(false);
        txtServicioPrestado.setEditable(false);
        txtTelefono.setEditable(false);
        txtDireccion.setEditable(false);
        txtSaldoFavor.setEditable(false);
        txtSaldoContra.setEditable(false);
    }

    private void limpiarCampos() {
        txtId.clear();
        txtNit.clear();
        txtServicioPrestado.clear();
        txtTelefono.clear();
        txtDireccion.clear();
        txtSaldoFavor.clear();
        txtSaldoContra.clear();
        txtSaldoLiquido.clear();
    }

    @FXML
    private void nuevo(ActionEvent event) {
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
                if (txtNit.getText().length() != 0 && txtDireccion.getText().length() != 0
                        && txtTelefono.getText().length() != 0 && txtServicioPrestado.getText().length() != 0
                        && txtSaldoFavor.getText().length() != 0 && txtSaldoContra.getText().length() != 0) {
                    if (ValidarDecimal(txtSaldoFavor.getText()) && ValidarDecimal(txtSaldoContra.getText())) {
                        if (ValidarTelefono(txtTelefono.getText())) {

                            agregarProveedores();
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
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setHeaderText(null);
                            alert.setTitle("KINAL MALL");
                            alert.setContentText("El numero telefonico ingresado es incorrecto");
                            alert.showAndWait();
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setHeaderText(null);
                        alert.setTitle("KINAL MALL");
                        alert.setContentText("El valor numerico ingresado es invalido");
                        alert.showAndWait();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setTitle("KINAL MALL");
                    alert.setContentText("Debe rellenar los datos correspondientes");
                    alert.showAndWait();
                }
                break;

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

                if (tblProveedores.getSelectionModel().getSelectedItem() != null) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setHeaderText(null);
                    alert.setTitle("Confirmación");
                    alert.setContentText("¿Estas seguro de eliminar el elemento seleccionado?");
                    Optional<ButtonType> result = alert.showAndWait();

                    if (result.get() == ButtonType.OK) {
                        try {
                            eliminarProveedores();
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

        switch (operacion) {
            case NINGUNO:
                if (tblProveedores.getSelectionModel().getSelectedItem() != null) {
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
                if (ValidarDecimal(txtSaldoContra.getText()) && ValidarDecimal(txtSaldoFavor.getText())) {
                    if (ValidarTelefono(txtTelefono.getText())) {
                        editarProveedores();
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
                        alert.setContentText("El numero telefonico ingresado es invalido");
                        alert.showAndWait();

                    }
                } else {

                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setHeaderText(null);
                    alert.setTitle("KINAL MALL");
                    alert.setContentText("El valor numerico ingresado es invalido");
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
                GenerarReporte.getInstance().mostrarReporte("ReporteProveedores.jasper", "Reporte Proveedores", parametros);
        }

    }

    @FXML
    private void mostrarCuentasPorPagar(ActionEvent event) {
        this.escenarioPrincipal.mostrarCuentasPorPagar();
    }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    @FXML
    private void mostrarVistaMenuPrincipal(MouseEvent event) {
        this.escenarioPrincipal.mostrarMenuPrincipal();
    }

}
