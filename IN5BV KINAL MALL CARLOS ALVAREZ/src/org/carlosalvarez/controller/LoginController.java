/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.carlosalvarez.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import org.carlosalvarez.system.Principal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import javafx.scene.control.Alert;
import org.carlosalvarez.bean.Usuario;
import org.carlosalvarez.db.Conexion;

/**
 * FXML Controller class
 *
 * @author Carlos Adolfo Alvarez Crúz
 */
public class LoginController implements Initializable {

    @FXML
    private Button btningresar;
    @FXML
    private ImageView imgNuevo;
    @FXML
    private Button btnCancelar;
    @FXML
    private ImageView imgEliminar;
    @FXML
    private TextField txtUser;
    @FXML
    private PasswordField txtContra;

    private Principal escenarioPrincipal;

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    private void ingresar(ActionEvent event) {
        String user = txtUser.getText();
        String pass = txtContra.getText();

        getPassword(user);

        if (escenarioPrincipal.getUsuario() != null) {
            if (!(txtUser.getText().isEmpty() || txtContra.getText().isEmpty())) {
                if (pass.equals(getPassword(user))) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setHeaderText(null);
                    alert.setTitle("KINAL MALL");
                    alert.setContentText("Bienvenido " + escenarioPrincipal.getUsuario().getNombre());
                    alert.showAndWait();

                    escenarioPrincipal.mostrarMenuPrincipal();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setTitle("KINAL MALL");
                    alert.setContentText("Contraseña o usuario incorrecto");
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setTitle("KINAL MALL");
                alert.setContentText("Falta llenar datos");
                alert.showAndWait();
            }
        }
        
        txtUser.clear();
        txtContra.clear();

    }

    private String getPassword(String user) {
        String passDecodificado = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarUsuario(?)}");
            pstmt.setString(1, user);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                escenarioPrincipal.getUsuario().setUser(rs.getString("user"));
                escenarioPrincipal.getUsuario().setPass(rs.getString("pass"));
                escenarioPrincipal.getUsuario().setNombre(rs.getString("nombre"));
                escenarioPrincipal.getUsuario().setRolid(rs.getInt("rol"));
                passDecodificado = new String(Base64.getDecoder().decode(escenarioPrincipal.getUsuario().getPass()));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }

                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return passDecodificado;
    }

    @FXML
    private void cancelar(ActionEvent event) {
        System.exit(0);
    }

}
