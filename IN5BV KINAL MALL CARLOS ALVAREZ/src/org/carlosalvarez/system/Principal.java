package org.carlosalvarez.system;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.carlosalvarez.controller.AdministracionController;
import org.carlosalvarez.controller.AutorController;
import org.carlosalvarez.controller.CargosController;
import org.carlosalvarez.controller.ClientesController;
import org.carlosalvarez.controller.CuentasPorCobrarController;
import org.carlosalvarez.controller.CuentasPorPagarController;
import org.carlosalvarez.controller.DepartamentosController;
import org.carlosalvarez.controller.EmpleadosController;
import org.carlosalvarez.controller.HorariosController;
import org.carlosalvarez.controller.LocalesController;
import org.carlosalvarez.controller.MenuPrincipalContoller;
import org.carlosalvarez.controller.ProveedoresController;
import org.carlosalvarez.controller.TipoClienteController;
import org.carlosalvarez.controller.LoginController;
import java.util.Base64;
import org.carlosalvarez.bean.Usuario;

/**
 *
 * @author Carlos Adolfo Alvarez Crúz
 * @date 5 may 2021
 * @time 17:27:49 Codigo tecnico: IN5BV
 */
public class Principal extends Application {

    private Stage escenarioPrincipal;
    private Scene escena;
    private final String PAQUETE_VIEW = "/org/carlosalvarez/view/";
    private final String PAQUETE_IMAGES = "/org/carlosalvarez/resource//images/";
    private final String PAQUETE_CSS = "/org/carlosalvarez/resource/css/";

    private Usuario usuario;

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    
    
    public static void main(String[] args) {

        launch(args);

    }

    @Override
    public void start(Stage stage) throws Exception {
        this.escenarioPrincipal = stage;
        //escenarioPrincipal.getIcons().add(new Image(PAQUETE_IMAGES + "logo.png"));
        stage.setTitle("KINAL MALL -Carlos Alvarez");
        
        usuario = new Usuario();
        
        mostrarLogin();
        //mostrarMenuPrincipal();
    }

    public void mostrarLogin(){
        try {
            
            LoginController lo = (LoginController) cambiarEscena("LoginView.fxml", 917, 360);
            lo.setEscenarioPrincipal(this);
            
        } catch (Exception e) {
            System.out.println("Se produjo un error al mostrar login");
            e.printStackTrace();
        }
    }
    
    public void mostrarMenuPrincipal() {
        try {
            MenuPrincipalContoller menuController = (MenuPrincipalContoller) cambiarEscena("MenuPrincipalView.fxml", 1044, 584);
            menuController.setEscenarioPrincipal(this);
            menuController.validarPermiso();
        } catch (Exception e) {
            System.out.println("Se produjo un error al mostrar la vista del menú principal");
            e.printStackTrace();
        }
        /*
         catch (NullPointerException ex) {
            System.out.println("Se produjo un error de tipo nullExeption");9
        } catch (Exception e) {
            System.out.println("Otro error");
        }*/
    }

    public void mostrarAutor() {

        try {
            AutorController autorController = (AutorController) cambiarEscena("AutorView.fxml", 1044, 591);
            autorController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            System.out.println("Se produjo un error al mostrar la vista del autor ");
            e.printStackTrace();
        }

    }

    public void mostrarAdministracion() {
        try {
            AdministracionController adminController = (AdministracionController) cambiarEscena("AdministracionView.fxml", 1044, 591);
            adminController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            System.out.println("Se produjo un error al mostrar la vista de administracion");
            e.printStackTrace();
        }
    }

    public void mostrarTipoCliente() {

        try {
            TipoClienteController tipoClienteController = (TipoClienteController) cambiarEscena("TipoClienteView.fxml", 1044, 591);
            tipoClienteController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            System.out.println("Se produjo un error al mostra la vista TipoClientes");
            e.printStackTrace();
        }

    }

    public void mostrarDepartamentos() {
        try {
            DepartamentosController departamentosController = (DepartamentosController) cambiarEscena("DepartamentosView.fxml", 1044, 591);
            departamentosController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            System.out.println("Se produjo un error al mostrar la vista Departamentos");
            e.printStackTrace();
        }
    }

    public void mostrarLocales() {
        try {
            LocalesController localesController = (LocalesController) cambiarEscena("LocalesView.fxml", 1044, 591);
            localesController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            System.out.println("Se produjo un error al mostrar la vista Locales");
            e.printStackTrace();
        }
    }

    public void mostrarClientes() {
        try {
            ClientesController clientesController = (ClientesController) cambiarEscena("ClientesView.fxml", 1044, 591);
            clientesController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            System.out.println("Se produjo un error al mostrar la vista de clientes");
            e.printStackTrace();
        }
    }

