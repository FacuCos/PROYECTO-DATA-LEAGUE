package Utilidades;

import java.sql.Connection;
import java.sql.DriverManager;

public class conexionBD {
	private static final String RUTA = "jdbc:mysql://localhost:3306/DataLeague";
	private static final String USUARIO = "root";
	private static final String CONTRASEÑA = "A1=m(9miZ*-a78r+t=";
	
	private static Connection conexion = null;
	
	public static Connection ConexionBD ()
	{
		if(conexion == null)
		{
			try
			{
				conexion = DriverManager.getConnection(RUTA, USUARIO, CONTRASEÑA);
				System.out.println("Conexion Establecida");
			}
				catch (Exception error)
					{
						System.out.println("Error en Conexion: "+error.getMessage());
					}
		}
		
		return conexion;
	}
}
