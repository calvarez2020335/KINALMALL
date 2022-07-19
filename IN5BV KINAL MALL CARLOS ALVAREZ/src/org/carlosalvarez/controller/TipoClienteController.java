/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.carlosalvarez.controller;

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
import javafx.scene.input.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.carlosalvarez.bean.TipoCliente;
import org.carlosalvarez.db.Conexion;
import org.carlosalvarez.report.GenerarReporte;
import org.carlosalvarez.system.Principal;

/**
 *
 * @author Carlos Adolfo Alvarez Crúz
 * @date 10 jun 2021
 * @time 12:05:16 Codigo tecnico: IN5BV
 */
public class TipoClienteController implements Initializable {

    private Principal escenarioPrincipal;

    private ObservableList<TipoCliente> listaTipoCliente;
    @FXML
    private ImageView imgNuevo;
    @FXML
    private ImageView imgEliminar;
    @FXML
    private ImageView imgEditar;
    @FXML
    private ImageView imgReporte;

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
    private TextField txtDescripcion;
    @FXML
    private TableView tblTipoCliente;
    @FXML
    private TableColumn colId;
    @FXML
    private TableColumn colDescripcion;

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

    public ObservableList<TipoCliente> getTipoCliente() {

        ArrayList<TipoCliente> listado = new ArrayList<TipoCliente>();
        PreparedStatement stmt = null;
        ResultSet resultado = null;

        try {
            stmt = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarTipoCliente()}");
            resultado = stmt.executeQuery();

            while (resultado.next()) {
                listado.add(new TipoCliente(resultado.getInt("id"),
                        resultado.getString("descripcion")));
            }

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

        listaTipoCliente = FXCollections.observableArrayList(listado);
        return listaTipoCliente;
    }

    public void cargarDatos() {
        tblTipoCliente.setItems(getTipoCliente());
        colId.setCellValueFactory(new PropertyValueFactory<TipoCliente, Integer>("id"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<TipoCliente, String>("descripcion"));
    }

    public boolean existeElementoSeleccionado() {
        if (tblTipoCliente.getSelectionModel().getSelectedItem() == null) {
            return false;
        } else {
            return true;
        }
    }

    public void agregarTipoCliente() {

        TipoCliente registro = new TipoCliente();
        registro.setDescripcion(txtDescripcion.getText());

        try {

            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarTipoCliente(?)}");
            stmt.setString(1, registro.getDescripcion());

            stmt.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void eliminarTipoCliente() {
        try {
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarTipoCliente(?)}");
            stmt.setInt(1, ((TipoCliente) tblTipoCliente.getSelectionModel().getSelectedItem()).getId());
            stmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void editarTipoCliente() {

        ArrayList<TextField> listaTetxField = new ArrayList<>();
        listaTetxField.add(txtId);
        listaTetxField.add(txtDescripcion);

        if (escenarioPrincipal.validarDatos(listaTetxField)) {

            TipoCliente registro = (TipoCliente) tblTipoCliente.getSelectionModel().getSelectedItem();
            registro.setId(Integer.parseInt(txtId.getText()));
            registro.setDescripcion(txtDescripcion.getText());

            try {
                PreparedStatement stmt;
                stmt = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarTipoCliente(?,?)}");
                stmt.setInt(1, registro.getId());
                stmt.setString(2, registro.getDescripcion());
                stmt.execute();
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
    private void seleccionarElemento() {
        if (existeElementoSeleccionado()) {
            txtId.setText(String.valueOf(((TipoCliente) tblTipoCliente.getSelectionModel().getSelectedItem()).getId()));
            txtDescripcion.setText(((TipoCliente) tblTipoCliente.getSelectionModel().getSelectedItem()).getDescripcion());
        }
    }

    private void habilitarCampos() {
        txtId.setEditable(false);
        txtDescripcion.setEditable(true);
    }

    private void deshabilitarCampos() {
        txtId.setEditable(false);
        txtDescripcion.setEditable(false);
    }

    private void limpiarCampos() {
        txtId.clear();
        txtDescripcion.clear();
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
                if (txtDescripcion.getText().length() != 0) {
                    agregarTipoCliente();
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
                    alert.setContentText("Debe rellenar los campos correspondientes");
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
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image("/org/carlosalvarez/resource/images/nuevo.png"));
                imgEliminar.setImage(new Image("/org/carlosalvarez/resource/images/eliminar.png"));
                limpiarCampos();
                deshabilitarCampos();
                operacion = Operaciones.NINGUNO;
                break;
            case NINGUNO://Elimanación
                if (tblTipoCliente.getSelectionModel().getSelectedItem() != null) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setHeaderText(null);
                    alert.setTitle("Confirmación");
                    alert.setContentText("¿Estas seguro de eliminar el elemento seleccionado?");
                    Optional<ButtonType> result = alert.showAndWait();

                    if (result.get() == ButtonType.OK) {
                        try {
                            eliminarTipoCliente();
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
                    alert.setContentText("Debe seleccionar un elemnto");
                    alert.showAndWait();
                }
        }
    }

    @FXML
    private void editar(ActionEvent event) {

        switch (operacion) {
            case NINGUNO:
                if (tblTipoCliente.getSelectionModel().getSelectedItem() != null) {
                    habilitarCampos();
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    imgEditar.setImage(new Image("/org/carlosalvarez/resource/images/actualizar.png"));
                    imgReporte.setImage(new Image("/org/carlosalvarez/resource/images/cancelar.png"));
                    btntNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    operacion = Operaciones.ACTUALIZAR;
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setHeaderText(null);
                    alert.setTitle("KINAL MALL");
                    alert.setContentText("Debe seleccionar un elemnto");
                    alert.showAndWait();
                }
                break;
            case ACTUALIZAR:
                editarTipoCliente();
                limpiarCampos();
                cargarDatos();
                deshabilitarCampos();
                imgEditar.setImage(new Image("/org/carlosalvarez/resource/images/editar.png"));
                imgReporte.setImage(new Image("/org/carlosalvarez/resource/images/reporte.png"));
                btntNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                operacion = Operaciones.NINGUNO;
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
                GenerarReporte.getInstance().mostrarReporte("ReporteTipoCliente.jasper", "Reporte Tipo Cliente", parametros);
        }
    }

    @FXML
    private void mostrarVistaMenuPrincipal(MouseEvent event) {
        this.escenarioPrincipal.mostrarAdministracion();
    }

}
