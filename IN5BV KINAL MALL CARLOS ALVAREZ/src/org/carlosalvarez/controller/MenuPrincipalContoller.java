package org.carlosalvarez.controller;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import org.carlosalvarez.system.Principal;

/**
 *
 * @author Carlos Adolfo Alvarez Crúz
 * @date 5 may 2021
 * @time 17:18:19
 * Codigo tecnico: IN5BV
 */
public class MenuPrincipalContoller implements Initializable{

    private Principal escenarioPrincipal;
    @FXML
    private MenuItem menuAdministracion;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
    
    public void validarPermiso(){
        if(escenarioPrincipal.getUsuario().getRolid() != 1){
            menuAdministracion.setDisable(true);
        }
    }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    @FXML
    private void mostrarVistaAutor(ActionEvent event) {
        escenarioPrincipal.mostrarAutor();
    }
    
    @FXML
    private void mostrarVistaAdministracion(){
        escenarioPrincipal.mostrarAdministracion();
    }

    
    @FXML
    private void mostrarVistaClientes(ActionEvent event) {
        escenarioPrincipal.mostrarClientes();
    }

    //private void mostrarVistadepartamentos(ActionEvent event) {
      //  escenarioPrincipal.mostrarDepartamentos();
    //}

    @FXML
    private void mostrarVistaProveedores(ActionEvent event) {
        escenarioPrincipal.mostrarProveedores();
    }

    /*private void mostrarVistaHorarios(ActionEvent event) {
        escenarioPrincipal.mostrarHorarios();
    }

    private void mostrarVistaCuentasPorCobrar(ActionEvent event) {
        escenarioPrincipal.mostrarCuentasPorCobrar();
    }
    */
    @FXML
    private void mostrarVistaEmpleados(ActionEvent event) {
        escenarioPrincipal.mostrarEmpleados();
    }

    @FXML
    private void CerrarSesion(ActionEvent event) {
        escenarioPrincipal.mostrarLogin();
    }
    
}
