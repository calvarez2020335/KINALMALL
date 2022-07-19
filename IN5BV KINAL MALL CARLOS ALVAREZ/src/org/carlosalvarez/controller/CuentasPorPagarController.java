/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.carlosalvarez.controller;

import com.jfoenix.controls.JFXDatePicker;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import org.carlosalvarez.bean.Administracion;
import org.carlosalvarez.bean.CuentasPorPagar;
import org.carlosalvarez.bean.Proveedores;
import org.carlosalvarez.db.Conexion;
import org.carlosalvarez.report.GenerarReporte;
import org.carlosalvarez.system.Principal;

/**
 *
 * @author Carlos Adolfo Alvarez Crúz
 * @date 25 jun 2021
 * @time 16:20:05 Codigo tecnico: IN5BV
 */
public class CuentasPorPagarController implements Initializable {

    private Principal escenarioPrincipal;

    private final String PAQUETE_IMAGE = "/org/carlosalvarez/resource/images/";

    @FXML
    private JFXDatePicker dpFechaLimite;
    @FXML
    private ComboBox cmbEstadoPago;

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    private enum Operaciones {
        NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO;
    }

    private Operaciones operacion = Operaciones.NINGUNO;

    private ObservableList<CuentasPorPagar> listaCuentasPorPagar;
    private ObservableList<Administracion> listaAdministracion;
    private ObservableList<Proveedores> listaProveedores;

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
    private TextField txtNumeroFactura;
    @FXML
    private TableView tblCuentasPorPagar;
    @FXML
    private TableColumn colId;
    @FXML
    private TableColumn colNumeroFactura;
    @FXML
    private TableColumn colFechaLimitePago;
    @FXML
    private TableColumn colEstadoPago;
    @FXML
    private TableColumn colValorNeto;
    @FXML
    private TableColumn colIdAdministracion;
    @FXML
    private TableColumn colIdProveedor;
    @FXML
    private TextField txtValorNetoPago;
    @FXML
    private ComboBox cmbProveedor;
    @FXML
    private ComboBox cmbAdministracion;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatos();
        dpFechaLimite.setDisable(true);

        Locale locale = new Locale("es", "GT");
        Locale.setDefault(locale);
        
        ObservableList<String> listaOpciones = FXCollections.observableArrayList("PENDIENTE",  "CANCELADO");
        
