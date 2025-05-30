/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxappescolar.controlador;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafxappescolar.Interfaz.INotificacion;
import javafxappescolar.JavaFXAppEscolar;
import javafxappescolar.modelo.dao.AlumnoDAO;
import javafxappescolar.modelo.pojo.Alumno;
import javafxappescolar.modelo.pojo.ResultadoOperacion;
import javafxappescolar.utilidades.Utilidad;

/**
 * FXML Controller class
 *
 * @author rodrigoluna
 */
public class FXMLAdminAlumnoController implements Initializable, INotificacion{

    @FXML
    private TableColumn  colMatricula;
    @FXML
    private TableColumn  colApPaterno;
    @FXML
    private TableColumn  colApMaterno;
    @FXML
    private TableColumn  colNombre;
    @FXML
    private TableColumn  colFacultad;
    @FXML
    private TableColumn  colCarrera;
    @FXML
    private TextField tfBuscar;
    @FXML
    private TableView<Alumno> tvAlumnos;
    private ObservableList<Alumno> alumnos;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarInformacionTabla();
    }    
    
    private void configurarTabla (){
        colMatricula.setCellValueFactory(new PropertyValueFactory("matricula") );
        colNombre.setCellValueFactory(new PropertyValueFactory("nombre"));
        colApMaterno.setCellValueFactory(new PropertyValueFactory("apellidoMaterno"));
        colApPaterno.setCellValueFactory(new PropertyValueFactory("apellidoPaterno"));
        colFacultad.setCellValueFactory(new PropertyValueFactory("nombreFacultad"));
        colCarrera.setCellValueFactory(new PropertyValueFactory("nombreCarrera"));
    }
    
    private void cargarInformacionTabla(){
        try {
            alumnos = FXCollections.observableArrayList();
            ArrayList<Alumno> alumnoDAO = AlumnoDAO.obtenerAlumnos();
            alumnos.addAll(alumnoDAO);
            tvAlumnos.setItems(alumnos);
        } catch (SQLException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.NONE, "Error al cargar",  "Lo sentimos, no se pudo mostrar la información");
            cerrarVentana();
        }
    }
    
    private void cerrarVentana(){
        ((Stage) tfBuscar.getScene().getWindow()).close();
      
    }

    @FXML
    private void btnClicAgregar(ActionEvent event) {
        irFormularioAlumno(false, null);
    }

    @FXML
    private void btnClicModificar(ActionEvent event) {
        Alumno alumno = tvAlumnos.getSelectionModel().getSelectedItem();
        if(alumno != null){
            irFormularioAlumno(true, alumno);
        }else{
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Selecciona un alumno", "Para modificar la información "
                    + "de un alumno debes seleccionar uno");
        }
    }

    @FXML
    private void btnClicEliminar(ActionEvent event) {
        int pos = tvAlumnos.getSelectionModel().getSelectedIndex();
        if(pos >= 0){
            Alumno alumno = alumnos.get(pos);
            String confirmacion = String.format("¿Estas seguro de que quieres eliminar al alumno(a) %s %s?", 
                    alumno.getNombre(), 
                    alumno.getApellidoPaterno());
            if(Utilidad.mostrarAlertaConfirmacion("Eliminar alumno(a)", confirmacion)){
                eliminarAlumno(alumno.getIdAlumno());
            }
        }else{
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Selecciona un alumno", "Selecciona un alumno de la tabla para "
                    + "que pueda ser elminado");
        }
    }
    
    private void irFormularioAlumno(boolean esEdicion, Alumno alumnoEdicion){
        try {
            Stage escenarioFormulario = new Stage();
            FXMLLoader loader = new FXMLLoader(JavaFXAppEscolar.class.getResource("vista/FXMLFormularioAlumno.fxml"));
            Parent vista = loader.load();
            
            FXMLFormularioAlumnoController controlador = loader.getController();
            controlador.inicializarInformacion(esEdicion, alumnoEdicion, this);
            Scene escena = new Scene(vista);
            escenarioFormulario.setScene(escena);
            escenarioFormulario.setTitle("Formulario Alumno");
            escenarioFormulario.initModality(Modality.APPLICATION_MODAL);
            escenarioFormulario.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void operacionExitosa(String tipo, String nombreAlumno) {
        System.out.println("Operacion: " + tipo + ", con el alumno(a): " + nombreAlumno);
        cargarInformacionTabla();
    }
    
    public void eliminarAlumno(int idAlumno){
        try {
            ResultadoOperacion resultado = AlumnoDAO.eliminarAlumno(idAlumno);
            if(!resultado.isError()){
                Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Alumno(a) eliminado", "El registro del alumno "
                        + "fue eliminado correctamente");
                cargarInformacionTabla();
            }else{
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Problemas al eliminar", "Por el momento no se pudo "
                        + "completar la operacion");
            }
        } catch (SQLException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Problemas al eliminar", "Lo sentimos :( por el momento no se "
                    + "puede completar la operacion");
        }
    }
    
}
