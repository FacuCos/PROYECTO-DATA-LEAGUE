package ConsultasBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
 
import ClasesObjetos.Categorias;
import ClasesObjetos.Clubes;
import Utilidades.conexionBD;

public class CategoriaBD {

	private static final String SELECT_BASE =
			"SELECT cat.id, cat.division, cat.cantidad_jugadores, cat.edad_minima, cat.edad_maxima, "
			+ "c.id AS club_id, c.nombre AS club_nombre, c.estadio_nombre, c.direccion_estadio, c.escudo_ruta "
			+ "FROM categorias cat "
			+ "JOIN clubes c ON cat.club_id = c.id";
 
	public List<Categorias> listarCategorias()
	{
		List<Categorias> categorias = new ArrayList<>();
		String consulta = SELECT_BASE;
 
		try
		{
			Connection BD = conexionBD.ConexionBD();
			PreparedStatement peticionSQL = BD.prepareStatement(consulta);
			ResultSet filas = peticionSQL.executeQuery();
 
			while(filas.next())
			{
				categorias.add(obtenerCategoria(filas));
			}
		}
		catch(Exception error)
		{
			error.printStackTrace();
		}
 
		return categorias;
	}
 
	public List<Categorias> listarCategoriasPorClub(int idClub)
	{
		List<Categorias> categorias = new ArrayList<>();
		String consulta = SELECT_BASE + " WHERE cat.club_id = ?";
 
		try
		{
			Connection BD = conexionBD.ConexionBD();
			PreparedStatement peticionSQL = BD.prepareStatement(consulta);
			peticionSQL.setInt(1, idClub);
			ResultSet filas = peticionSQL.executeQuery();
 
			while(filas.next())
			{
				categorias.add(obtenerCategoria(filas));
			}
		}
		catch(Exception error)
		{
			error.printStackTrace();
		}
 
		return categorias;
	}
 
	public Categorias buscarCategoriaPorId(int idCategoria)
	{
		String consulta = SELECT_BASE + " WHERE cat.id = ?";
		Categorias categoria = null;
 
		try
		{
			Connection BD = conexionBD.ConexionBD();
			PreparedStatement peticionSQL = BD.prepareStatement(consulta);
			peticionSQL.setInt(1, idCategoria);
			ResultSet filas = peticionSQL.executeQuery();
 
			if(filas.next())
			{
				categoria = obtenerCategoria(filas);
			}
		}
		catch(Exception error)
		{
			error.printStackTrace();
		}
 
		return categoria;
	}
 
	public int insertarCategoria(Categorias categoria)
	{
		String consulta = "INSERT INTO categorias (division, cantidad_jugadores, edad_minima, edad_maxima, club_id) "
				+ "VALUES (?, ?, ?, ?, ?)";
		int idGenerado = -1;
 
		try
		{
			Connection BD = conexionBD.ConexionBD();
			PreparedStatement peticionSQL = BD.prepareStatement(consulta, Statement.RETURN_GENERATED_KEYS);
 
			peticionSQL.setString(1, categoria.getDivision());
			peticionSQL.setInt(2, categoria.getCantidadDeJugadores());
			peticionSQL.setInt(3, categoria.getEdadMinima());
			peticionSQL.setInt(4, categoria.getEdadMaxima());
			peticionSQL.setInt(5, categoria.getClub().getId());
 
			int filasAfectadas = peticionSQL.executeUpdate();
 
			if(filasAfectadas > 0)
			{
				ResultSet claveGenerada = peticionSQL.getGeneratedKeys();
				if(claveGenerada.next())
				{
					idGenerado = claveGenerada.getInt(1);
					categoria.setId(idGenerado);
				}
			}
		}
		catch(Exception error)
		{
			error.printStackTrace();
		}
 
		return idGenerado;
	}
 
	public boolean actualizarCategoria(Categorias categoria)
	{
		String consulta = "UPDATE categorias SET division = ?, cantidad_jugadores = ?, edad_minima = ?, "
				+ "edad_maxima = ?, club_id = ? WHERE id = ?";
		boolean exito = false;
 
		try
		{
			Connection BD = conexionBD.ConexionBD();
			PreparedStatement peticionSQL = BD.prepareStatement(consulta);
 
			peticionSQL.setString(1, categoria.getDivision());
			peticionSQL.setInt(2, categoria.getCantidadDeJugadores());
			peticionSQL.setInt(3, categoria.getEdadMinima());
			peticionSQL.setInt(4, categoria.getEdadMaxima());
			peticionSQL.setInt(5, categoria.getClub().getId());
			peticionSQL.setInt(6, categoria.getId());
 
			int filasAfectadas = peticionSQL.executeUpdate();
			exito = filasAfectadas > 0;
		}
		catch(Exception error)
		{
			error.printStackTrace();
		}
 
		return exito;
	}
 
	public boolean eliminarCategoria(int idCategoria)
	{
		String consulta = "DELETE FROM categorias WHERE id = ?";
		boolean exito = false;
 
		try
		{
			Connection BD = conexionBD.ConexionBD();
			PreparedStatement peticionSQL = BD.prepareStatement(consulta);
			peticionSQL.setInt(1, idCategoria);
 
			int filasAfectadas = peticionSQL.executeUpdate();
			exito = filasAfectadas > 0;
		}
		catch(Exception error)
		{
			error.printStackTrace();
		}
 
		return exito;
	}
 
	// RF-016: usado por ValidacionJugador antes de dar de alta o de baja un jugador en esta categoria
	public int contarJugadoresEnCategoria(int idCategoria)
	{
		String consulta = "SELECT COUNT(*) AS total FROM jugadores WHERE categoria_id = ?";
		int total = 0;
 
		try
		{
			Connection BD = conexionBD.ConexionBD();
			PreparedStatement peticionSQL = BD.prepareStatement(consulta);
			peticionSQL.setInt(1, idCategoria);
			ResultSet filas = peticionSQL.executeQuery();
 
			if(filas.next())
			{
				total = filas.getInt("total");
			}
		}
		catch(Exception error)
		{
			error.printStackTrace();
		}
 
		return total;
	}
 
	private Categorias obtenerCategoria(ResultSet resultados) throws SQLException
	{
		Categorias categoria = new Categorias();
		categoria.setId(resultados.getInt("id"));
		categoria.setDivision(resultados.getString("division"));
		categoria.setCantidadDeJugadores(resultados.getInt("cantidad_jugadores"));
		categoria.setEdadMinima(resultados.getInt("edad_minima"));
		categoria.setEdadMaxima(resultados.getInt("edad_maxima"));
 
		Clubes club = new Clubes();
		club.setId(resultados.getInt("club_id"));
		club.setNombre(resultados.getString("club_nombre"));
		club.setEstadioNombre(resultados.getString("estadio_nombre"));
		club.setDireccionEstadio(resultados.getString("direccion_estadio"));
		club.setEscudoRuta(resultados.getString("escudo_ruta"));
		categoria.setClub(club);
 
		return categoria;
	}
}