        cmbEstadoPago.getItems().addAll(listaOpciones);

    }

    public ObservableList<CuentasPorPagar> getCuenteasPorPagar() {

        ArrayList<CuentasPorPagar> listado = new ArrayList<>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {

            pstmt = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarCuentasPorPagar()}");
            rs = pstmt.executeQuery();

            while (rs.next()) {
                listado.add(new CuentasPorPagar(rs.getInt("id"),
                        rs.getString("numeroFactura"),
                        rs.getDate("fechaLimitePago"),
                        rs.getString("estadoPago"),
                        rs.getBigDecimal("valorNetoPago"),
                        rs.getInt("idAdministracion"),
                        rs.getInt("idProveedor")));
            }
            listaCuentasPorPagar = FXCollections.observableArrayList(listado);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                pstmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return listaCuentasPorPagar;
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

    private Administracion buscarAdministracion(int id) {

        Administracion administracion = null;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {

            pstmt = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarAdministracion(?)}");
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                administracion = new Administracion(
                        rs.getInt("id"),
                        rs.getString("direccion"),
                        rs.getString("telefono")
                );

            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            try {
                rs.close();
                pstmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return administracion;
    }

    private Proveedores buscarProveedores(int id) {

        Proveedores proveedores = null;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {

            pstmt = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarProveedores(?)}");
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                proveedores = new Proveedores(
                        rs.getInt("id"),
                        rs.getString("nit"),
                        rs.getString("servicioPrestado"),
                        rs.getString("telefono"),
                        rs.getString("direccion"),
                        rs.getBigDecimal("saldoFavor"),
                        rs.getBigDecimal("saldoContra")
                );

            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            try {
                rs.close();
                pstmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return proveedores;
    }

    public boolean existeElementoSeleccionado() {
        if (tblCuentasPorPagar.getSelectionModel().getSelectedItem() == null) {
            return false;
        } else {
            return true;
        }
    }

    @FXML
    private void seleccionarElementos() {

        if (existeElementoSeleccionado()) {
            txtId.setText(String.valueOf(((CuentasPorPagar) tblCuentasPorPagar.getSelectionModel().getSelectedItem()).getId()));
            //txtEstadoPago.setText(String.valueOf(((CuentasPorPagar) tblCuentasPorPagar.getSelectionModel().getSelectedItem()).getEstadoPago()));
            //txtFechaLimiteDePago.setText(String.valueOf(((CuentasPorPagar) tblCuentasPorPagar.getSelectionModel().getSelectedItem()).getFechaLimitePago()));
            cmbEstadoPago.setValue(((CuentasPorPagar) tblCuentasPorPagar.getSelectionModel().getSelectedItem()).getEstadoPago());
            dpFechaLimite.setValue(((CuentasPorPagar) tblCuentasPorPagar.getSelectionModel().getSelectedItem()).getFechaLimitePago().toLocalDate());
            txtNumeroFactura.setText(String.valueOf(((CuentasPorPagar) tblCuentasPorPagar.getSelectionModel().getSelectedItem()).getNumeroFactura()));
            txtValorNetoPago.setText(String.valueOf(((CuentasPorPagar) tblCuentasPorPagar.getSelectionModel().getSelectedItem()).getValorNetoPago()));
            cmbAdministracion.getSelectionModel().select(buscarAdministracion(((CuentasPorPagar) tblCuentasPorPagar.getSelectionModel().getSelectedItem()).getIdAdministracion()));
            cmbProveedor.getSelectionModel().select(buscarProveedores(((CuentasPorPagar) tblCuentasPorPagar.getSelectionModel().getSelectedItem()).getIdProveedor()));

        }

    }

    @FXML
    private void mostrarVistaMenuPrincipal(MouseEvent event) {
        escenarioPrincipal.mostrarProveedores();
    }

    public void agregarCuentasPorPagar() {

        CuentasPorPagar registro = new CuentasPorPagar();

        registro.setNumeroFactura(txtNumeroFactura.getText());
        registro.setFechaLimitePago(java.sql.Date.valueOf(dpFechaLimite.getValue()));
        //registro.setEstadoPago(txtEstadoPago.getText());
        registro.setEstadoPago(cmbEstadoPago.getValue().toString());
        BigDecimal b = new BigDecimal(txtValorNetoPago.getText());
        registro.setValorNetoPago(b);

        registro.setIdAdministracion(((Administracion) cmbAdministracion.getSelectionModel().getSelectedItem()).getId());
        registro.setIdProveedor(((Proveedores) cmbProveedor.getSelectionModel().getSelectedItem()).getId());

        try {
            PreparedStatement pstmt;

            pstmt = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarCuentasPorPagar(?,?,?,?,?,?)}");

            pstmt.setString(1, registro.getNumeroFactura());
            pstmt.setDate(2, registro.getFechaLimitePago());
            pstmt.setString(3, registro.getEstadoPago());
            pstmt.setBigDecimal(4, registro.getValorNetoPago());
            pstmt.setInt(5, registro.getIdAdministracion());
            pstmt.setInt(6, registro.getIdProveedor());

            pstmt.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eliminarCuentasPorPagar() {

        CuentasPorPagar cuentasPorPagar = ((CuentasPorPagar) tblCuentasPorPagar.getSelectionModel().getSelectedItem());

        try {

            PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarCuentasPorPagar(?)}");
            pstmt.setInt(1, cuentasPorPagar.getId());
            pstmt.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void editarCuentasPorPagar() {

        ArrayList<TextField> listaTexfield = new ArrayList<>();

        listaTexfield.add(txtId);
        //listaTexfield.add(txtEstadoPago);
        // listaTexfield.add(txtFechaLimiteDePago);
        listaTexfield.add(txtNumeroFactura);
        listaTexfield.add(txtValorNetoPago);

        ArrayList<ComboBox> listaComboBox = new ArrayList<>();

        listaComboBox.add(cmbEstadoPago);
        listaComboBox.add(cmbProveedor);
        listaComboBox.add(cmbProveedor);

        if (escenarioPrincipal.validar(listaTexfield, listaComboBox)) {

            CuentasPorPagar registro = new CuentasPorPagar();

            registro.setId(Integer.parseInt(txtId.getText()));
            registro.setNumeroFactura(txtNumeroFactura.getText());
            registro.setFechaLimitePago(java.sql.Date.valueOf(dpFechaLimite.getValue()));
            //registro.setFechaLimitePago(java.sql.Date.valueOf(txtFechaLimiteDePago.getText()));
            //registro.setEstadoPago(txtEstadoPago.getText());
            registro.setEstadoPago(cmbEstadoPago.getValue().toString());
            BigDecimal b = new BigDecimal(txtValorNetoPago.getText());
            registro.setValorNetoPago(b);

            registro.setIdAdministracion(((Administracion) cmbAdministracion.getSelectionModel().getSelectedItem()).getId());
            registro.setIdProveedor(((Proveedores) cmbProveedor.getSelectionModel().getSelectedItem()).getId());

            try {

                PreparedStatement pstmt;

                pstmt = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarCuentasPorPagar(?,?,?,?,?,?,?)}");

                pstmt.setInt(1, registro.getId());
                pstmt.setString(2, registro.getNumeroFactura());
                pstmt.setDate(3, registro.getFechaLimitePago());
                pstmt.setString(4, registro.getEstadoPago());
                pstmt.setBigDecimal(5, registro.getValorNetoPago());
                pstmt.setInt(6, registro.getIdAdministracion());
                pstmt.setInt(7, registro.getIdProveedor());
                pstmt.execute();

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setTitle("KINAL MALL");
            alert.setContentText("Faltan llenar datos");
            alert.showAndWait();

        }

    }

    public void cargarDatos() {

        tblCuentasPorPagar.setItems(getCuenteasPorPagar());
        colId.setCellValueFactory(new PropertyValueFactory<CuentasPorPagar, Integer>("id"));
        colNumeroFactura.setCellValueFactory(new PropertyValueFactory<CuentasPorPagar, String>("numeroFactura"));
        colFechaLimitePago.setCellValueFactory(new PropertyValueFactory<CuentasPorPagar, Date>("fechaLimitePago"));

        colEstadoPago.setCellValueFactory(new PropertyValueFactory<CuentasPorPagar, String>("estadoPago"));
        colValorNeto.setCellValueFactory(new PropertyValueFactory<CuentasPorPagar, BigDecimal>("valorNetoPago"));
        colIdAdministracion.setCellValueFactory(new PropertyValueFactory<CuentasPorPagar, Integer>("idAdministracion"));
        colIdProveedor.setCellValueFactory(new PropertyValueFactory<CuentasPorPagar, Integer>("idProveedor"));

        cmbAdministracion.setItems(getAdministracion());
        cmbProveedor.setItems(getProveedores());
    }

    private void habilitarCampos() {

        txtId.setEditable(false);
        //txtEstadoPago.setEditable(true);
        // txtFechaLimiteDePago.setEditable(true);
        dpFechaLimite.setDisable(false);
        txtNumeroFactura.setEditable(true);
        txtValorNetoPago.setEditable(true);

        cmbEstadoPago.setDisable(false);
        cmbAdministracion.setDisable(false);
        cmbProveedor.setDisable(false);

    }

    private void deshabilitarCampos() {

        txtId.setEditable(false);
       // txtEstadoPago.setEditable(false);
        // txtFechaLimiteDePago.setEditable(false);
        txtNumeroFactura.setEditable(false);
        txtValorNetoPago.setEditable(false);
        dpFechaLimite.setDisable(true);
        
        cmbEstadoPago.setDisable(true);
        cmbAdministracion.setDisable(true);
        cmbProveedor.setDisable(true);
    }

    private void limpiarCampos() {

        //txtEstadoPago.clear();
        // txtFechaLimiteDePago.clear();
        txtId.clear();
        txtNumeroFactura.clear();
        txtValorNetoPago.clear();
        dpFechaLimite.getEditor().clear();
        dpFechaLimite.setValue(null);
        
        cmbEstadoPago.valueProperty().setValue(null);
        cmbAdministracion.valueProperty().setValue(null);
        cmbProveedor.valueProperty().setValue(null);

    }

    public boolean ValidarDecimal(String numero) {
        Pattern pattern = Pattern.compile("^[0-9]{1,8}+([.][0-9]{1,2})?$");
        Matcher matcher = pattern.matcher(numero);
        return matcher.matches();
    }

    @FXML
    private void nuevo(ActionEvent event) {

        switch (operacion) {
            case NINGUNO:
                limpiarCampos();
                habilitarCampos();
                btntNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                imgNuevo.setImage(new Image(PAQUETE_IMAGE + "guardar.png"));
                imgEliminar.setImage(new Image(PAQUETE_IMAGE + "cancelar.png"));
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                operacion = Operaciones.GUARDAR;
                break;
            case GUARDAR:

                ArrayList<TextField> listadoTextFiel = new ArrayList<>();

                listadoTextFiel.add(txtNumeroFactura);
                listadoTextFiel.add(txtValorNetoPago);

                ArrayList<ComboBox> listaComboBox = new ArrayList<>();
                
                listaComboBox.add(cmbEstadoPago);
                listaComboBox.add(cmbAdministracion);
                listaComboBox.add(cmbProveedor);
                if (escenarioPrincipal.validar(listadoTextFiel, listaComboBox)) {
                    if (ValidarDecimal(txtValorNetoPago.getText())) {
                        if (dpFechaLimite.getValue() != null) {

                            agregarCuentasPorPagar();
                            cargarDatos();
                            deshabilitarCampos();
                            limpiarCampos();
                            btntNuevo.setText("Nuevo");
                            btnEliminar.setText("Eliminar");
                            imgNuevo.setImage(new Image(PAQUETE_IMAGE + "nuevo.png"));
                            imgEliminar.setImage(new Image(PAQUETE_IMAGE + "eliminar.png"));
                            btnEditar.setDisable(false);
                            btnReporte.setDisable(false);
                            operacion = Operaciones.NINGUNO;
                        } else {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setHeaderText(null);
                            alert.setTitle("KINAL MALL");
                            alert.setContentText("Debe seleccionar una fecha");
                            alert.showAndWait();
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setHeaderText(null);
                        alert.setTitle("KINAL MALL");
                        alert.setContentText("El valor numerico ingresado es invalido");
                        alert.showAndWait();
                    }
                } else {

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setTitle("KINAL MALL");
                    alert.setContentText("Falta llenar Datos");
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
                imgNuevo.setImage(new Image(PAQUETE_IMAGE + "nuevo.png"));
                imgEliminar.setImage(new Image(PAQUETE_IMAGE + "eliminar.png"));
                limpiarCampos();
                deshabilitarCampos();
                operacion = Operaciones.NINGUNO;
                break;
            case NINGUNO://Elimanación

                if (tblCuentasPorPagar.getSelectionModel().getSelectedItem() != null) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setHeaderText(null);
                    alert.setTitle("Confirmación");
                    alert.setContentText("¿Estas seguro de eliminar el elemento seleccionado?");
                    Optional<ButtonType> result = alert.showAndWait();

                    if (result.get() == ButtonType.OK) {
                        try {
                            eliminarCuentasPorPagar();
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
                if (tblCuentasPorPagar.getSelectionModel().getSelectedItem() != null) {
                    habilitarCampos();
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    imgEditar.setImage(new Image(PAQUETE_IMAGE + "actualizar.png"));
                    imgReporte.setImage(new Image(PAQUETE_IMAGE + "cancelar.png"));
                    btntNuevo.setDisable(true);
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
                if (ValidarDecimal(txtValorNetoPago.getText())) {
                    if (dpFechaLimite.getValue() != null) {
                        editarCuentasPorPagar();
                        limpiarCampos();
                        cargarDatos();
                        deshabilitarCampos();
                        btntNuevo.setDisable(false);
                        btnEliminar.setDisable(false);
                        imgEditar.setImage(new Image(PAQUETE_IMAGE + "editar.png"));
                        imgReporte.setImage(new Image(PAQUETE_IMAGE + "reporte.png"));
                        btnEditar.setText("Editar");
                        btnReporte.setText("Reporte");
                        operacion = Operaciones.NINGUNO;
                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setHeaderText(null);
                        alert.setTitle("KINAL MALL");
                        alert.setContentText("Debe seleccionar un elemento");
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
                btnEditar.setDisable(false);
                imgEditar.setImage(new Image(PAQUETE_IMAGE + "editar.png"));
                imgReporte.setImage(new Image(PAQUETE_IMAGE + "reporte.png"));
                btnReporte.setDisable(false);
                btntNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                cargarDatos();
                limpiarCampos();
                deshabilitarCampos();
                operacion = Operaciones.NINGUNO;
                break;
            case NINGUNO:
                Map parametros = new HashMap();
                GenerarReporte.getInstance().mostrarReporte("ReporteCuenteasPorPagar.jasper", "Reporte Cuentas por Pagar", parametros);
                break;
        }
    }
}
