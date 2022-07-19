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
import javafx.scene.input.MouseEvent;
import org.carlosalvarez.bean.Locales;
import org.carlosalvarez.system.Principal;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.carlosalvarez.db.Conexion;
import org.carlosalvarez.report.GenerarReporte;

/**
 *
 * @author Carlos Adolfo Alvarez Crúz
 * @date 14 jun 2021
 * @time 12:51:53 Codigo tecnico: IN5BV
 */
public class LocalesController implements Initializable {

    private Principal escenarioPrincipal;

    private ObservableList<Locales> listaLocales;
    @FXML
    private ImageView imgNuevo;
    @FXML
    private ImageView imgEliminar;
    @FXML
    private ImageView imgEditar;
    @FXML
    private ImageView imgReporte;
    @FXML
    private CheckBox chkDisponibiidad;
    @FXML
    private Label lblLocalesDisponibles;

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
    private TextField txtSaldoFavor;
    @FXML
    private TextField txtSaldoLiquido;
    @FXML
    private TableView tblLocales;
    @FXML
    private TableColumn colId;
    @FXML
    private TableColumn colSaldoFavor;
    @FXML
    private TableColumn colSaldoContra;
    @FXML
    private TableColumn colMesesPendientes;
    @FXML
    private TableColumn colDisponibilidad;
    @FXML
    private TableColumn colValorLocal;
    @FXML
    private TableColumn colValorAdministracion;
    @FXML
    private TextField txtSaldoContra;
    @FXML
    private TextField txtMesesPendientes;
    @FXML
    private TextField txtValorLocal;
    @FXML
    private TextField txtValorAdministracion;

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
    private void mostrarVistaMenuPrincipal(MouseEvent event) {
        this.escenarioPrincipal.mostrarAdministracion();
    }

    public ObservableList<Locales> getLocales() {

        ArrayList<Locales> listado = new ArrayList<Locales>();
        PreparedStatement stmt = null;
        ResultSet resultado = null;

        try {

            stmt = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarLocales()}");
            resultado = stmt.executeQuery();

            int contador = 0;

            while (resultado.next()) {
                listado.add(new Locales(resultado.getInt("id"),
                        resultado.getBigDecimal("saldoFavor"),
                        resultado.getBigDecimal("saldoContra"),
                        resultado.getInt("mesesPendientes"),
                        resultado.getBoolean("disponibilidad"),
                        resultado.getBigDecimal("valorLocal"),
                        resultado.getBigDecimal("valorAdministracion")));

                if (resultado.getBoolean("disponibilidad") == true) {
                    contador++;
                }
            }

            lblLocalesDisponibles.setText(String.valueOf(contador));

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

        listaLocales = FXCollections.observableArrayList(listado);
        return listaLocales;
    }

    public void cargarDatos() {
        tblLocales.setItems(getLocales());
        colId.setCellValueFactory(new PropertyValueFactory<Locales, Integer>("id"));
        colSaldoFavor.setCellValueFactory(new PropertyValueFactory<Locales, BigDecimal>("saldoFavor"));
        colSaldoContra.setCellValueFactory(new PropertyValueFactory<Locales, BigDecimal>("saldoContra"));
        colMesesPendientes.setCellValueFactory(new PropertyValueFactory<Locales, Integer>("mesesPendientes"));
        colDisponibilidad.setCellValueFactory(new PropertyValueFactory<Locales, Boolean>("disponibilidad"));
        colValorLocal.setCellValueFactory(new PropertyValueFactory<Locales, BigDecimal>("valorLocal"));
        colValorAdministracion.setCellValueFactory(new PropertyValueFactory<Locales, BigDecimal>("valorAdministracion"));
    }

    public boolean existeElementosSeleccionados() {
        if (tblLocales.getSelectionModel().getSelectedItem() == null) {
            return false;
        } else {
            return true;
        }
    }

