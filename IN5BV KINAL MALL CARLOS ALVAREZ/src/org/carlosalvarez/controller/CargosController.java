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
import javafx.scene.image.ImageView;
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
import org.carlosalvarez.bean.Cargos;
import org.carlosalvarez.db.Conexion;
import org.carlosalvarez.report.GenerarReporte;
import org.carlosalvarez.system.Principal;

/**
 *
 * @author Carlos Adolfo Alvarez Crúz
 * @date 10 jul 2021
 * @time 15:26:14 Codigo tecnico: IN5BV
 */
public class CargosController implements Initializable {

    private Principal escenarioPrincipal;

    private enum Operaciones {
        NUEVO, GUARDAR, EDITAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO
    }

    private Operaciones operacion = Operaciones.NINGUNO;

    private ObservableList<Cargos> listaCargos;

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
    private TableView tblCargos;
    @FXML
    private TableColumn colId;
    @FXML
    private TableColumn colNombre;
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtNombre;

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatos();
    }

    public ObservableList<Cargos> getCargos() {
        ArrayList<Cargos> listado = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarCargos()}");
            rs = pstmt.executeQuery();

            while (rs.next()) {
                listado.add(new Cargos(rs.getInt("id"), rs.getString("nombre")));
            }

            listaCargos = FXCollections.observableArrayList(listado);

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return listaCargos;
    }

    public void cargarDatos() {
        tblCargos.setItems(getCargos());
        colId.setCellValueFactory(new PropertyValueFactory<Cargos, Integer>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Cargos, String>("nombre"));
    }

    public boolean existeElementoSeleccionado() {
        if (tblCargos.getSelectionModel().getSelectedItem() == null) {
            return false;
        } else {
            return true;
        }
    }

    public void agregarCargos() {
        Cargos registro = new Cargos();
        registro.setNombre(txtNombre.getText());

        try {

            PreparedStatement pstmt;
            pstmt = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarCargos(?)}");
            pstmt.setString(1, registro.getNombre());
            pstmt.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void eliminarCargos() {
        try {
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarCargos(?)}");
            stmt.setInt(1, ((Cargos) tblCargos.getSelectionModel().getSelectedItem()).getId());
            stmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void editarCargos() {
        ArrayList<TextField> listaTexfield = new ArrayList<>();
        listaTexfield.add(txtId);
        listaTexfield.add(txtNombre);

        if (escenarioPrincipal.validarDatos(listaTexfield)) {
            Cargos registro = (Cargos) tblCargos.getSelectionModel().getSelectedItem();
            registro.setId(Integer.parseInt(txtId.getText()));
            registro.setNombre(txtNombre.getText());
            try {
                PreparedStatement stmt;
                stmt = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarCargos(?,?)}");
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
            txtId.setText(String.valueOf(((Cargos) tblCargos.getSelectionModel().getSelectedItem()).getId()));
            txtNombre.setText(((Cargos) tblCargos.getSelectionModel().getSelectedItem()).getNombre());
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
                    agregarCargos();
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
                if (tblCargos.getSelectionModel().getSelectedItem() != null) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setHeaderText(null);
                    alert.setTitle("Confirmación");
                    alert.setContentText("¿Estas seguro de eliminar el elemento seleccionado?");
                    Optional<ButtonType> result = alert.showAndWait();

                    if (result.get() == ButtonType.OK) {
                        try {
                            eliminarCargos();
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
                if (tblCargos.getSelectionModel().getSelectedItem() != null) {
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
                editarCargos();
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
                GenerarReporte.getInstance().mostrarReporte("ReporteCargos.jasper", "Reporte Cargos", parametros);
        }
    }

    @FXML
    private void mostrarVistaMenuPrincipal(MouseEvent event) {
        this.escenarioPrincipal.mostrarAdministracion();
    }

}
