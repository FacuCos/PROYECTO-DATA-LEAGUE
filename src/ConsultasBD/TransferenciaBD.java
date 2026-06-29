package ConsultasBD;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
 
import ClasesObjetos.Clubes;
import ClasesObjetos.Jugador;
import ClasesObjetos.Tranferencias;
import Utilidades.conexionBD;

public class TransferenciaBD {
	
	private static final String SELECT_BASE =
			"SELECT t.id, t.fecha_solicitud, t.fecha_respuesta, t.estado, "
			+ "j.id AS jugador_id, j.nombre AS jugador_nombre, j.apellido AS jugador_apellido, j.dni AS jugador_dni, "
			+ "co.id AS origen_id, co.nombre AS origen_nombre, co.estadio_nombre AS origen_estadio_nombre, "
			+ "co.direccion_estadio AS origen_direccion_estadio, co.escudo_ruta AS origen_escudo_ruta, "
			+ "cd.id AS destino_id, cd.nombre AS destino_nombre, cd.estadio_nombre AS destino_estadio_nombre, "
			+ "cd.direccion_estadio AS destino_direccion_estadio, cd.escudo_ruta AS destino_escudo_ruta "
			+ "FROM transferencias t "
			+ "JOIN jugadores j ON t.jugador_id = j.id "
			+ "JOIN clubes co ON t.club_origen_id = co.id "
			+ "JOIN clubes cd ON t.club_destino_id = cd.id";
 
	public List<Tranferencias> listarTransferencias()
	{
		List<Tranferencias> transferencias = new ArrayList<>();
		String consulta = SELECT_BASE;
 
		try
		{
			Connection BD = conexionBD.ConexionBD();
			PreparedStatement peticionSQL = BD.prepareStatement(consulta);
			ResultSet filas = peticionSQL.executeQuery();
 
			while(filas.next())
			{
				transferencias.add(obtenerTransferencia(filas));
			}
		}
		catch(Exception error)
		{
			error.printStackTrace();
		}
 
		return transferencias;
	}
 
	public List<Tranferencias> listarTransferenciasPorEstado(String estado)
	{
		List<Tranferencias> transferencias = new ArrayList<>();
		String consulta = SELECT_BASE + " WHERE t.estado = ?";
 
		try
		{
			Connection BD = conexionBD.ConexionBD();
			PreparedStatement peticionSQL = BD.prepareStatement(consulta);
			peticionSQL.setString(1, estado);
			ResultSet filas = peticionSQL.executeQuery();
 
			while(filas.next())
			{
				transferencias.add(obtenerTransferencia(filas));
			}
		}
		catch(Exception error)
		{
			error.printStackTrace();
		}
 
		return transferencias;
	}
 
	/**
	 * RF-046/RF-049: transferencias PENDIENTES donde el club destino es el del
	 * secretario logueado, es decir, las que ese secretario tiene que aceptar o rechazar.
	 */
	public List<Tranferencias> listarTransferenciasPendientesPorClubDestino(int idClubDestino)
	{
		List<Tranferencias> transferencias = new ArrayList<>();
		String consulta = SELECT_BASE + " WHERE t.estado = 'Pendiente' AND t.club_destino_id = ?";
 
		try
		{
			Connection BD = conexionBD.ConexionBD();
			PreparedStatement peticionSQL = BD.prepareStatement(consulta);
			peticionSQL.setInt(1, idClubDestino);
			ResultSet filas = peticionSQL.executeQuery();
 
			while(filas.next())
			{
				transferencias.add(obtenerTransferencia(filas));
			}
		}
		catch(Exception error)
		{
			error.printStackTrace();
		}
 
		return transferencias;
	}
 
	public Tranferencias buscarTransferenciaPorId(int idTransferencia)
	{
		String consulta = SELECT_BASE + " WHERE t.id = ?";
		Tranferencias transferencia = null;
 
		try
		{
			Connection BD = conexionBD.ConexionBD();
			PreparedStatement peticionSQL = BD.prepareStatement(consulta);
			peticionSQL.setInt(1, idTransferencia);
			ResultSet filas = peticionSQL.executeQuery();
 
			if(filas.next())
			{
				transferencia = obtenerTransferencia(filas);
			}
		}
		catch(Exception error)
		{
			error.printStackTrace();
		}
 
		return transferencia;
	}
 