    private void agregarLocales() {
        Locales registro = new Locales();

        BigDecimal a = new BigDecimal(txtSaldoFavor.getText());
        registro.setSaldoFavor(a);
        BigDecimal b = new BigDecimal(txtSaldoContra.getText());
        registro.setSaldoContra(b);
        registro.setMesesPendientes(Integer.parseInt(txtMesesPendientes.getText()));
        registro.setDisponibilidad(chkDisponibiidad.isSelected());
        BigDecimal c = new BigDecimal(txtValorLocal.getText());
        registro.setValorLocal(c);
        BigDecimal d = new BigDecimal(txtValorAdministracion.getText());
        registro.setValorAdministracion(d);

        try {
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarLocales(?,?,?,?,?,?)}");
            stmt.setBigDecimal(1, registro.getSaldoFavor());
            stmt.setBigDecimal(2, registro.getSaldoContra());
            stmt.setInt(3, registro.getMesesPendientes());
            stmt.setBoolean(4, registro.isDisponibilidad());
            stmt.setBigDecimal(5, registro.getValorLocal());
            stmt.setBigDecimal(6, registro.getValorAdministracion());

            stmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void eliminarLocales() {
        try {
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarLocales(?)}");
            stmt.setInt(1, ((Locales) tblLocales.getSelectionModel().getSelectedItem()).getId());
            stmt.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void editarLocales() {

        ArrayList<TextField> listaTexfield = new ArrayList<>();

        listaTexfield.add(txtId);
        listaTexfield.add(txtSaldoFavor);
        listaTexfield.add(txtSaldoContra);
        listaTexfield.add(txtMesesPendientes);
        listaTexfield.add(txtValorLocal);
        listaTexfield.add(txtValorAdministracion);

        ArrayList<CheckBox> listaChecBox = new ArrayList<>();
        listaChecBox.add(chkDisponibiidad);

        if (escenarioPrincipal.validar2(listaTexfield, listaChecBox)) {

            Locales registro = new Locales();
            registro.setId(Integer.parseInt(txtId.getText()));
            BigDecimal a = new BigDecimal(txtSaldoFavor.getText());
            registro.setSaldoFavor(a);
            BigDecimal b = new BigDecimal(txtSaldoContra.getText());
            registro.setSaldoContra(b);
            registro.setMesesPendientes(Integer.parseInt(txtMesesPendientes.getText()));
            registro.setDisponibilidad(chkDisponibiidad.isSelected());
            BigDecimal c = new BigDecimal(txtValorLocal.getText());
            registro.setValorLocal(c);
            BigDecimal d = new BigDecimal(txtValorAdministracion.getText());
            registro.setValorAdministracion(d);

            try {
                PreparedStatement stmt;
                stmt = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarLocales(?,?,?,?,?,?,?)}");
                stmt.setInt(1, registro.getId());
                stmt.setBigDecimal(2, registro.getSaldoFavor());
                stmt.setBigDecimal(3, registro.getSaldoContra());
                stmt.setInt(4, registro.getMesesPendientes());
                stmt.setBoolean(5, registro.isDisponibilidad());
                stmt.setBigDecimal(6, registro.getValorLocal());
                stmt.setBigDecimal(7, registro.getValorAdministracion());
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
        if (existeElementosSeleccionados()) {
            txtId.setText(String.valueOf(((Locales) tblLocales.getSelectionModel().getSelectedItem()).getId()));
            txtSaldoFavor.setText(String.valueOf(((Locales) tblLocales.getSelectionModel().getSelectedItem()).getSaldoFavor()));
            txtSaldoContra.setText(String.valueOf(((Locales) tblLocales.getSelectionModel().getSelectedItem()).getSaldoContra()));
            txtMesesPendientes.setText(String.valueOf(((Locales) tblLocales.getSelectionModel().getSelectedItem()).getMesesPendientes()));
            txtValorLocal.setText(String.valueOf(((Locales) tblLocales.getSelectionModel().getSelectedItem()).getValorLocal()));

            //chkDisponibiidad.setText(String.valueOf(((Locales) tblLocales.getSelectionModel().getSelectedItem()).isDisponibilidad()));
            chkDisponibiidad.setSelected(((Locales) tblLocales.getSelectionModel().getSelectedItem()).isDisponibilidad());

            txtValorAdministracion.setText(String.valueOf(((Locales) tblLocales.getSelectionModel().getSelectedItem()).getValorAdministracion()));
            calculoSaldoLiquido();
        }

    }

    private void calculoSaldoLiquido() {

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
        txtSaldoFavor.setEditable(true);
        txtSaldoContra.setEditable(true);
        txtMesesPendientes.setEditable(true);
        chkDisponibiidad.setDisable(false);
        txtValorLocal.setEditable(true);
        txtValorAdministracion.setEditable(true);
    }

    private void deshabilitarCampos() {
        txtId.setEditable(false);
        txtSaldoFavor.setEditable(false);
        txtSaldoContra.setEditable(false);
        txtMesesPendientes.setEditable(false);
        chkDisponibiidad.setDisable(true);
        txtValorLocal.setEditable(false);
        txtValorAdministracion.setEditable(false);
    }

    private void limpiarCampos() {
        txtId.clear();
        txtSaldoFavor.clear();
        txtSaldoContra.clear();
        txtMesesPendientes.clear();
        chkDisponibiidad.setSelected(false);
        txtValorLocal.clear();
        txtValorAdministracion.clear();
        txtSaldoLiquido.clear();
    }

    public boolean ValidarDecimal(String numero) {
        Pattern pattern = Pattern.compile("^[0-9]{1,8}+([.][0-9]{1,2})?$");
        Matcher matcher = pattern.matcher(numero);
        return matcher.matches();
    }

    public boolean ValidarNumero(String numero) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher matcher = pattern.matcher(numero);
        return matcher.matches();
    }

    @FXML
    private void nuevo(ActionEvent event) {

        switch (operacion) {
            case NINGUNO:
                limpiarCampos();
                habilitarCampos();
                imgNuevo.setImage(new Image("/org/carlosalvarez/resource/images/guardar.png"));
                imgEliminar.setImage(new Image("/org/carlosalvarez/resource/images/cancelar.png"));
                btntNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                operacion = Operaciones.GUARDAR;
                break;
            case GUARDAR:
                if (txtSaldoContra.getText().length() != 0 && txtSaldoFavor.getText().length() != 0
                        && txtMesesPendientes.getText().length() != 0 && txtValorLocal.getText().length() != 0 && txtValorAdministracion.getText().length() != 0) {
                    if (ValidarDecimal(txtSaldoContra.getText()) && ValidarDecimal(txtSaldoFavor.getText())
                            && ValidarDecimal(txtValorAdministracion.getText()) && ValidarDecimal(txtValorLocal.getText())) {
                        if (ValidarNumero(txtMesesPendientes.getText())) {
                            agregarLocales();
                            cargarDatos();
                            deshabilitarCampos();
                            limpiarCampos();
                            imgNuevo.setImage(new Image("/org/carlosalvarez/resource/images/nuevo.png"));
                            imgEliminar.setImage(new Image("/org/carlosalvarez/resource/images/eliminar.png"));
                            btntNuevo.setText("Nuevo");
                            btnEliminar.setText("Eliminar");
                            btnEditar.setDisable(false);
                            btnReporte.setDisable(false);
                            operacion = Operaciones.NINGUNO;
                        } else {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setHeaderText(null);
                            alert.setTitle("KINAL MALL");
                            alert.setContentText("El campo meses pendientes solo acepta numeros");
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
                imgNuevo.setImage(new Image("/org/carlosalvarez/resource/images/nuevo.png"));
                imgEliminar.setImage(new Image("/org/carlosalvarez/resource/images/eliminar.png"));
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                limpiarCampos();
                deshabilitarCampos();
                operacion = Operaciones.NINGUNO;

                break;
            case NINGUNO://Elimanación
                if (tblLocales.getSelectionModel().getSelectedItem() != null) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setHeaderText(null);
                    alert.setTitle("Confirmación");
                    alert.setContentText("¿Estas seguro de eliminar el elemento seleccionado?");
                    Optional<ButtonType> result = alert.showAndWait();

                    if (result.get() == ButtonType.OK) {
                        try {
                            eliminarLocales();
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
                if (tblLocales.getSelectionModel().getSelectedItem() != null) {
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
                    alert.setContentText("Debe seleccionar un elemento");
                    alert.showAndWait();
                }
                break;
            case ACTUALIZAR:
                if (ValidarDecimal(txtSaldoContra.getText()) && ValidarDecimal(txtSaldoFavor.getText())
                        && ValidarDecimal(txtValorAdministracion.getText()) && ValidarDecimal(txtValorLocal.getText())) {
                    if (ValidarNumero(txtMesesPendientes.getText())) {
                        editarLocales();
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
                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setHeaderText(null);
                        alert.setTitle("KINAL MALL");
                        alert.setContentText("El campo meses pendientes solo acepta numeros");
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
        System.out.println("Operacion " + operacion);
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
                GenerarReporte.getInstance().mostrarReporte("ReporteLocales.jasper", "Reporte Locales", parametros);
        }
    }

}
