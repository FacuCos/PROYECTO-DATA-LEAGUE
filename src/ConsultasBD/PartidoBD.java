package ConsultasBD;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import ClasesObjetos.Arbitro;
import ClasesObjetos.Categorias;
import ClasesObjetos.Clubes;
import ClasesObjetos.Jugador;
import ClasesObjetos.Partidos;
import Utilidades.conexionBD;

public class PartidoBD {
	
	private static final String SELECT_BASE =
			"SELECT p.id, p.fecha_liga, p.fecha_partido, p.gol_local, p.gol_visitante, p.modificado, "
			+ "cl.id AS local_id, cl.nombre AS local_nombre, cl.estadio_nombre AS local_estadio_nombre, "
			+ "cl.direccion_estadio AS local_direccion_estadio, cl.escudo_ruta AS local_escudo_ruta, "
			+ "cv.id AS visitante_id, cv.nombre AS visitante_nombre, cv.estadio_nombre AS visitante_estadio_nombre, "
			+ "cv.direccion_estadio AS visitante_direccion_estadio, cv.escudo_ruta AS visitante_escudo_ruta, "
			+ "catl.id AS catlocal_id, catl.division AS catlocal_division, catl.cantidad_jugadores AS catlocal_cantidad, "
			+ "catl.edad_minima AS catlocal_edadmin, catl.edad_maxima AS catlocal_edadmax, "
			+ "catv.id AS catvisitante_id, catv.division AS catvisitante_division, catv.cantidad_jugadores AS catvisitante_cantidad, "
			+ "catv.edad_minima AS catvisitante_edadmin, catv.edad_maxima AS catvisitante_edadmax, "
			+ "a.id AS arbitro_id, a.nombre AS arbitro_nombre, a.apellido AS arbitro_apellido, a.dni AS arbitro_dni "
			+ "FROM partidos p "
			+ "JOIN clubes cl ON p.club_local_id = cl.id "
			+ "JOIN clubes cv ON p.club_visitante_id = cv.id "
			+ "JOIN categorias catl ON p.categoria_local_id = catl.id "
			+ "JOIN categorias catv ON p.categoria_visitante_id = catv.id "
			+ "JOIN usuarios a ON p.arbitro_id = a.id";
 
	public List<Partidos> listarPartidos()
	{
		List<Partidos> partidos = new ArrayList<>();
		String consulta = SELECT_BASE;
 
		try
		{
			Connection BD = conexionBD.ConexionBD();
			PreparedStatement peticionSQL = BD.prepareStatement(consulta);
			ResultSet filas = peticionSQL.executeQuery();
 
			while(filas.next())
			{
				partidos.add(obtenerPartido(filas));
			}
		}
		catch(Exception error)
		{
			error.printStackTrace();
		}
 
		return partidos;
	}
 
	public Partidos buscarPartidoPorId(int idPartido)
	{
		String consulta = SELECT_BASE + " WHERE p.id = ?";
		Partidos partido = null;
 
		try
		{
			Connection BD = conexionBD.ConexionBD();
			PreparedStatement peticionSQL = BD.prepareStatement(consulta);
			peticionSQL.setInt(1, idPartido);
			ResultSet filas = peticionSQL.executeQuery();
 
			if(filas.next())
			{
				partido = obtenerPartido(filas);
			}
		}
		catch(Exception error)
		{
			error.printStackTrace();
		}
 
		return partido;
	}
 
	public List<Partidos> listarPartidosPorClub(int idClub)
	{
		List<Partidos> partidos = new ArrayList<>();
		String consulta = SELECT_BASE + " WHERE p.club_local_id = ? OR p.club_visitante_id = ?";
 
		try
		{
			Connection BD = conexionBD.ConexionBD();
			PreparedStatement peticionSQL = BD.prepareStatement(consulta);
			peticionSQL.setInt(1, idClub);
			peticionSQL.setInt(2, idClub);
			ResultSet filas = peticionSQL.executeQuery();
 
			while(filas.next())
			{
				partidos.add(obtenerPartido(filas));
			}
		}
		catch(Exception error)
		{
			error.printStackTrace();
		}
 
		return partidos;
	}
 
