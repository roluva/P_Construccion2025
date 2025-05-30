/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxappescolar.controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafxappescolar.JavaFXAppEscolar;
import javafxappescolar.modelo.pojo.Usuario;

/**
 * FXML Controller class
 *
 * @author rodrigoluna
 */
public class FXMLPrincipalController implements Initializable {
    private Usuario usuarioSesion;
    @FXML
    private Label lbNombre;
    @FXML
    private Label lbUsuario;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void inicializarInformacion (Usuario usuarioSesion){
        this.usuarioSesion = usuarioSesion;
        cargarInformacionUsuario();
    }
    
    private void cargarInformacionUsuario(){
        if(usuarioSesion != null){
            lbNombre.setText(usuarioSesion.toString());
            lbUsuario.setText(usuarioSesion.getUsername());
        }
    }

    @FXML
    private void btnClicCerrarSesion(ActionEvent event) {
        try {
            FXMLLoader cargador = new FXMLLoader(JavaFXAppEscolar.class.getResource("vista/FXMLInicioSesion.fxml"));
            Parent vistaInicioSesion = cargador.load();
        
            Scene escenaInicio = new Scene(vistaInicioSesion);
            Stage escenarioActual = (Stage) lbNombre.getScene().getWindow();

            escenarioActual.setScene(escenaInicio);
            escenarioActual.setTitle("Inicio de Sesión");
            escenarioActual.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void btnClicAdminAlumno(ActionEvent event) {
        try {
            Stage escenarioAdmin = new Stage();
            Parent vista = FXMLLoader.load(JavaFXAppEscolar.class.getResource("vista/FXMLAdminAlumno.fxml"));
            Scene esecena =  new Scene(vista);
            escenarioAdmin.setScene(esecena);
            escenarioAdmin.setTitle("Administrador de Alumnos");
            escenarioAdmin.initModality(Modality.APPLICATION_MODAL);
            escenarioAdmin.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
}
