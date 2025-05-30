/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javafxappescolar.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafxappescolar.modelo.ConexionBD;
import javafxappescolar.modelo.pojo.Usuario;

/**
 *
 * @author rodrigoluna
 */
public class InicioSesionDAO {
    
    public static Usuario verificarCredenciales(String username, String password) throws SQLException{
        Usuario usuarioSesion = null;
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null){
            String consulta = "SELECT idUsuario, nombre, apellidoPaterno, apellidoMaterno, username " +
                    "FROM usuario " +
                    "WHERE username = ? AND password = ?" ;
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            sentencia.setString(1, username);
            sentencia.setString(2, password);
            ResultSet resultado = sentencia.executeQuery();
            
            if (resultado.next()){
                usuarioSesion = convertirRegistroUsuario(resultado);
            }
            resultado.close();
            sentencia.close();
            conexionBD.close();
        }else{
            throw new SQLException("Error: Sin conexion a la base de datos.");
        }
        return usuarioSesion;
    }
        
    private static Usuario convertirRegistroUsuario (ResultSet resultado) throws SQLException{
        Usuario usuario = new Usuario();
        
        usuario.setIdUsuario(resultado.getInt(1));
        usuario.setNombre(resultado.getString(2));
        usuario.setApellidoPaterno(resultado.getString(3));
        usuario.setApellidoMaterno(resultado.getString(4) != null ? resultado.getString(4) : "");
        usuario.setUsername(resultado.getString(5));
        
        return usuario;
    }
        
}

