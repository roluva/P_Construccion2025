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
import javafxappescolar.modelo.pojo.Alumno;
import javafxappescolar.modelo.pojo.ResultadoOperacion;

/**
 *
 * @author rodrigoluna
 */
public class AlumnoDAO {
    public static ArrayList<Alumno> obtenerAlumnos() throws SQLException{
         ArrayList<Alumno> alumnos = new ArrayList();
         Connection conexionBD = ConexionBD.abrirConexion();
         if(conexionBD != null){
             String consulta ="select idAlumno, a.nombre, apellidoPaterno, apellidoMaterno, matricula, email, a.idCarrera, fechaNacimiento, c.nombre AS 'Carrera', " 
                     +"c.idFacultad, f.nombre AS 'Facultad' " + 
                     "from alumno a " + 
                     "inner join carrera c on c.idCarrera = a.idCarrera " + 
                     "inner join facultad f on f.idFacultad = c.idFacultad";
             PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
             ResultSet resultado = sentencia.executeQuery();
             while(resultado.next()){
                 alumnos.add(convertirRegistroAlumno(resultado));
             }
             sentencia.close();
             resultado.close();
             conexionBD.close();
         }else{
             throw new SQLException("Sin conexion a la base de datos :(");
         }
         return alumnos;
    }
    
    public static byte[] obtenerFotoAlumno(int idAlumno) throws SQLException{
        byte[] foto = null;
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            String consulta = "SELECT foto FROM alumno WHERE idAlumno = ?";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            sentencia.setInt(1, idAlumno);
            ResultSet resultado = sentencia.executeQuery();
            if (resultado.next()) {
                foto = resultado.getBytes("foto");
            }
            resultado.close();
            sentencia.close();
            conexionBD.close();
        } else {
            throw new SQLException("Sin conexión a la base de datos :(");
        }
        return foto;
    }
    
    public static boolean verificarExistenciaMatricula(String matricula) throws SQLException{
        boolean existe = false;
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            String consulta = "SELECT COUNT(*) AS total FROM alumno WHERE matricula = ?";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            sentencia.setString(1, matricula);
            ResultSet resultado = sentencia.executeQuery();
            if (resultado.next()) {
                existe = resultado.getInt("total") > 0;
            }
            resultado.close();
            sentencia.close();
            conexionBD.close();
        } else {
            throw new SQLException("Sin conexión a la BD");
        }
        return existe;
    }
    
    public static ResultadoOperacion editarAlumno (Alumno alumno) throws SQLException{
        ResultadoOperacion resultado = new ResultadoOperacion();
        Connection conexionBD = ConexionBD.abrirConexion();

        if (conexionBD != null) {
            String consulta = "UPDATE alumno SET nombre = ?, apellidoPaterno = ?, apellidoMaterno = ?, email = ?, idCarrera = ?, fechaNacimiento = ?, foto = ? WHERE idAlumno = ?";
            PreparedStatement ps = conexionBD.prepareStatement(consulta);
            ps.setString(1, alumno.getNombre());
            ps.setString(2, alumno.getApellidoPaterno());
            ps.setString(3, alumno.getApellidoMaterno());
            ps.setString(4, alumno.getEmail());
            ps.setInt(5, alumno.getIdCarrera());
            ps.setString(6, alumno.getFechaNacimiento());
            ps.setBytes(7, alumno.getFoto());
            ps.setInt(8, alumno.getIdAlumno());

            int filasActualizadas = ps.executeUpdate();

            if (filasActualizadas == 1) {
                resultado.setError(false);
                resultado.setMensaje("Alumno editado correctamente");
            } else {
                resultado.setError(true);
                resultado.setMensaje("Lo sentimos no se pudo editar el alumno, intente más tarde");
            }

            ps.close();
            conexionBD.close();
        } else {
            throw new SQLException("Error: Sin conexion a la base de datos");
        }
        return resultado;
    }
    
    public static ResultadoOperacion eliminarAlumno (int idAlumno) throws SQLException{
        ResultadoOperacion resultado = new ResultadoOperacion();
        Connection conexionBD = ConexionBD.abrirConexion();

        if (conexionBD != null) {
            String consultaSQL = "DELETE FROM alumno WHERE idAlumno = ?";
            PreparedStatement ps = conexionBD.prepareStatement(consultaSQL);
            ps.setInt(1, idAlumno);

            int filasEliminadas = ps.executeUpdate();

            if (filasEliminadas == 1) {
                resultado.setError(false);
                resultado.setMensaje("Alumno eliminado correctamente");
            } else {
                resultado.setError(true);
                resultado.setMensaje("Lo sentimos no se pudo eliminar el alumno, intente más tarde");
            }

            ps.close();
            conexionBD.close();
        } else {
            throw new SQLException("Error: Sin conexion a la base de datos");
        }
        return resultado;
    }
    
    private static Alumno convertirRegistroAlumno (ResultSet resultado) throws SQLException{
        Alumno alumno = new Alumno();
        alumno.setIdAlumno(resultado.getInt("idAlumno"));
        alumno.setNombre(resultado.getString("nombre"));
        alumno.setApellidoPaterno(resultado.getString("apellidoPaterno"));
        alumno.setApellidoMaterno(resultado.getString("apellidoMaterno"));
        alumno.setMatricula(resultado.getString("matricula"));
        alumno.setEmail(resultado.getString("email"));
        alumno.setIdCarrera(resultado.getInt("idCarrera"));
        alumno.setNombreCarrera(resultado.getString("carrera"));
        alumno.setIdFacultad(resultado.getInt("idFacultad"));
        alumno.setNombreFacultad(resultado.getString("facultad"));
        alumno.setFechaNacimiento(resultado.getString("fechaNacimiento"));
        
        return alumno;
    }
    
    public static ResultadoOperacion registrarAlumno(Alumno alumno) throws SQLException{
        ResultadoOperacion resultado = new ResultadoOperacion();
        Connection conexionBD = ConexionBD.abrirConexion();
        if(conexionBD != null){
            String sentencia ="INSERT INTO alumno (nombre, apellidoPaterno, apellidoMaterno, matricula, email, idCarrera, fechaNacimiento, foto)"
                    + " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement prepararSentencia = conexionBD.prepareStatement(sentencia);
            prepararSentencia.setString(1, alumno.getNombre());
            prepararSentencia.setString(2, alumno.getApellidoPaterno());
            prepararSentencia.setString(3, alumno.getApellidoMaterno());
            prepararSentencia.setString(4, alumno.getMatricula());
            prepararSentencia.setString(5, alumno.getEmail());
            prepararSentencia.setInt(6, alumno.getIdCarrera());
            prepararSentencia.setString(7, alumno.getFechaNacimiento());
            prepararSentencia.setBytes(8, alumno.getFoto());
            
            int filasAfectadas = prepararSentencia.executeUpdate();
            if (filasAfectadas == 1){
                resultado.setError(false);
                resultado.setMensaje("Alumno(a) registrado correctamente.");
            }else{
                resultado.setError(true);
                resultado.setMensaje("Lo sentimos. Por el momento no se puede registrar la información del alumno(a)" 
                        + ". Por favor intentalo más tarde.");
            }
            prepararSentencia.close();
            conexionBD.close();
        }else{
            throw new SQLException("Sin conexion. Error en la BD");
        }
        return resultado;
    }
}