	public int insertarTransferencia(Tranferencias transferencia)
	{
		String consulta = "INSERT INTO transferencias (fecha_solicitud, fecha_respuesta, estado, jugador_id, "
				+ "club_origen_id, club_destino_id) VALUES (?, ?, ?, ?, ?, ?)";
		int idGenerado = -1;
 
		try
		{
			Connection BD = conexionBD.ConexionBD();
			PreparedStatement peticionSQL = BD.prepareStatement(consulta, Statement.RETURN_GENERATED_KEYS);
 
			peticionSQL.setDate(1, Date.valueOf(transferencia.getFechaSolicitud()));
 
			// fecha_respuesta admite NULL en la base (todavia no hubo respuesta), por eso se chequea antes de convertir
			if(transferencia.getFechaRespuesta() != null)
			{
				peticionSQL.setDate(2, Date.valueOf(transferencia.getFechaRespuesta()));
			}
			else
			{
				peticionSQL.setNull(2, java.sql.Types.DATE);
			}
 
			peticionSQL.setString(3, transferencia.getEstado());
			peticionSQL.setInt(4, transferencia.getJugador().getIdJugador());
			peticionSQL.setInt(5, transferencia.getClubOrigen().getId());
			peticionSQL.setInt(6, transferencia.getClubDestino().getId());
 
			int filasAfectadas = peticionSQL.executeUpdate();
 
			if(filasAfectadas > 0)
			{
				ResultSet claveGenerada = peticionSQL.getGeneratedKeys();
				if(claveGenerada.next())
				{
					idGenerado = claveGenerada.getInt(1);
					transferencia.setIdTransferencia(idGenerado);
				}
			}
		}
		catch(Exception error)
		{
			error.printStackTrace();
		}
 
		return idGenerado;
	}
 
	public boolean actualizarEstadoTransferencia(int idTransferencia, String nuevoEstado, java.time.LocalDate fechaRespuesta)
	{
		String consulta = "UPDATE transferencias SET estado = ?, fecha_respuesta = ? WHERE id = ?";
		boolean exito = false;
 
		try
		{
			Connection BD = conexionBD.ConexionBD();
			PreparedStatement peticionSQL = BD.prepareStatement(consulta);
 
			peticionSQL.setString(1, nuevoEstado);
			peticionSQL.setDate(2, Date.valueOf(fechaRespuesta));
			peticionSQL.setInt(3, idTransferencia);
 
			int filasAfectadas = peticionSQL.executeUpdate();
			exito = filasAfectadas > 0;
		}
		catch(Exception error)
		{
			error.printStackTrace();
		}
 
		return exito;
	}
 
	public boolean eliminarTransferencia(int idTransferencia)
	{
		String consulta = "DELETE FROM transferencias WHERE id = ?";
		boolean exito = false;
 
		try
		{
			Connection BD = conexionBD.ConexionBD();
			PreparedStatement peticionSQL = BD.prepareStatement(consulta);
			peticionSQL.setInt(1, idTransferencia);
 
			int filasAfectadas = peticionSQL.executeUpdate();
			exito = filasAfectadas > 0;
		}
		catch(Exception error)
		{
			error.printStackTrace();
		}
 
		return exito;
	}
 
	private Tranferencias obtenerTransferencia(ResultSet resultados) throws SQLException
	{
		Tranferencias transferencia = new Tranferencias();
		transferencia.setIdTransferencia(resultados.getInt("id"));
		transferencia.setFechaSolicitud(resultados.getDate("fecha_solicitud").toLocalDate());
		transferencia.setEstado(resultados.getString("estado"));
 
		// fecha_respuesta puede ser NULL (transferencia todavia sin respuesta) -> hay que chequear antes de convertir
		Date fechaRespuestaSQL = resultados.getDate("fecha_respuesta");
		if(fechaRespuestaSQL != null)
		{
			transferencia.setFechaRespuesta(fechaRespuestaSQL.toLocalDate());
		}
 
		// Jugador liviano: solo id, nombre, apellido, dni (sin club/categoria/carnet completos)
		Jugador jugador = new Jugador();
		jugador.setIdJugador(resultados.getInt("jugador_id"));
		jugador.setNombre(resultados.getString("jugador_nombre"));
		jugador.setApellido(resultados.getString("jugador_apellido"));
		jugador.setDni(resultados.getString("jugador_dni"));
		transferencia.setJugador(jugador);
 
		Clubes clubOrigen = new Clubes();
		clubOrigen.setId(resultados.getInt("origen_id"));
		clubOrigen.setNombre(resultados.getString("origen_nombre"));
		clubOrigen.setEstadioNombre(resultados.getString("origen_estadio_nombre"));
		clubOrigen.setDireccionEstadio(resultados.getString("origen_direccion_estadio"));
		clubOrigen.setEscudoRuta(resultados.getString("origen_escudo_ruta"));
		transferencia.setClubOrigen(clubOrigen);
 
		Clubes clubDestino = new Clubes();
		clubDestino.setId(resultados.getInt("destino_id"));
		clubDestino.setNombre(resultados.getString("destino_nombre"));
		clubDestino.setEstadioNombre(resultados.getString("destino_estadio_nombre"));
		clubDestino.setDireccionEstadio(resultados.getString("destino_direccion_estadio"));
		clubDestino.setEscudoRuta(resultados.getString("destino_escudo_ruta"));
		transferencia.setClubDestino(clubDestino);
 
		return transferencia;
	}

}
