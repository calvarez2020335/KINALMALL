/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.carlosalvarez.controller;

import com.jfoenix.controls.JFXTimePicker;
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
import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import org.carlosalvarez.bean.Horarios;
import org.carlosalvarez.db.Conexion;
import org.carlosalvarez.system.Principal;
import java.sql.Time;
import java.time.LocalTime;
import java.time.format.FormatStyle;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.util.StringConverter;
import javafx.util.converter.LocalTimeStringConverter;
import org.carlosalvarez.report.GenerarReporte;

/**
 *
 * @author Carlos Adolfo Alvarez Crúz
 * @date 23 jun 2021
 * @time 12:56:52 Codigo tecnico: IN5BV
 */
public class HorariosController implements Initializable {

    private Principal escenarioPrincipal;

    private ObservableList<Horarios> listaHorarios;
    @FXML
    private CheckBox chkLunes;
    @FXML
    private CheckBox chkMartes;
    @FXML
    private CheckBox chkMiercoles;
    @FXML
    private CheckBox chkJuevez;
    @FXML
    private CheckBox chkViernes;
    @FXML
    private JFXTimePicker tpHorarioEntrada;
    @FXML
    private JFXTimePicker tpHorarioSalida;

    /*  private boolean validarHorarios() {
        
        String horarioEntrada = txtHorarioEntrada.getText();
        Pattern pattern = Pattern.compile("([01][0-9]|[2][0123]):([0-5][0-9]):([0-5][0-9])");
        Matcher matcher = pattern.matcher(horarioEntrada);
        
        boolean resultado = matcher.matches();
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("kinal mall");
        alert.setContentText(String.valueOf(resultado));
        alert.showAndWait();
        
        return resultado;
    }*/
    private enum Operaciones {
        NUEVO, GUARDAR, EDITAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO
    }

    private Operaciones operacion = Operaciones.NINGUNO;

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

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
    private TableView tblHorarios;
    @FXML
    private TableColumn colId;
    @FXML
    private TableColumn colHorarioEntrada;
    @FXML
    private TableColumn colHorarioSalida;
    @FXML
    private TableColumn colLunes;
    @FXML
    private TableColumn colMartes;
    @FXML
    private TableColumn colMiercoles;
    @FXML
    private TableColumn colJueves;
    @FXML
    private TableColumn colViernes;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatos();

        Locale locale = new Locale("es", "GT");
        Locale.setDefault(locale);

        tpHorarioEntrada.set24HourView(false);
        tpHorarioSalida.set24HourView(false);
        
        StringConverter<LocalTime> convertior = new LocalTimeStringConverter(FormatStyle.SHORT, Locale.US);
        tpHorarioEntrada.setConverter(convertior);
        
        StringConverter<LocalTime> convertior1 = new LocalTimeStringConverter(FormatStyle.SHORT, Locale.US);
        tpHorarioSalida.setConverter(convertior1);

