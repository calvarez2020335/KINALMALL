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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.carlosalvarez.bean.Administracion;
import org.carlosalvarez.bean.Clientes;
import org.carlosalvarez.bean.Locales;
import org.carlosalvarez.bean.TipoCliente;
import org.carlosalvarez.system.Principal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory;
import org.carlosalvarez.db.Conexion;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.carlosalvarez.report.GenerarReporte;

/**
 *
 * @author Carlos Adolfo Alvarez Crúz
 * @date 9 jun 2021
 * @time 14:52:46 Codigo tecnico: IN5BV
 */
public class ClientesController implements Initializable {

    Principal escenarioPrincipal;
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
    private TextField txtTelefono;
    @FXML
    private TextField txtDireccion;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtApellidos;
    @FXML
    private TableView tblClientes;
    @FXML
    private TableColumn colId;
    @FXML
    private TableColumn colNombre;
    @FXML
    private TableColumn colTelefono;
    @FXML
    private TableColumn colDireccion;
    @FXML
    private TableColumn colEmail;
    @FXML
    private TableColumn colcolApellidos;
    @FXML
    private TableColumn colIdTipoCliente;
    @FXML
    private ComboBox cmbTipoCliente;
    @FXML
    private ImageView imgNuevo;
    @FXML
    private ImageView imgEliminar;
    @FXML
    private ImageView imgEditar;
    @FXML
    private ImageView imgReporte;

    private enum Operaciones {
        NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO;
    }

    private Operaciones operacion = Operaciones.NINGUNO;

    private ObservableList<Clientes> listaClientes;

    private ObservableList<TipoCliente> listaTipoClientes;

    private ObservableList<Locales> listaLocales;

    private ObservableList<Administracion> listaAdministracion;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatos();
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