	public int insertarPartido(Partidos partido)
	{
		String consulta = "INSERT INTO partidos (arbitro_id, fecha_liga, fecha_partido, gol_local, gol_visitante, "
				+ "club_local_id, club_visitante_id, categoria_local_id, categoria_visitante_id) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		int idGenerado = -1;
 
		try
		{
			Connection BD = conexionBD.ConexionBD();
			PreparedStatement peticionSQL = BD.prepareStatement(consulta, Statement.RETURN_GENERATED_KEYS);
 
			peticionSQL.setInt(1, partido.getArbitro().getId());
			peticionSQL.setString(2, partido.getFechaLiga());
			peticionSQL.setDate(3, Date.valueOf(partido.getFechaPartido()));
			peticionSQL.setInt(4, partido.getGolLocal());
			peticionSQL.setInt(5, partido.getGolVisitante());
			peticionSQL.setInt(6, partido.getClubLocal().getId());
			peticionSQL.setInt(7, partido.getClubVisitante().getId());
			peticionSQL.setInt(8, partido.getCategoriaLocal().getId());
			peticionSQL.setInt(9, partido.getCategoriaVisitante().getId());
 
			int filasAfectadas = peticionSQL.executeUpdate();
 
			if(filasAfectadas > 0)
			{
				ResultSet claveGenerada = peticionSQL.getGeneratedKeys();
				if(claveGenerada.next())
				{
					idGenerado = claveGenerada.getInt(1);
					partido.setIdPartido(idGenerado);
				}
			}
		}
		catch(Exception error)
		{
			error.printStackTrace();
		}
 
		return idGenerado;
	}
 
	/**
	 * RF-032: cada llamada a este metodo marca modificado=1, asi una segunda
	 * llamada puede ser bloqueada por ValidacionPartido antes de siquiera llegar aca.
	 */
	public boolean actualizarResultado(int idPartido, int golLocal, int golVisitante)
	{
		String consulta = "UPDATE partidos SET gol_local = ?, gol_visitante = ?, modificado = 1 WHERE id = ?";
		boolean exito = false;
 
		try
		{
			Connection BD = conexionBD.ConexionBD();
			PreparedStatement peticionSQL = BD.prepareStatement(consulta);
 
			peticionSQL.setInt(1, golLocal);
			peticionSQL.setInt(2, golVisitante);
			peticionSQL.setInt(3, idPartido);
 
			int filasAfectadas = peticionSQL.executeUpdate();
			exito = filasAfectadas > 0;
		}
		catch(Exception error)
		{
			error.printStackTrace();
		}
 
		return exito;
	}
 
	public boolean eliminarPartido(int idPartido)
	{
		String consulta = "DELETE FROM partidos WHERE id = ?";
		boolean exito = false;
 
		try
		{
			Connection BD = conexionBD.ConexionBD();
			PreparedStatement peticionSQL = BD.prepareStatement(consulta);
			peticionSQL.setInt(1, idPartido);
 
			int filasAfectadas = peticionSQL.executeUpdate();
			exito = filasAfectadas > 0;
		}
		catch(Exception error)
		{
			error.printStackTrace();
		}
 
		return exito;
	}
 
	// --- Jugadores convocados (lista abierta, se cargan aparte, no en el JOIN principal) ---
 
	public List<Jugador> obtenerJugadoresLocal(int idPartido)
	{
		return obtenerJugadoresDePartido(idPartido, "partido_jugadores_local");
	}
 
	public List<Jugador> obtenerJugadoresVisitante(int idPartido)
	{
		return obtenerJugadoresDePartido(idPartido, "partido_jugadores_visitante");
	}
 
	private List<Jugador> obtenerJugadoresDePartido(int idPartido, String tablaPuente)
	{
		List<Jugador> jugadores = new ArrayList<>();
 
		// El nombre de la tabla viene fijo desde el propio codigo (no del usuario), por eso es seguro
		// concatenarlo aca; nunca se debe concatenar un dato que venga de afuera por riesgo de SQL injection
		String consulta = "SELECT j.id, j.nombre, j.apellido, j.dni "
				+ "FROM jugadores j "
				+ "JOIN " + tablaPuente + " pj ON j.id = pj.jugador_id "
				+ "WHERE pj.partido_id = ?";
 
		try
		{
			Connection BD = conexionBD.ConexionBD();
			PreparedStatement peticionSQL = BD.prepareStatement(consulta);
			peticionSQL.setInt(1, idPartido);
			ResultSet filas = peticionSQL.executeQuery();
 
			while(filas.next())
			{
				Jugador jugador = new Jugador();
				jugador.setIdJugador(filas.getInt("id"));
				jugador.setNombre(filas.getString("nombre"));
				jugador.setApellido(filas.getString("apellido"));
				jugador.setDni(filas.getString("dni"));
				jugadores.add(jugador);
			}
		}
		catch(Exception error)
		{
			error.printStackTrace();
		}
 
		return jugadores;
	}
 