    public void mostrarProveedores() {
        try {
            ProveedoresController proveedoresController = (ProveedoresController) cambiarEscena("ProveedoresView.fxml", 1044, 591);
            proveedoresController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            System.out.println("Se produjo un error al mostrar la vista Proveedores");
            e.printStackTrace();
        }
    }

    public void mostrarHorarios() {
        try {
            HorariosController horariosController = (HorariosController) cambiarEscena("HorariosView.fxml", 1044, 591);
            horariosController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            System.out.println("Se produjo un error al mostrar la vista Horarios");
            e.printStackTrace();
        }
    }

    public void mostrarCuentasPorPagar() {

        try {
            CuentasPorPagarController cuentasPorPagarController = (CuentasPorPagarController) cambiarEscena("CuentasPorPagarView.fxml", 1044, 591);
            cuentasPorPagarController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            System.out.println("Se produjo un error al mostrar la vista Cuentas por Pagar");
            e.printStackTrace();
        }

    }

    public void mostrarCuentasPorCobrar() {

        try {
            CuentasPorCobrarController cuentasPorCobrarController = (CuentasPorCobrarController) cambiarEscena("CuentasPorCobrarView.fxml", 1044, 591);
            cuentasPorCobrarController.setEscenarioPrincipal(this);

        } catch (Exception e) {
            System.out.println("Se produjo un error al mostrar la vista Cuentas por Cobrar");
            e.printStackTrace();
        }

    }
    
    public void mostrarEmpleados(){
        try {
            EmpleadosController empleadosController = (EmpleadosController) cambiarEscena("EmpleadosView.fxml", 1044, 591);
            empleadosController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            System.out.println("Error al mostrar la vista empleados");
            e.printStackTrace();
        }
    }
    
    public void mostrarCargos(){
        try {
            CargosController cargosController = (CargosController) cambiarEscena("CargosView.fxml", 1044, 591);
            cargosController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            System.out.println("Error al mostrar la vista Cargos");
            e.printStackTrace();
        }
    }

    public boolean validarDatos(ArrayList<TextField> listadoCampos) {
        boolean respuesta = true;

        for (TextField campoTexto : listadoCampos) {

            if (campoTexto.getText().trim().isEmpty()) {
                return false;
            }

        }

        return respuesta;
    }

    public boolean validar3(ArrayList<TextField> listadoCampos, ArrayList<Spinner> listaSpinner, ArrayList<ComboBox> listaComboBox) {
        boolean respuesta = true;
        
        for (ComboBox combobox : listaComboBox) {

            if (combobox.getSelectionModel().getSelectedItem() == null) {
                return false;
            }
        }

        for (Spinner spinner : listaSpinner) {

            if (listaSpinner.isEmpty()) {
                return false;
            }
        }

        for (TextField campoTexto : listadoCampos) {

            if (campoTexto.getText().trim().isEmpty()) {
                return false;
            }
        }
        return respuesta;
    }
    
    public boolean validar2(ArrayList<TextField> listadoCampos, ArrayList<CheckBox> listaChecBox) {
        boolean respuesta = true;

        for (CheckBox checkBox : listaChecBox) {

            if (listaChecBox.isEmpty()) {
                return false;
            }
        }

        for (TextField campoTexto : listadoCampos) {

            if (campoTexto.getText().trim().isEmpty()) {
                return false;
            }
        }
        return respuesta;
    }

    public boolean validar(ArrayList<TextField> listadoCampos, ArrayList<ComboBox> listaComboBox) {
        boolean respuesta = true;

        for (ComboBox combobox : listaComboBox) {

            if (combobox.getSelectionModel().getSelectedItem() == null) {
                return false;
            }
        }

        for (TextField campoTexto : listadoCampos) {

            if (campoTexto.getText().trim().isEmpty()) {
                return false;
            }
        }
        return respuesta;
    }

    public Initializable cambiarEscena(String Fxml, int ancho, int alto) throws IOException {
        Initializable resultado;
        FXMLLoader cargadorFXML = new FXMLLoader();
        cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
        cargadorFXML.setLocation(Principal.class.getResource(PAQUETE_VIEW + Fxml));
        InputStream archivo = Principal.class.getResourceAsStream(PAQUETE_VIEW + Fxml);
        escena = new Scene((AnchorPane) cargadorFXML.load(archivo), ancho, alto);
        escena.getStylesheets().add(PAQUETE_CSS + "estilosKinalMall.css"); //Vista 
        this.escenarioPrincipal.setScene(escena);
        this.escenarioPrincipal.sizeToScene();
        this.escenarioPrincipal.setResizable(false);
        this.escenarioPrincipal.centerOnScreen();
        this.escenarioPrincipal.show();
        resultado = (Initializable) cargadorFXML.getController();
        return resultado;

    }
}
