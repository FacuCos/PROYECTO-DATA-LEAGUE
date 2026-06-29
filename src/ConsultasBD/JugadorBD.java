package ConsultasBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
 
import ClasesObjetos.Carnets;
import ClasesObjetos.Categorias;
import ClasesObjetos.Clubes;
import ClasesObjetos.Jugador;
import Utilidades.conexionBD;

public class JugadorBD {


	private static final String SELECT_BASE =
			"SELECT j.id, j.nombre, j.apellido, j.dni, j.fecha_nacimiento, j.domicilio, j.telefono, j.gmail, j.ya_jugo, "
			+ "c.id AS club_id, c.nombre AS club_nombre, c.estadio_nombre, c.direccion_estadio, c.escudo_ruta, "
			+ "cat.id AS categoria_id, cat.division, cat.cantidad_jugadores, cat.edad_minima, cat.edad_maxima, "
			+ "car.id AS carnet_id, car.fecha_emision, car.fecha_vencimiento, car.estado AS carnet_estado, car.ruta_foto_jugador "
			+ "FROM jugadores j "
			+ "JOIN clubes c ON j.club_id = c.id "
			+ "JOIN categorias cat ON j.categoria_id = cat.id "
			+ "JOIN carnets car ON j.id = car.jugador_id";
 
	public List<Jugador> listarJugadores()
	{
		List<Jugador> jugadores = new ArrayList<>();
		String consulta = SELECT_BASE;
 
		try
		{
			Connection BD = conexionBD.ConexionBD();
			PreparedStatement peticionSQL = BD.prepareStatement(consulta);
			ResultSet filas = peticionSQL.executeQuery();
 
			while(filas.next())
			{
				jugadores.add(obtenerJugador(filas));
			}
		}
		catch(Exception error)
		{
			error.printStackTrace();
		}
 
		return jugadores;
	}
 
	public Jugador buscarJugadorPorId(int idJugador)
	{
		String consulta = SELECT_BASE + " WHERE j.id = ?";
		Jugador jugador = null;
 
		try
		{
			Connection BD = conexionBD.ConexionBD();
			PreparedStatement peticionSQL = BD.prepareStatement(consulta);
			peticionSQL.setInt(1, idJugador);
			ResultSet filas = peticionSQL.executeQuery();
 
			if(filas.next())
			{
				jugador = obtenerJugador(filas);
			}
		}
		catch(Exception error)
		{
			error.printStackTrace();
		}
 
		return jugador;
	}
 
	public int insertarJugador(Jugador jugador)
	{
		String consulta = "INSERT INTO jugadores (nombre, apellido, dni, fecha_nacimiento, domicilio, telefono, gmail, ya_jugo, club_id, categoria_id) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		int idGenerado = -1;
 
		try
		{
			Connection BD = conexionBD.ConexionBD();
			PreparedStatement peticionSQL = BD.prepareStatement(consulta, Statement.RETURN_GENERATED_KEYS);
 
			peticionSQL.setString(1, jugador.getNombre());
			peticionSQL.setString(2, jugador.getApellido());
			peticionSQL.setString(3, jugador.getDni());
			peticionSQL.setDate(4, java.sql.Date.valueOf(jugador.getFechaNacimiento()));
			peticionSQL.setString(5, jugador.getDomicilio());
			peticionSQL.setString(6, jugador.getTelefono());
			peticionSQL.setString(7, jugador.getGmail());
			peticionSQL.setBoolean(8, jugador.isYaJugo());
			peticionSQL.setInt(9, jugador.getClub().getId());
			peticionSQL.setInt(10, jugador.getCategoria().getId());
 
			int filasAfectadas = peticionSQL.executeUpdate();
 
			if(filasAfectadas > 0)
			{
				ResultSet claveGenerada = peticionSQL.getGeneratedKeys();
				if(claveGenerada.next())
				{
					idGenerado = claveGenerada.getInt(1);
					jugador.setIdJugador(idGenerado);
				}
			}
		}
		catch(Exception error)
		{
			error.printStackTrace();
		}
 
		return idGenerado;
	}
 
