/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.carlosalvarez.controller;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.carlosalvarez.bean.Administracion;
import org.carlosalvarez.bean.Clientes;
import org.carlosalvarez.bean.CuentasPorCobrar;
import org.carlosalvarez.bean.Locales;
import org.carlosalvarez.db.Conexion;
import org.carlosalvarez.report.GenerarReporte;
import org.carlosalvarez.system.Principal;

/**
 *
 * @author Carlos Adolfo Alvarez Crúz
 * @date 30 jun 2021
 * @time 14:39:05 Codigo tecnico: IN5BV
 */
public class CuentasPorCobrarController implements Initializable {

    private Principal escenarioPrincipal;
    @FXML
    private ComboBox cmbEstadoPago;

    private enum Operaciones {
        NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO;
    }

    private final String PAQUETE_IMAGE = "/org/carlosalvarez/resource/images/";

    private Operaciones operacion = Operaciones.NINGUNO;

    private ObservableList<CuentasPorCobrar> listadoCuentasPorCobrar;
    private ObservableList<Administracion> listaAdministracion;
    private ObservableList<Clientes> listaClientes;
    private ObservableList<Locales> listaLocales;

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
    private TableView tblCuentasPorCobrar;
    @FXML
    private TableColumn colId;
    @FXML
    private TableColumn colNumeroFactura;
    @FXML
    private TableColumn colAnio;
    @FXML
    private TableColumn colMes;
    @FXML
    private TableColumn colValorNeto;
    @FXML
    private TableColumn colEstadoPagp;
    @FXML
    private TableColumn colIdAdministracion;
    @FXML
    private TableColumn colIdCliente;
    @FXML
    private TableColumn colIdLocal;
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtNumeroFactura;
    @FXML
    private TextField txtValorNetoPago;
    @FXML
    private ComboBox cmbAdministracion;
    @FXML
    private ComboBox cmbCliente;
    @FXML
    private ComboBox cmbLocal;

    @FXML
    private Spinner<Integer> spnAnio;

    private SpinnerValueFactory<Integer> valueFactoryAnio;

    @FXML
    private Spinner<Integer> spnMes;

    private SpinnerValueFactory<Integer> valueFactoryMes;

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        cargarDatos();

        valueFactoryAnio = new SpinnerValueFactory.IntegerSpinnerValueFactory(2020, 2050, 2021);
        spnAnio.setValueFactory(valueFactoryAnio);

