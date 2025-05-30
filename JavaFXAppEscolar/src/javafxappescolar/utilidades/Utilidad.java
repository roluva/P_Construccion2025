/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javafxappescolar.utilidades;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.stage.Stage;

/**
 *
 * @author rodrigoluna
 */
public class Utilidad {

    public Utilidad() {
    }
    public static void mostrarAlertaSimple (Alert.AlertType tipo, String titulo, String contenido){
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }
    
    public static boolean mostrarAlertaConfirmacion (String titulo, String contenidio){
        Alert alertaConfirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        alertaConfirmacion.setTitle(titulo);
        alertaConfirmacion.setHeaderText(titulo);
        alertaConfirmacion.setContentText(titulo);
        Optional <ButtonType> seleccion = alertaConfirmacion.showAndWait();
        
        return seleccion.get() == ButtonType.OK;
    }
    
    public static Stage getEscenarioComponente (Control componente){
        return (Stage) componente.getScene().getWindow();
    }
    
}
