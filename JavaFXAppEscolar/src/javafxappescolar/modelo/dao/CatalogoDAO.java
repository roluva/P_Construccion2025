/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javafxappescolar.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafxappescolar.modelo.ConexionBD;
import javafxappescolar.modelo.pojo.Carrera;
import javafxappescolar.modelo.pojo.Facultad;

/**
 *
 * @author rodrigoluna
 */
public class CatalogoDAO {
    public static ArrayList<Facultad> obtenerFacultad() throws SQLException{
        ArrayList<Facultad> facultades = new ArrayList();
        Connection conexionBD = ConexionBD.abrirConexion();

        if (conexionBD != null) {
            String consulta = "SELECT idFacultad, nombre FROM facultad";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            ResultSet resultado = sentencia.executeQuery();

            while (resultado.next()) {
                facultades.add(convertirRegistroFacultad(resultado));
            }
            sentencia.close();
            resultado.close();
            conexionBD.close();
        } else {
            throw new SQLException("Error sin conexión a la Base de datos.");
        }
        return facultades;
        
    }
    
    public static ArrayList<Carrera> obtenerCarrerasPorFacultad(int idFacultad) throws SQLException{
        ArrayList<Carrera> carreras = new ArrayList();
        Connection conexionBD = ConexionBD.abrirConexion();

        if (conexionBD != null) {
            String consulta = "SELECT idCarrera, nombre, idFacultad, codigo FROM carrera WHERE idFacultad = ?";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            sentencia.setInt(1, idFacultad);
            ResultSet resultado = sentencia.executeQuery();

            while (resultado.next()) {
                carreras.add(convertirRegistroCarrera(resultado));
            }
            sentencia.close();
            resultado.close();
            conexionBD.close();
        } else {
            throw new SQLException("Error sin conexión a la Base de datos.");

        }
        return carreras;
    }
    
    private static Facultad convertirRegistroFacultad(ResultSet resultado) throws SQLException {
        Facultad facultad = new Facultad();
        facultad.setIdFacultad(resultado.getInt("idFacultad"));
        facultad.setNombre(resultado.getString("nombre"));
        return facultad;
    }
    
     private static Carrera convertirRegistroCarrera(ResultSet resultado) throws SQLException {
        Carrera carrera = new Carrera();
        carrera.setIdCarrera(resultado.getInt("idCarrera"));
        carrera.setNombre(resultado.getString("nombre"));
        carrera.setCodigo(resultado.getString("codigo"));
        return carrera;
    }
    
}