    public ObservableList<TipoCliente> getTipoCliente() {
        ArrayList<TipoCliente> lista = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {

            pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarTipoCliente()}");
            rs = pstmt.executeQuery();

            while (rs.next()) {
                lista.add(new TipoCliente(rs.getInt("id"), rs.getString("descripcion")));
            }
            listaTipoClientes = FXCollections.observableArrayList(lista);

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

        return listaTipoClientes;

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

    public ObservableList<Administracion> getAdministracion() {

        ArrayList<Administracion> listado = new ArrayList<Administracion>();
        PreparedStatement stmt = null;
        ResultSet resultado = null;

        try {

            //CallableStatement stmt;
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

    private TipoCliente buscarTipoCliente(int id) {
        TipoCliente registro = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarTipoCliente(?)}");
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                registro = new TipoCliente(rs.getInt("id"), rs.getString("descripcion"));
            }

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

        return registro;
    }

    public boolean ValidarTelefono(String numero) {
        Pattern pattern = Pattern.compile("^[0-9]{8}$");
        Matcher matcher = pattern.matcher(numero);
        return matcher.matches();
    }

    public boolean validarEmail(String email) {
        Pattern pattern = Pattern.compile("^[\\w-]+(\\.[\\w-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public void agregarCliente() {

        Clientes registro = new Clientes();

        registro.setNombres(txtNombre.getText());
        registro.setApellidos(txtApellidos.getText());
        registro.setTelefono(txtTelefono.getText());
        registro.setDireccion(txtDireccion.getText());
        registro.setEmail(txtEmail.getText());
        registro.setIdTipoCliente(((TipoCliente) cmbTipoCliente.getSelectionModel().getSelectedItem()).getId());

        try {

            PreparedStatement pstmt;

            pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_AgregarClientes(?,?,?,?,?,?)}");
            pstmt.setString(1, registro.getNombres());
            pstmt.setString(2, registro.getApellidos());
            pstmt.setString(3, registro.getTelefono());
            pstmt.setString(4, registro.getDireccion());
            pstmt.setString(5, registro.getEmail());
            pstmt.setInt(6, registro.getIdTipoCliente());
            pstmt.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eliminarClientes() {
        Clientes cliente = ((Clientes) tblClientes.getSelectionModel().getSelectedItem());

        try {

            PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarClientes(?)}");
            pstmt.setInt(1, cliente.getId());

            pstmt.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void editarClientes() {
        ArrayList<TextField> listaTetxField = new ArrayList<>();

        listaTetxField.add(txtId);
        listaTetxField.add(txtNombre);
        listaTetxField.add(txtApellidos);
        listaTetxField.add(txtDireccion);
        listaTetxField.add(txtEmail);
        listaTetxField.add(txtTelefono);

        ArrayList<ComboBox> listaComboBox = new ArrayList<>();
        listaComboBox.add(cmbTipoCliente);

        if (escenarioPrincipal.validar(listaTetxField, listaComboBox)) {

            Clientes cliente = new Clientes();

            cliente.setId(Integer.parseInt(txtId.getText()));
            cliente.setNombres(txtNombre.getText());
            cliente.setApellidos(txtApellidos.getText());
            cliente.setTelefono(txtTelefono.getText());
            cliente.setDireccion(txtDireccion.getText());
            cliente.setEmail(txtEmail.getText());
            cliente.setIdTipoCliente(((TipoCliente) cmbTipoCliente.getSelectionModel().getSelectedItem()).getId());

            try {
                PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarClientes(?,?,?,?,?,?,?)}");
                pstmt.setInt(1, cliente.getId());
                pstmt.setString(2, cliente.getNombres());
                pstmt.setString(3, cliente.getApellidos());
                pstmt.setString(4, cliente.getTelefono());
                pstmt.setString(5, cliente.getDireccion());
                pstmt.setString(6, cliente.getEmail());
                pstmt.setInt(7, cliente.getIdTipoCliente());
                pstmt.execute();

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

    public void cargarDatos() {

        tblClientes.setItems(getClientes());
        colId.setCellValueFactory(new PropertyValueFactory<Clientes, Integer>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Clientes, String>("nombres"));
        colcolApellidos.setCellValueFactory(new PropertyValueFactory<Clientes, String>("apellidos"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Clientes, String>("telefono"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Clientes, String>("direccion"));
        colEmail.setCellValueFactory(new PropertyValueFactory<Clientes, String>("email"));
        colIdTipoCliente.setCellValueFactory(new PropertyValueFactory<Clientes, Integer>("idTipoCliente"));

        cmbTipoCliente.setItems(getTipoCliente());
    }

    public boolean existeElementoSeleccionado() {
        if (tblClientes.getSelectionModel().getSelectedItem() == null) {
            return false;
        } else {
            return true;
        }
    }

    @FXML
    public void seleccionarElementos() {
        if (existeElementoSeleccionado()) {
            txtId.setText(String.valueOf(((Clientes) tblClientes.getSelectionModel().getSelectedItem()).getId()));
            txtNombre.setText(((Clientes) tblClientes.getSelectionModel().getSelectedItem()).getNombres());
            txtApellidos.setText(((Clientes) tblClientes.getSelectionModel().getSelectedItem()).getApellidos());
            txtTelefono.setText(((Clientes) tblClientes.getSelectionModel().getSelectedItem()).getTelefono());
            txtEmail.setText(((Clientes) tblClientes.getSelectionModel().getSelectedItem()).getEmail());
            txtDireccion.setText(((Clientes) tblClientes.getSelectionModel().getSelectedItem()).getDireccion());
            cmbTipoCliente.getSelectionModel().select(buscarTipoCliente(((Clientes) tblClientes.getSelectionModel().getSelectedItem()).getIdTipoCliente()));
        }
    }

    public void activarControles() {
        txtId.setEditable(false);
        txtApellidos.setEditable(true);
        txtNombre.setEditable(true);
        txtDireccion.setEditable(true);
        txtTelefono.setEditable(true);
        txtEmail.setEditable(true);
        cmbTipoCliente.setDisable(false);
    }

    public void desativarControles() {
        txtId.setEditable(false);
        txtApellidos.setEditable(false);
        txtNombre.setEditable(false);
        txtDireccion.setEditable(false);
        txtTelefono.setEditable(false);
        txtEmail.setEditable(false);
        cmbTipoCliente.setDisable(true);
    }

    public void limpiarControles() {
        txtId.clear();
        txtApellidos.clear();
        txtNombre.clear();
        txtDireccion.clear();
        txtTelefono.clear();
        txtEmail.clear();
        // cmbTipoCliente.getSelectionModel().clearSelection();
        cmbTipoCliente.valueProperty().set(null);
    }

    @FXML
    private void nuevo(ActionEvent event) {
        switch (operacion) {
            case NINGUNO:
                limpiarControles();
                activarControles();
                btntNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                imgNuevo.setImage(new Image("/org/carlosalvarez/resource/images/guardar.png"));
                imgEliminar.setImage(new Image("/org/carlosalvarez/resource/images/cancelar.png"));
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                operacion = Operaciones.GUARDAR;
                break;
            case GUARDAR:

                ArrayList<TextField> listadoTextFiel = new ArrayList<>();

                listadoTextFiel.add(txtNombre);
                listadoTextFiel.add(txtApellidos);
                listadoTextFiel.add(txtDireccion);
                listadoTextFiel.add(txtEmail);
                listadoTextFiel.add(txtTelefono);

                ArrayList<ComboBox> listaComboBox = new ArrayList<>();
                listaComboBox.add(cmbTipoCliente);

                if (escenarioPrincipal.validar(listadoTextFiel, listaComboBox)) {
                    if (validarEmail(txtEmail.getText())) {
                        if (ValidarTelefono(txtTelefono.getText())) {

                            agregarCliente();
                            cargarDatos();
                            desativarControles();
                            limpiarControles();
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
                            alert.setContentText("El numero telefonico es invalido");
                            alert.showAndWait();
                        }

                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setHeaderText(null);
                        alert.setTitle("KINAL MALL");
                        alert.setContentText("El correo es invalido");
                        alert.showAndWait();
                    }

                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setHeaderText(null);
                    alert.setTitle("KINAL MALL");
                    alert.setContentText("Falta agregar datos");
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
                limpiarControles();
                desativarControles();
                operacion = Operaciones.NINGUNO;
                break;
            case NINGUNO://Elimanación

                if (tblClientes.getSelectionModel().getSelectedItem() != null) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setHeaderText(null);
                    alert.setTitle("Confirmación");
                    alert.setContentText("¿Estas seguro de eliminar el elemento seleccionado?");
                    Optional<ButtonType> result = alert.showAndWait();

                    if (result.get() == ButtonType.OK) {
                        try {
                            eliminarClientes();
                            limpiarControles();
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
                if (tblClientes.getSelectionModel().getSelectedItem() != null) {
                    activarControles();
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
                if (ValidarTelefono(txtTelefono.getText())) {
                    if (validarEmail(txtEmail.getText())) {
                        editarClientes();
                        limpiarControles();
                        cargarDatos();
                        desativarControles();
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
                        alert.setContentText("El correo electronico es invalido");
                        alert.showAndWait();

                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setHeaderText(null);
                    alert.setTitle("KINAL MALL");
                    alert.setContentText("El numero telefonico es invalido");
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
                imgEditar.setImage(new Image("/org/carlosalvarez/resource/images/editar.png"));
                imgReporte.setImage(new Image("/org/carlosalvarez/resource/images/reporte.png"));
                btnReporte.setDisable(false);
                btntNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                cargarDatos();
                limpiarControles();
                desativarControles();
                operacion = Operaciones.NINGUNO;
            case NINGUNO:
                Map parametros = new HashMap();
                GenerarReporte.getInstance().mostrarReporte("ReporteClientes.jasper", "Reporte Clientes", parametros);
                break;
        }
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    @FXML
    private void mostrarVistaMenuPrincipal(MouseEvent event) {
        escenarioPrincipal.mostrarMenuPrincipal();
    }
    
    @FXML
    private void mostrarCuentasPorCobrar(ActionEvent event) {
        escenarioPrincipal.mostrarCuentasPorCobrar();
    }
}