        valueFactoryMes = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 12, 1);
        spnMes.setValueFactory(valueFactoryMes);

        ObservableList<String> listaOpciones = FXCollections.observableArrayList("PENDIENTE", "CANCELADO");

        cmbEstadoPago.getItems().addAll(listaOpciones);
    }

    public ObservableList<CuentasPorCobrar> getCuentasPorCobrar() {

        ArrayList<CuentasPorCobrar> listado = new ArrayList<>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {

            pstmt = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarCuentasPorCobrar()}");
            rs = pstmt.executeQuery();

            while (rs.next()) {
                listado.add(new CuentasPorCobrar(rs.getInt("id"),
                        rs.getString("numeroFactura"),
                        rs.getInt("anio"),
                        rs.getInt("mes"),
                        rs.getBigDecimal("valorNetoPago"),
                        rs.getString("estadoPago"),
                        rs.getInt("idAdministracion"),
                        rs.getInt("idCliente"),
                        rs.getInt("idLocal")));
            }
            listadoCuentasPorCobrar = FXCollections.observableArrayList(listado);

        } catch (SQLException e) {
            System.out.println("Error al intentar al consltar la tabla cuentas por cobrar");
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

        return listadoCuentasPorCobrar;

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

    public ObservableList<Clientes> getClientes() {
        ArrayList<Clientes> lista = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarClientes()}");
            rs = pstmt.executeQuery();

            while (rs.next()) {
                lista.add(new Clientes(rs.getInt("id"),
                        rs.getString("nombres"),
                        rs.getString("apellidos"),
                        rs.getString("telefono"),
                        rs.getString("direccion"),
                        rs.getString("email"),
                        rs.getInt("idTipoCliente")));
            }
            listaClientes = FXCollections.observableArrayList(lista);

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
        return listaClientes;
    }

    public ObservableList<Locales> getLocales() {

        ArrayList<Locales> lista = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarLocales()}");
            rs = pstmt.executeQuery();

            while (rs.next()) {
                lista.add(new Locales(rs.getInt("id"),
                        rs.getBigDecimal("saldoFavor"),
                        rs.getBigDecimal("saldoContra"),
                        rs.getInt("mesesPendientes"),
                        rs.getBoolean("disponibilidad"),
                        rs.getBigDecimal("valorLocal"),
                        rs.getBigDecimal("valorAdministracion")));
            }
            listaLocales = FXCollections.observableArrayList(lista);

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
        return listaLocales;
    }

    public boolean existeElemetoSeleccionado() {

        return tblCuentasPorCobrar.getSelectionModel().getSelectedItem() != null;

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

    private Clientes buscaCliente(int id) {
        Clientes registro = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarClientes(?)}");
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                registro = new Clientes(
                        rs.getInt("id"),
                        rs.getString("nombres"),
                        rs.getString("apellidos"),
                        rs.getString("telefono"),
                        rs.getString("direccion"),
                        rs.getString("email"),
                        rs.getInt("idTipoCliente"));
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

        return registro;
    }

    private Locales buscarLocales(int id) {
        Locales local = null;
        PreparedStatement pstmt = null;
        ResultSet resultado = null;

        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarLocales(?)}");
            pstmt.setInt(1, id);
            resultado = pstmt.executeQuery();

            while (resultado.next()) {
                local = new Locales(resultado.getInt("id"),
                        resultado.getBigDecimal("saldoFavor"),
                        resultado.getBigDecimal("saldoContra"),
                        resultado.getInt("mesesPendientes"),
                        resultado.getBoolean("disponibilidad"),
                        resultado.getBigDecimal("valorLocal"),
                        resultado.getBigDecimal("valorAdministracion"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                resultado.close();
                pstmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return local;
    }

    @FXML
    private void seleccionarElementos() {

        if (existeElemetoSeleccionado()) {
            txtId.setText(String.valueOf(((CuentasPorCobrar) tblCuentasPorCobrar.getSelectionModel().getSelectedItem()).getId()));
            txtNumeroFactura.setText(((CuentasPorCobrar) tblCuentasPorCobrar.getSelectionModel().getSelectedItem()).getNumeroFactura());
            spnAnio.getValueFactory().setValue(((CuentasPorCobrar) tblCuentasPorCobrar.getSelectionModel().getSelectedItem()).getAnio());
            spnMes.getValueFactory().setValue(((CuentasPorCobrar) tblCuentasPorCobrar.getSelectionModel().getSelectedItem()).getMes());
            txtValorNetoPago.setText(String.valueOf(((CuentasPorCobrar) tblCuentasPorCobrar.getSelectionModel().getSelectedItem()).getValorNetoPago()));
            // txtEstadoPago.setText(((CuentasPorCobrar) tblCuentasPorCobrar.getSelectionModel().getSelectedItem()).getEstadoPago());

            cmbEstadoPago.setValue(((CuentasPorCobrar) tblCuentasPorCobrar.getSelectionModel().getSelectedItem()).getEstadoPago());
            cmbAdministracion.getSelectionModel().select(buscarAdministracion(((CuentasPorCobrar) tblCuentasPorCobrar.getSelectionModel().getSelectedItem()).getIdAdministracion()));
            cmbCliente.getSelectionModel().select(buscaCliente(((CuentasPorCobrar) tblCuentasPorCobrar.getSelectionModel().getSelectedItem()).getIdCliente()));
            cmbLocal.getSelectionModel().select(((CuentasPorCobrar) tblCuentasPorCobrar.getSelectionModel().getSelectedItem()).getIdLocal());
        }
    }

    public void agregarCuentasPorCobrar() {
        CuentasPorCobrar registro = new CuentasPorCobrar();

        registro.setNumeroFactura(txtNumeroFactura.getText());
        registro.setAnio(spnAnio.getValue());
        registro.setMes(spnMes.getValue());
        BigDecimal a = new BigDecimal(txtValorNetoPago.getText());
        registro.setValorNetoPago(a);
        registro.setEstadoPago(cmbEstadoPago.getValue().toString());
        registro.setIdAdministracion(((Administracion) cmbAdministracion.getSelectionModel().getSelectedItem()).getId());
        registro.setIdCliente(((Clientes) cmbCliente.getSelectionModel().getSelectedItem()).getId());
        registro.setIdLocal(((Locales) cmbLocal.getSelectionModel().getSelectedItem()).getId());

        try {

            PreparedStatement pstmt;

            pstmt = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarCuentasPorCobrar(?,?,?,?,?,?,?,?)}");
            pstmt.setString(1, registro.getNumeroFactura());
            pstmt.setInt(2, registro.getAnio());
            pstmt.setInt(3, registro.getMes());
            pstmt.setBigDecimal(4, registro.getValorNetoPago());
            pstmt.setString(5, registro.getEstadoPago());
            pstmt.setInt(6, registro.getIdAdministracion());
            pstmt.setInt(7, registro.getIdCliente());
            pstmt.setInt(8, registro.getIdLocal());
            pstmt.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void eliminarCuentasPorCobrar() {

        CuentasPorCobrar cuentasPorCobrar = ((CuentasPorCobrar) tblCuentasPorCobrar.getSelectionModel().getSelectedItem());

        try {

            PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarCuentasPorCobrar(?)}");
            pstmt.setInt(1, cuentasPorCobrar.getId());
            pstmt.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void editarCuentasPorCobrar() {

        ArrayList<TextField> listaTexfield = new ArrayList<>();

        listaTexfield.add(txtId);
        listaTexfield.add(txtNumeroFactura);
        listaTexfield.add(txtValorNetoPago);

        ArrayList<Spinner> listaSpinner = new ArrayList<>();

        listaSpinner.add(spnMes);
        listaSpinner.add(spnAnio);

        ArrayList<ComboBox> listaComboBox = new ArrayList<>();

        listaComboBox.add(cmbLocal);
        listaComboBox.add(cmbEstadoPago);
        listaComboBox.add(cmbAdministracion);
        listaComboBox.add(cmbCliente);

        if (escenarioPrincipal.validar3(listaTexfield, listaSpinner, listaComboBox)) {

            CuentasPorCobrar registro = new CuentasPorCobrar();

            registro.setId(Integer.parseInt(txtId.getText()));
            registro.setNumeroFactura(txtNumeroFactura.getText());
            registro.setAnio(spnAnio.getValue());
            registro.setMes(spnMes.getValue());
            BigDecimal a = new BigDecimal(txtValorNetoPago.getText());
            registro.setValorNetoPago(a);
            registro.setEstadoPago(cmbEstadoPago.getValue().toString());
            registro.setIdAdministracion(((Administracion) cmbAdministracion.getSelectionModel().getSelectedItem()).getId());
            registro.setIdCliente(((Clientes) cmbCliente.getSelectionModel().getSelectedItem()).getId());
            registro.setIdLocal(((Locales) cmbLocal.getSelectionModel().getSelectedItem()).getId());

            try {

                PreparedStatement pstmt;

                pstmt = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarCuentasPorCobrar(?,?,?,?,?,?,?,?,?)}");
                pstmt.setInt(1, registro.getId());
                pstmt.setString(2, registro.getNumeroFactura());
                pstmt.setInt(3, registro.getAnio());
                pstmt.setInt(4, registro.getMes());
                pstmt.setBigDecimal(5, registro.getValorNetoPago());
                pstmt.setString(6, registro.getEstadoPago());
                pstmt.setInt(7, registro.getIdAdministracion());
                pstmt.setInt(8, registro.getIdCliente());
                pstmt.setInt(9, registro.getIdLocal());
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

        tblCuentasPorCobrar.setItems(getCuentasPorCobrar());
        colId.setCellValueFactory(new PropertyValueFactory<CuentasPorCobrar, Integer>("id"));
        colNumeroFactura.setCellValueFactory(new PropertyValueFactory<CuentasPorCobrar, String>("numeroFactura"));
        colAnio.setCellValueFactory(new PropertyValueFactory<CuentasPorCobrar, Integer>("anio"));
        colMes.setCellValueFactory(new PropertyValueFactory<CuentasPorCobrar, Integer>("mes"));
        colValorNeto.setCellValueFactory(new PropertyValueFactory<CuentasPorCobrar, BigDecimal>("valorNetoPago"));
        colEstadoPagp.setCellValueFactory(new PropertyValueFactory<CuentasPorCobrar, String>("estadoPago"));
        colIdAdministracion.setCellValueFactory(new PropertyValueFactory<CuentasPorCobrar, Integer>("idAdministracion"));
        colIdCliente.setCellValueFactory(new PropertyValueFactory<CuentasPorCobrar, Integer>("idCliente"));
        colIdLocal.setCellValueFactory(new PropertyValueFactory<CuentasPorCobrar, Integer>("idLocal"));

        cmbAdministracion.setItems(getAdministracion());
        cmbCliente.setItems(getClientes());
        cmbLocal.setItems(getLocales());

    }

    private void habilitarCampos() {

        txtId.setEditable(false);

        txtValorNetoPago.setEditable(true);
        txtNumeroFactura.setEditable(true);

        spnAnio.setDisable(false);
        spnMes.setDisable(false);

        cmbEstadoPago.setDisable(false);
        cmbAdministracion.setDisable(false);
        cmbCliente.setDisable(false);
        cmbLocal.setDisable(false);

    }

    private void deshabilitarCampos() {

        txtId.setEditable(false);

        txtValorNetoPago.setEditable(false);
        txtNumeroFactura.setEditable(false);

        spnAnio.setDisable(true);
        spnMes.setDisable(true);

        cmbEstadoPago.setDisable(true);
        cmbAdministracion.setDisable(true);
        cmbCliente.setDisable(true);
        cmbLocal.setDisable(true);

    }

    private void limpiarCampos() {

        txtId.clear();

        txtNumeroFactura.clear();
        txtValorNetoPago.clear();

        spnAnio.getValueFactory().setValue(2021);
        spnMes.getValueFactory().setValue(1);

        cmbEstadoPago.setValue(null);
        cmbAdministracion.valueProperty().setValue(null);
        cmbCliente.valueProperty().setValue(null);
        cmbLocal.valueProperty().setValue(null);
    }

    public boolean ValidarDecimal(String numero) {
        Pattern pattern = Pattern.compile("^[0-9]{1,8}+([.][0-9]{1,2})?$");
        Matcher matcher = pattern.matcher(numero);
        return matcher.matches();
    }

    public boolean validarTexto(String letra) {
        Pattern pattern = Pattern.compile("^\\D+$");
        Matcher matcher = pattern.matcher(letra);
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
                listaComboBox.add(cmbCliente);
                listaComboBox.add(cmbLocal);
                if (escenarioPrincipal.validar(listadoTextFiel, listaComboBox)) {
                    if (ValidarDecimal(txtValorNetoPago.getText())) {

                        agregarCuentasPorCobrar();
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
                        alert.setContentText("El valor numerico ingresado es invalido");
                        alert.showAndWait();
                    }
                } else {

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setTitle("KINAL MALL");
                    alert.setContentText("Falta llenar datos");
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

                if (tblCuentasPorCobrar.getSelectionModel().getSelectedItem() != null) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setHeaderText(null);
                    alert.setTitle("Confirmación");
                    alert.setContentText("¿Estas seguro de eliminar el elemento seleccionado?");
                    Optional<ButtonType> result = alert.showAndWait();

                    if (result.get() == ButtonType.OK) {
                        try {
                            eliminarCuentasPorCobrar();
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
                if (tblCuentasPorCobrar.getSelectionModel().getSelectedItem() != null) {
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

                    editarCuentasPorCobrar();
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
                limpiarCampos();
                deshabilitarCampos();
                operacion = Operaciones.NINGUNO;
            case NINGUNO:    
                Map parametros = new HashMap();
                GenerarReporte.getInstance().mostrarReporte("ReporteCuentasPorCobrar.jasper", "Reporte Cuentas por cobrar", parametros);
                break;
        }

    }

    @FXML
    private void mostrarVistaMenuPrincipal(MouseEvent event) {
        this.escenarioPrincipal.mostrarClientes();
    }

}
