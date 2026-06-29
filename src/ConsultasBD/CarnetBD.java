package ConsultasBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
 
import ClasesObjetos.Carnets;
import Utilidades.conexionBD;

public class CarnetBD {
	

	public Carnets obtenerCarnetPorJugador(int idJugador)
	{
		String consulta = "SELECT id, fecha_emision, fecha_vencimiento, fecha_limite_renovacion, estado, ruta_foto_jugador "
				+ "FROM carnets WHERE jugador_id = ?";
		Carnets carnet = null;
 
		try
		{
			Connection BD = conexionBD.ConexionBD();
			PreparedStatement peticionSQL = BD.prepareStatement(consulta);
			peticionSQL.setInt(1, idJugador);
			ResultSet filas = peticionSQL.executeQuery();
 
			if(filas.next())
			{
				carnet = obtenerCarnet(filas);
			}
		}
		catch(Exception error)
		{
			error.printStackTrace();
		}
 
		return carnet;
	}
 
	public int insertarCarnet(Carnets carnet, int idJugador)
	{
		String consulta = "INSERT INTO carnets (fecha_emision, fecha_vencimiento, fecha_limite_renovacion, estado, ruta_foto_jugador, jugador_id) "
				+ "VALUES (?, ?, ?, ?, ?, ?)";
		int idGenerado = -1;
 
		try
		{
			Connection BD = conexionBD.ConexionBD();
			PreparedStatement peticionSQL = BD.prepareStatement(consulta, Statement.RETURN_GENERATED_KEYS);
 
			peticionSQL.setDate(1, java.sql.Date.valueOf(carnet.getFechaEmision()));
			peticionSQL.setDate(2, java.sql.Date.valueOf(carnet.getFechaVencimiento()));
			peticionSQL.setDate(3, java.sql.Date.valueOf(carnet.getFechaLimiteRenovacion()));
			peticionSQL.setBoolean(4, carnet.isEstado());
			peticionSQL.setString(5, carnet.getRutaFotoJugador());
			peticionSQL.setInt(6, idJugador);
 
			int filasAfectadas = peticionSQL.executeUpdate();
 
			if(filasAfectadas > 0)
			{
				ResultSet claveGenerada = peticionSQL.getGeneratedKeys();
				if(claveGenerada.next())
				{
					idGenerado = claveGenerada.getInt(1);
				}
			}
		}
		catch(Exception error)
		{
			error.printStackTrace();
		}
 
		return idGenerado;
	}
 
	public boolean actualizarCarnet(Carnets carnet)
	{
		String consulta = "UPDATE carnets SET fecha_emision = ?, fecha_vencimiento = ?, fecha_limite_renovacion = ?, "
				+ "estado = ?, ruta_foto_jugador = ? WHERE id = ?";
		boolean exito = false;
 
		try
		{
			Connection BD = conexionBD.ConexionBD();
			PreparedStatement peticionSQL = BD.prepareStatement(consulta);
 
			peticionSQL.setDate(1, java.sql.Date.valueOf(carnet.getFechaEmision()));
			peticionSQL.setDate(2, java.sql.Date.valueOf(carnet.getFechaVencimiento()));
			peticionSQL.setDate(3, java.sql.Date.valueOf(carnet.getFechaLimiteRenovacion()));
			peticionSQL.setBoolean(4, carnet.isEstado());
			peticionSQL.setString(5, carnet.getRutaFotoJugador());
			peticionSQL.setInt(6, carnet.getIdCarnet());
 
			int filasAfectadas = peticionSQL.executeUpdate();
			exito = filasAfectadas > 0;
		}
		catch(Exception error)
		{
			error.printStackTrace();
		}
 
		return exito;
	}
 
	public boolean eliminarCarnet(int idCarnet)
	{
		String consulta = "DELETE FROM carnets WHERE id = ?";
		boolean exito = false;
 
		try
		{
			Connection BD = conexionBD.ConexionBD();
			PreparedStatement peticionSQL = BD.prepareStatement(consulta);
			peticionSQL.setInt(1, idCarnet);
 
			int filasAfectadas = peticionSQL.executeUpdate();
			exito = filasAfectadas > 0;
		}
		catch(Exception error)
		{
			error.printStackTrace();
		}
 
		return exito;
	}
 
	private Carnets obtenerCarnet(ResultSet resultados) throws SQLException
	{
		Carnets carnet = new Carnets();
		carnet.setIdCarnet(resultados.getInt("id"));
		carnet.setFechaEmision(resultados.getDate("fecha_emision").toLocalDate());
		carnet.setFechaVencimiento(resultados.getDate("fecha_vencimiento").toLocalDate());
		carnet.setFechaLimiteRenovacion(resultados.getDate("fecha_limite_renovacion").toLocalDate());
		carnet.setEstado(resultados.getBoolean("estado"));
		carnet.setRutaFotoJugador(resultados.getString("ruta_foto_jugador"));
		// carnet.setJugador(null) -> se deja en null a propósito, para cortar la referencia circular Carnets<->Jugador
 
		return carnet;
	}

}