        //tpHorarioEntrada.setEditable(false);
        //tpHorarioSalida.setEditable(false);
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
        } catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                resultado.close();
                stmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return listaHorarios;
    }

    public void cargarDatos() {
        tblHorarios.setItems(getHorarios());
        colId.setCellValueFactory(new PropertyValueFactory<Horarios, Integer>("id"));
        colHorarioEntrada.setCellValueFactory(new PropertyValueFactory<Horarios, Time>("horarioEntrada"));
        colHorarioSalida.setCellValueFactory(new PropertyValueFactory<Horarios, Time>("horarioSalida"));
        colLunes.setCellValueFactory(new PropertyValueFactory<Horarios, Boolean>("lunes"));
        colMartes.setCellValueFactory(new PropertyValueFactory<Horarios, Boolean>("martes"));
        colMiercoles.setCellValueFactory(new PropertyValueFactory<Horarios, Boolean>("miercoles"));
        colJueves.setCellValueFactory(new PropertyValueFactory<Horarios, Boolean>("jueves"));
        colViernes.setCellValueFactory(new PropertyValueFactory<Horarios, Boolean>("viernes"));
    }

    public boolean existeElementoSeleccionado() {
        if (tblHorarios.getSelectionModel().getSelectedItem() == null) {
            return false;
        } else {
            return true;
        }
    }

    public void agregarHorarios() {

        Horarios registro = new Horarios();

        registro.setHorarioEntrada((Time.valueOf(tpHorarioEntrada.getValue())));
        registro.setHorarioSalida(Time.valueOf(tpHorarioSalida.getValue()));
        registro.setLunes(chkLunes.isSelected());
        registro.setMartes(chkMartes.isSelected());
        registro.setMiercoles(chkMiercoles.isSelected());
        registro.setJueves(chkJuevez.isSelected());
        registro.setViernes(chkViernes.isSelected());

        try {

            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarHorarios(?,?,?,?,?,?,?)}");
            stmt.setTime(1, registro.getHorarioEntrada());
            stmt.setTime(2, registro.getHorarioSalida());
            stmt.setBoolean(3, registro.isLunes());
            stmt.setBoolean(4, registro.isMartes());
            stmt.setBoolean(5, registro.isMiercoles());
            stmt.setBoolean(6, registro.isJueves());
            stmt.setBoolean(7, registro.isViernes());
            stmt.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eliminarHoarario() {

        try {
            PreparedStatement stmt;

            stmt = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarHorarios(?)}");

            stmt.setInt(1, ((Horarios) tblHorarios.getSelectionModel().getSelectedItem()).getId());
            stmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void editarHorarios() {
        ArrayList<TextField> listaTetxField = new ArrayList<>();

        listaTetxField.add(txtId);

        ArrayList<CheckBox> listaCheckBox = new ArrayList<>();

        listaCheckBox.add(chkLunes);
        listaCheckBox.add(chkMartes);
        listaCheckBox.add(chkMiercoles);
        listaCheckBox.add(chkJuevez);
        listaCheckBox.add(chkViernes);

        if (escenarioPrincipal.validar2(listaTetxField, listaCheckBox)) {

            Horarios registro = new Horarios();

            registro.setId(Integer.parseInt(txtId.getText()));
            registro.setHorarioEntrada((Time.valueOf(tpHorarioEntrada.getValue())));
            registro.setHorarioSalida((Time.valueOf(tpHorarioSalida.getValue())));
            registro.setLunes(chkLunes.isSelected());
            registro.setMartes(chkMartes.isSelected());
            registro.setMiercoles(chkMiercoles.isSelected());
            registro.setJueves(chkJuevez.isSelected());
            registro.setViernes(chkViernes.isSelected());

            try {

                PreparedStatement stmt;
                stmt = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarHorarios(?,?,?,?,?,?,?,?)}");
                stmt.setInt(1, registro.getId());
                stmt.setTime(2, registro.getHorarioEntrada());
                stmt.setTime(3, registro.getHorarioSalida());
                stmt.setBoolean(4, registro.isLunes());
                stmt.setBoolean(5, registro.isMartes());
                stmt.setBoolean(6, registro.isMiercoles());
                stmt.setBoolean(7, registro.isJueves());
                stmt.setBoolean(8, registro.isViernes());
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
            txtId.setText(String.valueOf(((Horarios) tblHorarios.getSelectionModel().getSelectedItem()).getId()));
            tpHorarioEntrada.setValue(((Horarios) tblHorarios.getSelectionModel().getSelectedItem()).getHorarioEntrada().toLocalTime());
            tpHorarioSalida.setValue(((Horarios) tblHorarios.getSelectionModel().getSelectedItem()).getHorarioSalida().toLocalTime());
            chkLunes.setSelected(((Horarios) tblHorarios.getSelectionModel().getSelectedItem()).isLunes());
            chkMartes.setSelected(((Horarios) tblHorarios.getSelectionModel().getSelectedItem()).isMartes());
            chkMiercoles.setSelected(((Horarios) tblHorarios.getSelectionModel().getSelectedItem()).isMiercoles());
            chkJuevez.setSelected(((Horarios) tblHorarios.getSelectionModel().getSelectedItem()).isJueves());
            chkViernes.setSelected(((Horarios) tblHorarios.getSelectionModel().getSelectedItem()).isViernes());
        }

    }

    private void habilitarCampos() {
        txtId.setEditable(false);

        tpHorarioEntrada.setDisable(false);
        tpHorarioSalida.setDisable(false);
        chkLunes.setDisable(false);
        chkMartes.setDisable(false);
        chkMiercoles.setDisable(false);
        chkJuevez.setDisable(false);
        chkViernes.setDisable(false);
    }

    private void deshabilitarCampos() {
        txtId.setEditable(false);
        tpHorarioEntrada.setDisable(true);
        tpHorarioSalida.setDisable(true);
        chkLunes.setDisable(true);
        chkMartes.setDisable(true);
        chkMiercoles.setDisable(true);
        chkJuevez.setDisable(true);
        chkViernes.setDisable(true);
    }

    private void limpiarCampos() {
        txtId.clear();
        tpHorarioEntrada.getEditor().clear();
        tpHorarioSalida.getEditor().clear();
        chkLunes.setSelected(false);
        chkMartes.setSelected(false);
        chkMiercoles.setSelected(false);
        chkJuevez.setSelected(false);
        chkViernes.setSelected(false);
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

                if (tpHorarioEntrada.getValue() != null && tpHorarioSalida.getValue() != null) {
                    agregarHorarios();
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
                    alert.setContentText("Debe seleccionar una hora");
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
                if (tblHorarios.getSelectionModel().getSelectedItem() != null) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setHeaderText(null);
                    alert.setTitle("Confirmación");
                    alert.setContentText("¿Estas seguro de eliminar el elemento seleccionado?");
                    Optional<ButtonType> result = alert.showAndWait();

                    if (result.get() == ButtonType.OK) {

                        try {
                            eliminarHoarario();
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
                if (tblHorarios.getSelectionModel().getSelectedItem() != null) {
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
                if (tpHorarioEntrada.getValue() != null && tpHorarioSalida.getValue() != null) {
                editarHorarios();
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
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setTitle("KINAL MALL");
                    alert.setContentText("Debe seleccionar una hora");
                    alert.showAndWait();}
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
                break;
            case NINGUNO:
                Map parametros = new HashMap();
                GenerarReporte.getInstance().mostrarReporte("ReporteHorarios.jasper", "Reporte Horarios", parametros);
                break;
        }
    }

    @FXML
    private void mostrarVistaMenuPrincipal(MouseEvent event) {
        this.escenarioPrincipal.mostrarEmpleados();
    }

}
