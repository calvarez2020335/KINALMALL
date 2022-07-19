/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.carlosalvarez.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.carlosalvarez.bean.Departamentos;
import org.carlosalvarez.db.Conexion;
import org.carlosalvarez.report.GenerarReporte;
import org.carlosalvarez.system.Principal;

/**
 *
 * @author Carlos Adolfo Alvarez Crúz
 * @date 14 jun 2021
 * @time 8:07:21 Codigo tecnico: IN5BV
 */
public class DepartamentosController implements Initializable {

    private Principal escenarioPrincipal;

    private ObservableList<Departamentos> listaDepartamentos;
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
    private TextField txtNombre;
    @FXML
    private TableView tblDepartamentos;
    @FXML
    private TableColumn colId;
    @FXML
    private TableColumn colNombre;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargaDatos();
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    @FXML
    private void mostrarVistaMenuPrincipal(MouseEvent event) {
        this.escenarioPrincipal.mostrarAdministracion();
    }

    public ObservableList<Departamentos> getDepartamentos() {

        ArrayList<Departamentos> listado = new ArrayList<Departamentos>();
        PreparedStatement stmt = null;
        ResultSet resultado = null;

        try {

            
            stmt = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarDepartamentos()}");
            resultado = stmt.executeQuery();

            while (resultado.next()) {
                listado.add(new Departamentos(resultado.getInt("id"),
                        resultado.getString("nombre")));
            }
            listaDepartamentos = FXCollections.observableArrayList(listado);

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

        return listaDepartamentos;
    }

    public void cargaDatos() {
        tblDepartamentos.setItems(getDepartamentos());
        colId.setCellValueFactory(new PropertyValueFactory<Departamentos, Integer>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Departamentos, String>("nombre"));
    }

    public boolean existeElementoSeleccionado() {
        if (tblDepartamentos.getSelectionModel().getSelectedItem() == null) {
            return false;
        } else {
            return true;
        }
    }

    private void agregarDepartamentos() {

        Departamentos registro = new Departamentos();
        registro.setNombre(txtNombre.getText());

        try {

            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarDepartamentos(?)}");
            stmt.setString(1, registro.getNombre());
            stmt.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void eliminarDepartamentos() {
        try {
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarDepartamentos(?)}");
            stmt.setInt(1, ((Departamentos) tblDepartamentos.getSelectionModel().getSelectedItem()).getId());
            stmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void editarDepartamentos() {

        ArrayList<TextField> listaTetxField = new ArrayList<>();
        listaTetxField.add(txtId);
        listaTetxField.add(txtNombre);

        if (escenarioPrincipal.validarDatos(listaTetxField)) {

            Departamentos registro = (Departamentos) tblDepartamentos.getSelectionModel().getSelectedItem();
            registro.setId(Integer.parseInt(txtId.getText()));
            registro.setNombre(txtNombre.getText());

            try {
                PreparedStatement stmt;
                stmt = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarDepartamentos(?,?)}");
                stmt.setInt(1, registro.getId());
                stmt.setString(2, registro.getNombre());
                stmt.execute();
                System.out.println(stmt.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setTitle("KINAL MALL");
            alert.setContentText("Debe rellenar los campos correspondientes");
            alert.showAndWait();
        }
    }

    @FXML
    private void seleccionarElemento() {
        if (existeElementoSeleccionado()) {
            txtId.setText(String.valueOf(((Departamentos) tblDepartamentos.getSelectionModel().getSelectedItem()).getId()));
            txtNombre.setText(((Departamentos) tblDepartamentos.getSelectionModel().getSelectedItem()).getNombre());
        }
    }

    private void habilitarCampos() {
        txtId.setEditable(false);
        txtNombre.setEditable(true);
    }

    private void deshabilitarCampos() {
        txtId.setEditable(false);
        txtNombre.setEditable(false);
    }

    private void limpiarCampos() {
        txtId.clear();
        txtNombre.clear();
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
                if (txtNombre.getText().length() != 0) {
                    agregarDepartamentos();
                    cargaDatos();
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
                if (tblDepartamentos.getSelectionModel().getSelectedItem() != null) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setHeaderText(null);
                    alert.setTitle("Confirmación");
                    alert.setContentText("¿Estas seguro de eliminar el elemento seleccionado?");
                    Optional<ButtonType> result = alert.showAndWait();

                    if (result.get() == ButtonType.OK) {
                        try {
                            eliminarDepartamentos();
                            limpiarCampos();
                            cargaDatos();
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
                if (tblDepartamentos.getSelectionModel().getSelectedItem() != null) {
                    habilitarCampos();
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btntNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    imgEditar.setImage(new Image("/org/carlosalvarez/resource/images/actualizar.png"));
                    imgReporte.setImage(new Image("/org/carlosalvarez/resource/images/cancelar.png"));
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
                editarDepartamentos();
                limpiarCampos();
                cargaDatos();
                deshabilitarCampos();
                btntNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                imgEditar.setImage(new Image("/org/carlosalvarez/resource/images/editar.png"));
                imgReporte.setImage(new Image("/org/carlosalvarez/resource/images/reporte.png"));
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
                GenerarReporte.getInstance().mostrarReporte("ReporteDepartamentos.jasper", "Reporte Departamentos", parametros);
        }
    }

}
