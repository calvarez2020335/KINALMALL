/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.carlosalvarez.controller;

import com.jfoenix.controls.JFXDatePicker;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.carlosalvarez.bean.Administracion;
import org.carlosalvarez.bean.Cargos;
import org.carlosalvarez.bean.Departamentos;
import org.carlosalvarez.bean.Empleados;
import org.carlosalvarez.bean.Horarios;
import org.carlosalvarez.db.Conexion;
import org.carlosalvarez.report.GenerarReporte;
import org.carlosalvarez.system.Principal;

/**
 *
 * @author Carlos Adolfo Alvarez Crúz
 * @date 9 jul 2021
 * @time 12:20:20 Codigo tecnico: IN5BV
 */
public class EmpleadosController implements Initializable {

    private Principal escenarioPrincipal;
    @FXML
    private TableView tblEmpleados;
    @FXML
    private TableColumn colNombre;
    @FXML
    private TableColumn colApellidos;
    @FXML
    private TableColumn colEmail;
    @FXML
    private TableColumn colTelefono;
    @FXML
    private TableColumn colFechaContratacion;
    @FXML
    private TableColumn colSueldo;
    @FXML
    private TableColumn colIdDepartamento;
    @FXML
    private TableColumn colIdCargo;
    @FXML
    private TableColumn colIdHorario;

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
    private TableColumn colId;
    @FXML
    private TableColumn colIdAdministracion;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtEmail;
    @FXML
    private ComboBox cmbDepartamento;
    @FXML
    private ComboBox cmbcargo;
    @FXML
    private TextField txtTelefono;
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtApellidos;
    @FXML
    private JFXDatePicker dpFechaContratacion;
    @FXML
    private TextField txtSueldo;
    @FXML
    private ComboBox cmbHorario;
    @FXML
    private ComboBox cmbAdministracion;

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    private ObservableList<Empleados> listaEmpleados;
    private ObservableList<Departamentos> listaDepartamentos;
    private ObservableList<Cargos> listaCargos;
    private ObservableList<Horarios> listaHorarios;
    private ObservableList<Administracion> listaAdministracion;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatos();
    }

    public ObservableList<Empleados> getEmpleados() {
        ArrayList<Empleados> lista = new ArrayList<>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarEmpleados()}");
            rs = pstmt.executeQuery();

            while (rs.next()) {
                lista.add(new Empleados(rs.getInt("id"),
                        rs.getString("nombres"),
                        rs.getString("apellidos"),
                        rs.getString("email"),
                        rs.getString("telefono"),
                        rs.getDate("fechaContratacion"),
                        rs.getBigDecimal("sueldo"),
                        rs.getInt("idDepartamento"),
                        rs.getInt("idCargo"),
                        rs.getInt("idHorario"),
                        rs.getInt("idAdministracion"))
                );
            }

            listaEmpleados = FXCollections.observableArrayList(lista);

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

        return listaEmpleados;
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

    public ObservableList<Cargos> getCargos() {
        ArrayList<Cargos> listado = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarCargos()}");
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

    public ObservableList<Horarios> getHorarios() {

        PreparedStatement stmt = null;
        ResultSet resultado = null;

        ArrayList<Horarios> listado = new ArrayList<Horarios>();

        try {

            stmt = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarHorarios()}");
            resultado = stmt.executeQuery();

            while (resultado.next()) {
                listado.add(new Horarios(resultado.getInt("id"),
                        resultado.getTime("horarioEntrada"),
                        resultado.getTime("horarioSalida"),
                        resultado.getBoolean("lunes"),
                        resultado.getBoolean("martes"),
                        resultado.getBoolean("miercoles"),
                        resultado.getBoolean("jueves"),
                        resultado.getBoolean("viernes")));
            }
            listaHorarios = FXCollections.observableArrayList(listado);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                resultado.close();
                stmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return listaHorarios;
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

    private Departamentos buscarDepartamento(int id) {
        Departamentos departamentos = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarDepartamentos(?)}");
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                departamentos = new Departamentos(rs.getInt("id"), rs.getString("nombre"));
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

        return departamentos;
    }

    private Cargos buscarCargos(int id) {
        Cargos cargos = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarCargos(?)}");
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                cargos = new Cargos(rs.getInt("id"), rs.getString("nombre"));
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
        return cargos;
    }

    private Horarios buscarHorarios(int id) {
        Horarios horarios = null;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarHorarios(?)}");
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                horarios = new Horarios(rs.getInt("id"),
                        rs.getTime("horarioEntrada"),
                        rs.getTime("horarioSalida"),
                        rs.getBoolean("lunes"),
                        rs.getBoolean("martes"),
                        rs.getBoolean("miercoles"),
                        rs.getBoolean("jueves"),
                        rs.getBoolean("viernes"));
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
        return horarios;
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

    public void cargarDatos() {
        tblEmpleados.setItems(getEmpleados());
        colId.setCellValueFactory(new PropertyValueFactory<Empleados, Integer>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Empleados, String>("nombres"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<Empleados, String>("apellidos"));
        colEmail.setCellValueFactory(new PropertyValueFactory<Empleados, String>("email"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Empleados, String>("telefono"));
        colFechaContratacion.setCellValueFactory(new PropertyValueFactory<Empleados, Date>("fechaContratacion"));
        colSueldo.setCellValueFactory(new PropertyValueFactory<Empleados, BigDecimal>("sueldo"));
        colIdDepartamento.setCellValueFactory(new PropertyValueFactory<Empleados, Integer>("idDepartamento"));
        colIdCargo.setCellValueFactory(new PropertyValueFactory<Empleados, Integer>("idCargo"));
        colIdHorario.setCellValueFactory(new PropertyValueFactory<Empleados, Integer>("idHorario"));
        colIdAdministracion.setCellValueFactory(new PropertyValueFactory<Empleados, Integer>("idAdministracion"));

        cmbAdministracion.setItems(getAdministracion());
        cmbDepartamento.setItems(getDepartamentos());
        cmbHorario.setItems(getHorarios());
        cmbcargo.setItems(getCargos());
    }

    public boolean existeElementoSeleccionado() {
        if (tblEmpleados.getSelectionModel().getSelectedItem() == null) {
            return false;
        } else {
            return true;
        }
    }

    @FXML
    private void seleccionarElementos() {
        if (existeElementoSeleccionado()) {
            txtId.setText(String.valueOf(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getId()));
            txtNombre.setText(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getNombres());
            txtApellidos.setText(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getApellidos());
            txtTelefono.setText(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getTelefono());
            txtEmail.setText(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getEmail());
            txtSueldo.setText(String.valueOf(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getSueldo()));
            dpFechaContratacion.setValue(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getFechaContratacion().toLocalDate());

            cmbDepartamento.getSelectionModel().select(buscarDepartamento(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getIdDepartamento()));
            cmbcargo.getSelectionModel().select(buscarCargos(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getIdCargo()));
            cmbHorario.getSelectionModel().select(buscarHorarios(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getIdHorario()));
            cmbAdministracion.getSelectionModel().select(buscarAdministracion(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getIdAdministracion()));
        }
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

    public boolean ValidarDecimal(String numero) {
        Pattern pattern = Pattern.compile("^[0-9]{1,8}+([.][0-9]{1,2})?$");
        Matcher matcher = pattern.matcher(numero);
        return matcher.matches();
    }

    public void agregarEmpleados() {
        Empleados empleados = new Empleados();

        empleados.setNombres(txtNombre.getText());
        empleados.setApellidos(txtApellidos.getText());
        empleados.setEmail(txtEmail.getText());
        empleados.setTelefono(txtTelefono.getText());
        empleados.setFechaContratacion(java.sql.Date.valueOf(dpFechaContratacion.getValue()));
        BigDecimal a = new BigDecimal(txtSueldo.getText());
        empleados.setSueldo(a);

        empleados.setIdDepartamento(((Departamentos) cmbDepartamento.getSelectionModel().getSelectedItem()).getId());
        empleados.setIdCargo(((Cargos) cmbcargo.getSelectionModel().getSelectedItem()).getId());
        empleados.setIdHorario(((Horarios) cmbHorario.getSelectionModel().getSelectedItem()).getId());
        empleados.setIdAdministracion(((Administracion) cmbAdministracion.getSelectionModel().getSelectedItem()).getId());

        try {
            PreparedStatement pstms;

            pstms = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarEmpleados(?,?,?,?,?,?,?,?,?,?)}");
            pstms.setString(1, empleados.getNombres());
            pstms.setString(2, empleados.getApellidos());
            pstms.setString(3, empleados.getEmail());
            pstms.setString(4, empleados.getTelefono());
            pstms.setDate(5, empleados.getFechaContratacion());
            pstms.setBigDecimal(6, empleados.getSueldo());
            pstms.setInt(7, empleados.getIdDepartamento());
            pstms.setInt(8, empleados.getIdCargo());
            pstms.setInt(9, empleados.getIdHorario());
            pstms.setInt(10, empleados.getIdAdministracion());
            pstms.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void eliminarEmpleadso() {
        Empleados empleados = (Empleados) tblEmpleados.getSelectionModel().getSelectedItem();

        try {

            PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarEmpleados(?)}");
            pstmt.setInt(1, empleados.getId());
            pstmt.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void editarEmpleados() {
        ArrayList<TextField> listaTexfield = new ArrayList<>();

        listaTexfield.add(txtId);
        listaTexfield.add(txtNombre);
        listaTexfield.add(txtApellidos);
        listaTexfield.add(txtEmail);
        listaTexfield.add(txtTelefono);
        listaTexfield.add(txtSueldo);

        ArrayList<ComboBox> listaComboBox = new ArrayList<>();

        listaComboBox.add(cmbDepartamento);
        listaComboBox.add(cmbcargo);
        listaComboBox.add(cmbHorario);
        listaComboBox.add(cmbAdministracion);

        if (escenarioPrincipal.validar(listaTexfield, listaComboBox)) {

            Empleados empleados = new Empleados();

            empleados.setId(Integer.valueOf(txtId.getText()));
            empleados.setNombres(txtNombre.getText());
            empleados.setApellidos(txtApellidos.getText());
            empleados.setEmail(txtEmail.getText());
            empleados.setTelefono(txtTelefono.getText());
            empleados.setFechaContratacion(java.sql.Date.valueOf(dpFechaContratacion.getValue()));
            BigDecimal a = new BigDecimal(txtSueldo.getText());
            empleados.setSueldo(a);

            empleados.setIdDepartamento(((Departamentos) cmbDepartamento.getSelectionModel().getSelectedItem()).getId());
            empleados.setIdCargo(((Cargos) cmbcargo.getSelectionModel().getSelectedItem()).getId());
            empleados.setIdHorario(((Horarios) cmbHorario.getSelectionModel().getSelectedItem()).getId());
            empleados.setIdAdministracion(((Administracion) cmbAdministracion.getSelectionModel().getSelectedItem()).getId());

            try {
                PreparedStatement pstms;

                pstms = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarEmpleados(?,?,?,?,?,?,?,?,?,?,?)}");
                pstms.setInt(1, empleados.getId());
                pstms.setString(2, empleados.getNombres());
                pstms.setString(3, empleados.getApellidos());
                pstms.setString(4, empleados.getEmail());
                pstms.setString(5, empleados.getTelefono());
                pstms.setDate(6, empleados.getFechaContratacion());
                pstms.setBigDecimal(7, empleados.getSueldo());
                pstms.setInt(8, empleados.getIdDepartamento());
                pstms.setInt(9, empleados.getIdCargo());
                pstms.setInt(10, empleados.getIdHorario());
                pstms.setInt(11, empleados.getIdAdministracion());
                pstms.execute();

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

    public void activarControles() {
        txtId.setEditable(false);
        txtApellidos.setEditable(true);
        txtEmail.setEditable(true);
        txtNombre.setEditable(true);
        txtSueldo.setEditable(true);
        txtTelefono.setEditable(true);

        cmbAdministracion.setDisable(false);
        cmbDepartamento.setDisable(false);
        cmbHorario.setDisable(false);
        cmbcargo.setDisable(false);
    }

    public void desactivarControles() {
        txtId.setEditable(false);
        txtApellidos.setEditable(false);
        txtEmail.setEditable(false);
        txtNombre.setEditable(false);
        txtSueldo.setEditable(false);
        txtTelefono.setEditable(false);

        cmbAdministracion.setDisable(true);
        cmbDepartamento.setDisable(true);
        cmbHorario.setDisable(true);
        cmbcargo.setDisable(true);
    }

    public void limpiarControles() {
        txtApellidos.clear();
        txtEmail.clear();
        txtId.clear();
        txtNombre.clear();
        txtSueldo.clear();
        txtTelefono.clear();

        cmbAdministracion.valueProperty().set(null);
        cmbDepartamento.valueProperty().set(null);
        cmbHorario.valueProperty().set(null);
        cmbcargo.valueProperty().set(null);
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
                listadoTextFiel.add(txtSueldo);
                listadoTextFiel.add(txtEmail);
                listadoTextFiel.add(txtTelefono);

                ArrayList<ComboBox> listaComboBox = new ArrayList<>();
                listaComboBox.add(cmbAdministracion);
                listaComboBox.add(cmbDepartamento);
                listaComboBox.add(cmbHorario);
                listaComboBox.add(cmbcargo);

                if (escenarioPrincipal.validar(listadoTextFiel, listaComboBox)) {
                    if (validarEmail(txtEmail.getText())) {
                        if (ValidarTelefono(txtTelefono.getText())) {
                            if (dpFechaContratacion.getValue() != null) {
                                if (ValidarDecimal(txtSueldo.getText())) {

                                    agregarEmpleados();
                                    cargarDatos();
                                    desactivarControles();
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
                                    alert.setContentText("El valor numerico ingresado es incorrecto");
                                    alert.showAndWait();
                                }
                            } else {
                                Alert alert = new Alert(Alert.AlertType.WARNING);
                                alert.setHeaderText(null);
                                alert.setTitle("KINAL MALL");
                                alert.setContentText("Debe de ingresar una fecha");
                                alert.showAndWait();
                            }
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
                desactivarControles();
                operacion = Operaciones.NINGUNO;
                break;
            case NINGUNO://Elimanación

                if (tblEmpleados.getSelectionModel().getSelectedItem() != null) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setHeaderText(null);
                    alert.setTitle("Confirmación");
                    alert.setContentText("¿Estas seguro de eliminar el elemento seleccionado?");
                    Optional<ButtonType> result = alert.showAndWait();

                    if (result.get() == ButtonType.OK) {
                        try {
                            eliminarEmpleadso();
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
                if (tblEmpleados.getSelectionModel().getSelectedItem() != null) {
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
                        if (dpFechaContratacion.getValue() != null) {
                            if (ValidarDecimal(txtSueldo.getText())) {
                                editarEmpleados();
                                limpiarControles();
                                cargarDatos();
                                desactivarControles();
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
                                alert.setContentText("El valor numerico ingresado es incorrecto");
                                alert.showAndWait();
                            }
                        } else {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setHeaderText(null);
                            alert.setTitle("KINAL MALL");
                            alert.setContentText("Debe agregar una fecha");
                            alert.showAndWait();
                        }
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
                desactivarControles();
                operacion = Operaciones.NINGUNO;
            case NINGUNO:
                Map parametros = new HashMap();
                GenerarReporte.getInstance().mostrarReporte("ReporteEmpleados.jasper", "Reporte Empleados", parametros);
        }
    }

    @FXML
    private void mostrarVistaMenuPrincipal(MouseEvent event) {
        escenarioPrincipal.mostrarMenuPrincipal();
    }

    @FXML
    private void btnMostrarHorarios(ActionEvent event) {
        escenarioPrincipal.mostrarHorarios();
    }
    

}
