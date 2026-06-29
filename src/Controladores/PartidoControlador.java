package Controladores;

import java.util.List;
import ClasesObjetos.Jugador;
import ClasesObjetos.Partidos;
import ClasesObjetos.Usuario;
import ConsultasBD.PartidoBD;
import Validaciones.ValidacionPartido;
import Validaciones.ValidacionPermisos;
import Validaciones.ValidacionPermisos.Accion;
import Validaciones.ValidacionPermisos.Entidad;

public class PartidoControlador {
	
	private PartidoBD partidoBD = new PartidoBD();
	private ValidacionPartido validacionPartido = new ValidacionPartido();
	private ValidacionPermisos validacionPermisos = new ValidacionPermisos();
 
	private static final int MAXIMO_CONVOCADOS = 16; // RF-039
 
	public String crearPartido(Usuario usuarioLogueado, Partidos partido)
	{
		String errorPermiso = validacionPermisos.validarPermiso(
				validacionPermisos.obtenerRol(usuarioLogueado), Entidad.PARTIDO, Accion.CREAR);
		if(errorPermiso != null)
		{
			return errorPermiso;
		}
 
		int idGenerado = partidoBD.insertarPartido(partido);
		if(idGenerado == -1)
		{
			return "No se pudo registrar el partido. Intente nuevamente";
		}
 
		return null;
	}
 
	/**
	 * RF-032 + RF-040: solo el arbitro que dirigio el partido puede modificarlo,
	 * y solo una vez (validacion contra Partidos.isModificado()).
	 */
	public String modificarResultado(Usuario usuarioLogueado, Partidos partido, int golLocal, int golVisitante)
	{
		String errorPermiso = validacionPermisos.validarPermiso(
				validacionPermisos.obtenerRol(usuarioLogueado), Entidad.PARTIDO, Accion.MODIFICAR);
		if(errorPermiso != null)
		{
			return errorPermiso;
		}
 
		String errorArbitro = validacionPartido.validarArbitroAutorizado(partido, usuarioLogueado.getId());
		if(errorArbitro != null)
		{
			return errorArbitro;
		}
 
		String errorUnicaModificacion = validacionPartido.validarUnicaModificacion(partido);
		if(errorUnicaModificacion != null)
		{
			return errorUnicaModificacion;
		}
 
		boolean exito = partidoBD.actualizarResultado(partido.getIdPartido(), golLocal, golVisitante);
		if(!exito)
		{
			return "No se pudo actualizar el resultado del partido";
		}
 
		return null;
	}
 
	/**
	 * RF-026 + RF-039: convoca a un jugador al equipo local o visitante de un partido,
	 * validando que no se supere el maximo de 16 convocados.
	 */
	public String convocarJugadorLocal(int idPartido, int idJugador)
	{
		return convocarJugador(idPartido, idJugador, true);
	}
 
	public String convocarJugadorVisitante(int idPartido, int idJugador)
	{
		return convocarJugador(idPartido, idJugador, false);
	}
 
	private String convocarJugador(int idPartido, int idJugador, boolean esLocal)
	{
		List<Jugador> convocadosActuales = esLocal
				? partidoBD.obtenerJugadoresLocal(idPartido)
				: partidoBD.obtenerJugadoresVisitante(idPartido);
 
		String errorLimite = validacionPartido.validarLimiteConvocados(convocadosActuales.size());
		if(errorLimite != null)
		{
			return errorLimite;
		}
 
		boolean exito = esLocal
				? partidoBD.agregarJugadorLocal(idPartido, idJugador)
				: partidoBD.agregarJugadorVisitante(idPartido, idJugador);
 
		if(!exito)
		{
			return "No se pudo convocar al jugador";
		}
 
		return null;
	}
 
	public Partidos buscarPartido(int idPartido)
	{
		return partidoBD.buscarPartidoPorId(idPartido);
	}
 
	public List<Partidos> listarPartidos()
	{
		return partidoBD.listarPartidos();
	}
 
	/**
	 * RF-026: partidos donde participa un club especifico (como local o visitante).
	 * Usado por ConvocatoriaVista para que el Secretario solo vea los partidos
	 * de su propio club al armar la convocatoria.
	 */
	public List<Partidos> listarPartidosPorClub(int idClub)
	{
		return partidoBD.listarPartidosPorClub(idClub);
	}
 
	/**
	 * RF-029: filtra los partidos cuya fecha es hoy o futura, ordenados por fecha.
	 * Usado en pantallas publicas (Login, vista de Invitado) que muestran "proximos partidos"
	 * sin necesitar todo el historial. El filtro se hace en memoria sobre listarPartidos(),
	 * ya que PartidoBD no tiene un SELECT con WHERE fecha_partido >= ? todavia.
	 */
	public List<Partidos> listarProximosPartidos()
	{
		java.time.LocalDate hoy = java.time.LocalDate.now();
		List<Partidos> todos = partidoBD.listarPartidos();
		List<Partidos> proximos = new java.util.ArrayList<>();
 
		for(Partidos partido : todos)
		{
			if(partido.getFechaPartido() != null && !partido.getFechaPartido().isBefore(hoy))
			{
				proximos.add(partido);
			}
		}
 
		proximos.sort((p1, p2) -> p1.getFechaPartido().compareTo(p2.getFechaPartido()));
 
		return proximos;
	}
 
	public List<Jugador> obtenerJugadoresLocal(int idPartido)
	{
		return partidoBD.obtenerJugadoresLocal(idPartido);
	}
 
	public List<Jugador> obtenerJugadoresVisitante(int idPartido)
	{
		return partidoBD.obtenerJugadoresVisitante(idPartido);
	}	

}