	public boolean actualizarJugador(Jugador jugador)
	{
		String consulta = "UPDATE jugadores SET nombre = ?, apellido = ?, dni = ?, fecha_nacimiento = ?, domicilio = ?, telefono = ?, "
				+ "gmail = ?, ya_jugo = ?, club_id = ?, categoria_id = ? WHERE id = ?";
		boolean exito = false;
 
		try
		{
			Connection BD = conexionBD.ConexionBD();
			PreparedStatement peticionSQL = BD.prepareStatement(consulta);
 
			peticionSQL.setString(1, jugador.getNombre());
			peticionSQL.setString(2, jugador.getApellido());
			peticionSQL.setString(3, jugador.getDni());
			peticionSQL.setDate(4, java.sql.Date.valueOf(jugador.getFechaNacimiento()));
			peticionSQL.setString(5, jugador.getDomicilio());
			peticionSQL.setString(6, jugador.getTelefono());
			peticionSQL.setString(7, jugador.getGmail());
			peticionSQL.setBoolean(8, jugador.isYaJugo());
			peticionSQL.setInt(9, jugador.getClub().getId());
			peticionSQL.setInt(10, jugador.getCategoria().getId());
			peticionSQL.setInt(11, jugador.getIdJugador());
 
			int filasAfectadas = peticionSQL.executeUpdate();
			exito = filasAfectadas > 0;
		}
		catch(Exception error)
		{
			error.printStackTrace();
		}
 
		return exito;
	}
 
	public boolean eliminarJugador(int idJugador)
	{
		String consulta = "DELETE FROM jugadores WHERE id = ?";
		boolean exito = false;
 
		try
		{
			Connection BD = conexionBD.ConexionBD();
			PreparedStatement peticionSQL = BD.prepareStatement(consulta);
			peticionSQL.setInt(1, idJugador);
 
			int filasAfectadas = peticionSQL.executeUpdate();
			exito = filasAfectadas > 0;
		}
		catch(Exception error)
		{
			error.printStackTrace();
		}
 
		return exito;
	}
 
	private Jugador obtenerJugador(ResultSet resultados) throws SQLException
	{
		Jugador jugador = new Jugador();
		jugador.setIdJugador(resultados.getInt("id"));
		jugador.setNombre(resultados.getString("nombre"));
		jugador.setApellido(resultados.getString("apellido"));
		jugador.setDni(resultados.getString("dni"));
		jugador.setFechaNacimiento(resultados.getDate("fecha_nacimiento").toLocalDate());
		jugador.setDomicilio(resultados.getString("domicilio"));
		jugador.setTelefono(resultados.getString("telefono"));
		jugador.setGmail(resultados.getString("gmail"));
		jugador.setYaJugo(resultados.getBoolean("ya_jugo"));
 
		Clubes club = new Clubes();
		club.setId(resultados.getInt("club_id"));
		club.setNombre(resultados.getString("club_nombre"));
		club.setEstadioNombre(resultados.getString("estadio_nombre"));
		club.setDireccionEstadio(resultados.getString("direccion_estadio"));
		club.setEscudoRuta(resultados.getString("escudo_ruta"));
		jugador.setClub(club);
 
		Categorias categoria = new Categorias();
		categoria.setId(resultados.getInt("categoria_id"));
		categoria.setDivision(resultados.getString("division"));
		categoria.setCantidadDeJugadores(resultados.getInt("cantidad_jugadores"));
		categoria.setEdadMinima(resultados.getInt("edad_minima"));
		categoria.setEdadMaxima(resultados.getInt("edad_maxima"));
		jugador.setCategoria(categoria);
 
		Carnets carnet = new Carnets();
		carnet.setIdCarnet(resultados.getInt("carnet_id"));
		carnet.setFechaEmision(resultados.getDate("fecha_emision").toLocalDate());
		carnet.setFechaVencimiento(resultados.getDate("fecha_vencimiento").toLocalDate());
		carnet.setEstado(resultados.getBoolean("carnet_estado"));
		carnet.setRutaFotoJugador(resultados.getString("ruta_foto_jugador"));
		jugador.setCarnet(carnet);
 
		return jugador;
	}

}
