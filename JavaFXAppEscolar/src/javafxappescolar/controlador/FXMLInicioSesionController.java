/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxappescolar.controlador;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafxappescolar.JavaFXAppEscolar;
import javafxappescolar.modelo.ConexionBD;
import javafxappescolar.modelo.dao.InicioSesionDAO;
import javafxappescolar.modelo.pojo.Usuario;
import javafxappescolar.utilidades.Utilidad;

/**
 * FXML Controller class
 *
 * @author rodrigoluna
 */
public class FXMLInicioSesionController implements Initializable {

    @FXML
    private TextField ingresarUsuario;
    @FXML
    private PasswordField ingresarContraseñaa;
    @FXML
    private Label errorUsuario;
    @FXML
    private Label errorContraseña;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Connection conexionBD = ConexionBD.abrirConexion();
        if(conexionBD != null){
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Conexion Base de Datos");
            alerta.setHeaderText("Conexion establecida");
            alerta.setContentText("La conexion con la Base de Datos se ha realizado correctamente");
            
        alerta.show();
        }
    }   

    @FXML
    private void clicIniciarSesion(ActionEvent event) {
        String username = ingresarUsuario.getText();
        String password = ingresarContraseñaa.getText();
        
        if(validarCampos(username, password))
            validarCredenciales(username, password);
    }
    
    private boolean validarCampos(String username, String password){
        errorUsuario.setText("");
        errorContraseña.setText("");
        
        boolean camposValidos = true;
        
        if (username.isEmpty()){
            errorUsuario.setText("Campo Usuario obligatorio");
            camposValidos = false;
        }
        if(password.isEmpty()){
            errorContraseña.setText("Contraseña obligatoria");
            camposValidos = false;
        }
        return camposValidos;
    }
    
    private void validarCredenciales (String username, String password){
        try {
            Usuario usuarioSesion = InicioSesionDAO.verificarCredenciales(username, password);
            if(usuarioSesion != null){ //Flujo normal
                Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Credenciales correctas", 
                        "Bienvenido (a) " + usuarioSesion.toString() + " al sistema.");
                irPantallaPrincipal(usuarioSesion);
            }else{ //Flujo alterno
                Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Credenciales incorrectas", 
                        "Usuario y/o contraseñas incorrectos. Por favor verifica la información");
            }
        } catch (SQLException ex) { // FLujo excepcion
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Problemas de conexion", ex.getMessage());
        }
    }
    
    private void irPantallaPrincipal (Usuario usuarioSesion){
        try{
            Stage escenarioBase = (Stage) ingresarUsuario.getScene().getWindow();
            //Parent vista = FXMLLoader.load(JavaFXAppEscolar.class.getResource("vista/FXMLPrincipal.fxml"));
            FXMLLoader cargador = new FXMLLoader(JavaFXAppEscolar.class.getResource("vista/FXMLPrincipal.fxml"));
            Parent vista = cargador.load();
            FXMLPrincipalController controlador = cargador.getController();
            controlador.inicializarInformacion(usuarioSesion);
            Scene escenarioPrincipal = new Scene (vista);
            escenarioBase.setScene(escenarioPrincipal);
            escenarioBase.setTitle("Home");
            escenarioBase.showAndWait();
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }
    
}