	public boolean agregarJugadorLocal(int idPartido, int idJugador)
	{
		return agregarJugadorATabla(idPartido, idJugador, "partido_jugadores_local");
	}
 
	public boolean agregarJugadorVisitante(int idPartido, int idJugador)
	{
		return agregarJugadorATabla(idPartido, idJugador, "partido_jugadores_visitante");
	}
 
	private boolean agregarJugadorATabla(int idPartido, int idJugador, String tablaPuente)
	{
		String consulta = "INSERT INTO " + tablaPuente + " (partido_id, jugador_id) VALUES (?, ?)";
		boolean exito = false;
 
		try
		{
			Connection BD = conexionBD.ConexionBD();
			PreparedStatement peticionSQL = BD.prepareStatement(consulta);
			peticionSQL.setInt(1, idPartido);
			peticionSQL.setInt(2, idJugador);
 
			int filasAfectadas = peticionSQL.executeUpdate();
			exito = filasAfectadas > 0;
		}
		catch(Exception error)
		{
			error.printStackTrace();
		}
 
		return exito;
	}
 
	private Partidos obtenerPartido(ResultSet resultados) throws SQLException
	{
		Partidos partido = new Partidos();
		partido.setIdPartido(resultados.getInt("id"));
		partido.setFechaLiga(resultados.getString("fecha_liga"));
		partido.setFechaPartido(resultados.getDate("fecha_partido").toLocalDate());
		partido.setGolLocal(resultados.getInt("gol_local"));
		partido.setGolVisitante(resultados.getInt("gol_visitante"));
		partido.setModificado(resultados.getBoolean("modificado"));
 
		Clubes clubLocal = new Clubes();
		clubLocal.setId(resultados.getInt("local_id"));
		clubLocal.setNombre(resultados.getString("local_nombre"));
		clubLocal.setEstadioNombre(resultados.getString("local_estadio_nombre"));
		clubLocal.setDireccionEstadio(resultados.getString("local_direccion_estadio"));
		clubLocal.setEscudoRuta(resultados.getString("local_escudo_ruta"));
		partido.setClubLocal(clubLocal);
 
		Clubes clubVisitante = new Clubes();
		clubVisitante.setId(resultados.getInt("visitante_id"));
		clubVisitante.setNombre(resultados.getString("visitante_nombre"));
		clubVisitante.setEstadioNombre(resultados.getString("visitante_estadio_nombre"));
		clubVisitante.setDireccionEstadio(resultados.getString("visitante_direccion_estadio"));
		clubVisitante.setEscudoRuta(resultados.getString("visitante_escudo_ruta"));
		partido.setClubVisitante(clubVisitante);
 
		Categorias categoriaLocal = new Categorias();
		categoriaLocal.setId(resultados.getInt("catlocal_id"));
		categoriaLocal.setDivision(resultados.getString("catlocal_division"));
		categoriaLocal.setCantidadDeJugadores(resultados.getInt("catlocal_cantidad"));
		categoriaLocal.setEdadMinima(resultados.getInt("catlocal_edadmin"));
		categoriaLocal.setEdadMaxima(resultados.getInt("catlocal_edadmax"));
		partido.setCategoriaLocal(categoriaLocal);
 
		Categorias categoriaVisitante = new Categorias();
		categoriaVisitante.setId(resultados.getInt("catvisitante_id"));
		categoriaVisitante.setDivision(resultados.getString("catvisitante_division"));
		categoriaVisitante.setCantidadDeJugadores(resultados.getInt("catvisitante_cantidad"));
		categoriaVisitante.setEdadMinima(resultados.getInt("catvisitante_edadmin"));
		categoriaVisitante.setEdadMaxima(resultados.getInt("catvisitante_edadmax"));
		partido.setCategoriaVisitante(categoriaVisitante);
 
		// RF-040: arbitro liviano (id, nombre, apellido, dni), suficiente para validar identidad
		Arbitro arbitro = new Arbitro();
		arbitro.setId(resultados.getInt("arbitro_id"));
		arbitro.setNombre(resultados.getString("arbitro_nombre"));
		arbitro.setApellido(resultados.getString("arbitro_apellido"));
		arbitro.setDni(resultados.getString("arbitro_dni"));
		partido.setArbitro(arbitro);
 
		// jugadoresLocal y jugadorVisitante quedan en null aqui a proposito:
		// son listas abiertas, se cargan aparte con obtenerJugadoresLocal/obtenerJugadoresVisitante
		return partido;
	}

}
