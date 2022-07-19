package org.carlosalvarez.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Carlos Adolfo Alvarez Crúz
 * @date 27 may 2021
 * @time 16:09:27 Codigo tecnico: IN5BV
 */
public class Conexion {

    private Connection conexion;
    private final String URL;
    private final String SERVER;
    private final String PUERTO;
    private final String DB;
    private final String USER;
    private final String PASS;
    private static Conexion instancia;
    
    private Conexion() {
        
        SERVER = "localhost";
        PUERTO = "3306";
        DB = "IN5BV_KinalMall";
        USER = "root";
        PASS = "admin";
        
        URL = "jdbc:mysql://"+SERVER+ ":" +PUERTO+"/"+DB+"?allowPublicKeyRetrieval=true&serverTimezone=UTC&useSSL=false";
        
        try {
        
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            
            conexion = DriverManager.getConnection(URL,USER,PASS);
            
            System.out.println("Conexión Exitosa");
            
        } catch (ClassNotFoundException e) {
            
            System.out.println("No se encuetra ninguna definicion para la clase");
            e.printStackTrace();
            
        } catch (InstantiationException e) {
            
            System.out.println("No se puede crear una instancia del objeto");
            e.printStackTrace();
            
        } catch (IllegalAccessException e) {
            
            System.out.println("No se tiene los permisos para acceder al paquete");
            e.printStackTrace();
        }catch (SQLException e){
            
            System.out.println("Error en la conexion a la base de datos");
            e.printStackTrace();
            
        } catch (Exception e){
            System.out.println("Se produjo otro error");
            e.printStackTrace();
        }
    }

    public Connection getConexion() {
        return conexion;
    }
    
    public static Conexion getInstance(){
        if(instancia == null){
            instancia = new Conexion();
        }
        return instancia;
    }
    
}
