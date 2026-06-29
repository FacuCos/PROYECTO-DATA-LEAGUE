package ConsultasBD;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import ClasesObjetos.Clubes;
import Utilidades.conexionBD;

public class ClubesBD {

	public List<Clubes> listarClubes()
	{
		List<Clubes> clubes = new ArrayList<>();
		String consulta = "SELECT * FROM clubes";
 
		try
		{
			Connection BD = conexionBD.ConexionBD();
			PreparedStatement peticionSQL = BD.prepareStatement(consulta);
			ResultSet filas = peticionSQL.executeQuery();
 
			while(filas.next())
			{
				clubes.add(obtenerClub(filas));
			}
		}
		catch(Exception error)
		{
			error.printStackTrace();
		}
 
		return clubes;
	}
 
	public Clubes buscarClubPorId(int idClub)
	{
		String consulta = "SELECT * FROM clubes WHERE id = ?";
		Clubes club = null;
 
		try
		{
			Connection BD = conexionBD.ConexionBD();
			PreparedStatement peticionSQL = BD.prepareStatement(consulta);
			peticionSQL.setInt(1, idClub);
			ResultSet filas = peticionSQL.executeQuery();
 
			if(filas.next())
			{
				club = obtenerClub(filas);
			}
		}
		catch(Exception error)
		{
			error.printStackTrace();
		}
 
		return club;
	}
 
	public int insertarClub(Clubes club)
	{
		String consulta = "INSERT INTO clubes (nombre, estadio_nombre, direccion_estadio, escudo_ruta) VALUES (?, ?, ?, ?)";
		int idGenerado = -1;
 
		try
		{
			Connection BD = conexionBD.ConexionBD();
			PreparedStatement peticionSQL = BD.prepareStatement(consulta, Statement.RETURN_GENERATED_KEYS);
 
			peticionSQL.setString(1, club.getNombre());
			peticionSQL.setString(2, club.getEstadioNombre());
			peticionSQL.setString(3, club.getDireccionEstadio());
			peticionSQL.setString(4, club.getEscudoRuta());
 
			int filasAfectadas = peticionSQL.executeUpdate();
 
			if(filasAfectadas > 0)
			{
				ResultSet claveGenerada = peticionSQL.getGeneratedKeys();
				if(claveGenerada.next())
				{
					idGenerado = claveGenerada.getInt(1);
					club.setId(idGenerado);
				}
			}
		}
		catch(Exception error)
		{
			error.printStackTrace();
		}
 
		return idGenerado;
	}
 
	public boolean actualizarClub(Clubes club)
	{
		String consulta = "UPDATE clubes SET nombre = ?, estadio_nombre = ?, direccion_estadio = ?, escudo_ruta = ? WHERE id = ?";
		boolean exito = false;
 
		try
		{
			Connection BD = conexionBD.ConexionBD();
			PreparedStatement peticionSQL = BD.prepareStatement(consulta);
 
			peticionSQL.setString(1, club.getNombre());
			peticionSQL.setString(2, club.getEstadioNombre());
			peticionSQL.setString(3, club.getDireccionEstadio());
			peticionSQL.setString(4, club.getEscudoRuta());
			peticionSQL.setInt(5, club.getId());
 
			int filasAfectadas = peticionSQL.executeUpdate();
			exito = filasAfectadas > 0;
		}
		catch(Exception error)
		{
			error.printStackTrace();
		}
 
		return exito;
	}
 
	public boolean eliminarClub(int idClub)
	{
		String consulta = "DELETE FROM clubes WHERE id = ?";
		boolean exito = false;
 
		try
		{
			Connection BD = conexionBD.ConexionBD();
			PreparedStatement peticionSQL = BD.prepareStatement(consulta);
			peticionSQL.setInt(1, idClub);
 
			int filasAfectadas = peticionSQL.executeUpdate();
			exito = filasAfectadas > 0;
		}
		catch(Exception error)
		{
			error.printStackTrace();
		}
 
		return exito;
	}
 
	private Clubes obtenerClub(ResultSet resultados) throws SQLException
	{
		Clubes club = new Clubes();
		club.setId(resultados.getInt("id"));
		club.setNombre(resultados.getString("nombre"));
		club.setEstadioNombre(resultados.getString("estadio_nombre"));
		club.setDireccionEstadio(resultados.getString("direccion_estadio"));
		club.setEscudoRuta(resultados.getString("escudo_ruta"));
 
		return club;
	}
}
