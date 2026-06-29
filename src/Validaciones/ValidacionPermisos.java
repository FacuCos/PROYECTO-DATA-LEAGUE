package Validaciones;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
 import ClasesObjetos.Secretario;
import ClasesObjetos.Usuario;

public class ValidacionPermisos {
	

	public enum Rol { ADMINISTRADOR, SECRETARIO, ARBITRO, INVITADO }
 
	public enum Entidad { USUARIO_SC, CLUB, CATEGORIA, JUGADOR, USUARIO_ARBITRO, PARTIDO, TRANSFERENCIA }
 
	public enum Accion { CREAR, LISTAR, MODIFICAR, CONSULTAR, ELIMINAR }
 

	private static final Map<Entidad, Map<Accion, EnumSet<Rol>>> PERMISOS = construirMatrizDePermisos();
 
	private static Map<Entidad, Map<Accion, EnumSet<Rol>>> construirMatrizDePermisos()
	{
		Map<Entidad, Map<Accion, EnumSet<Rol>>> matriz = new EnumMap<>(Entidad.class);
 
		// Fila "SC" (alta/gestion de usuarios Secretario): todo es exclusivo del Administrador
		Map<Accion, EnumSet<Rol>> sc = new EnumMap<>(Accion.class);
		sc.put(Accion.CREAR, EnumSet.of(Rol.ADMINISTRADOR));
		sc.put(Accion.LISTAR, EnumSet.of(Rol.ADMINISTRADOR));
		sc.put(Accion.MODIFICAR, EnumSet.of(Rol.ADMINISTRADOR));
		sc.put(Accion.CONSULTAR, EnumSet.of(Rol.ADMINISTRADOR));
		sc.put(Accion.ELIMINAR, EnumSet.of(Rol.ADMINISTRADOR));
		matriz.put(Entidad.USUARIO_SC, sc);
 
		// Fila "Club"
		Map<Accion, EnumSet<Rol>> club = new EnumMap<>(Accion.class);
		club.put(Accion.CREAR, EnumSet.of(Rol.ADMINISTRADOR));
		club.put(Accion.LISTAR, EnumSet.allOf(Rol.class)); // "*" = cualquier rol, incluido Invitado
		club.put(Accion.MODIFICAR, EnumSet.of(Rol.ADMINISTRADOR, Rol.SECRETARIO));
		club.put(Accion.CONSULTAR, EnumSet.allOf(Rol.class));
		club.put(Accion.ELIMINAR, EnumSet.of(Rol.ADMINISTRADOR));
		matriz.put(Entidad.CLUB, club);
 
		// Fila "Categoria"
		Map<Accion, EnumSet<Rol>> categoria = new EnumMap<>(Accion.class);
		categoria.put(Accion.CREAR, EnumSet.of(Rol.ADMINISTRADOR));
		categoria.put(Accion.LISTAR, EnumSet.allOf(Rol.class));
		categoria.put(Accion.MODIFICAR, EnumSet.of(Rol.ADMINISTRADOR, Rol.SECRETARIO));
		categoria.put(Accion.CONSULTAR, EnumSet.allOf(Rol.class));
		categoria.put(Accion.ELIMINAR, EnumSet.of(Rol.ADMINISTRADOR));
		matriz.put(Entidad.CATEGORIA, categoria);
 
		// Fila "Jugador"
		Map<Accion, EnumSet<Rol>> jugador = new EnumMap<>(Accion.class);
		jugador.put(Accion.CREAR, EnumSet.of(Rol.ADMINISTRADOR, Rol.SECRETARIO));
		jugador.put(Accion.LISTAR, EnumSet.allOf(Rol.class));
		jugador.put(Accion.MODIFICAR, EnumSet.of(Rol.ADMINISTRADOR, Rol.SECRETARIO));
		jugador.put(Accion.CONSULTAR, EnumSet.allOf(Rol.class));
		jugador.put(Accion.ELIMINAR, EnumSet.of(Rol.ADMINISTRADOR, Rol.SECRETARIO));
		matriz.put(Entidad.JUGADOR, jugador);
 
		// Fila "A" (alta/gestion de usuarios Arbitro): todo es exclusivo del Administrador
		Map<Accion, EnumSet<Rol>> arbitro = new EnumMap<>(Accion.class);
		arbitro.put(Accion.CREAR, EnumSet.of(Rol.ADMINISTRADOR));
		arbitro.put(Accion.LISTAR, EnumSet.of(Rol.ADMINISTRADOR, Rol.SECRETARIO));
		arbitro.put(Accion.MODIFICAR, EnumSet.of(Rol.ADMINISTRADOR));
		arbitro.put(Accion.CONSULTAR, EnumSet.of(Rol.ADMINISTRADOR));
		arbitro.put(Accion.ELIMINAR, EnumSet.of(Rol.ADMINISTRADOR));
		matriz.put(Entidad.USUARIO_ARBITRO, arbitro);
 
		// Fila "Partido"
		Map<Accion, EnumSet<Rol>> partido = new EnumMap<>(Accion.class);
		partido.put(Accion.CREAR, EnumSet.of(Rol.ADMINISTRADOR, Rol.ARBITRO));
		partido.put(Accion.LISTAR, EnumSet.allOf(Rol.class));
		partido.put(Accion.MODIFICAR, EnumSet.of(Rol.ARBITRO));
		partido.put(Accion.CONSULTAR, EnumSet.allOf(Rol.class));
		partido.put(Accion.ELIMINAR, EnumSet.noneOf(Rol.class)); // "-" = nadie
		matriz.put(Entidad.PARTIDO, partido);
 
		// Fila "Transferencia"
		Map<Accion, EnumSet<Rol>> transferencia = new EnumMap<>(Accion.class);
		transferencia.put(Accion.CREAR, EnumSet.of(Rol.SECRETARIO));
		transferencia.put(Accion.LISTAR, EnumSet.of(Rol.SECRETARIO));
		transferencia.put(Accion.MODIFICAR, EnumSet.noneOf(Rol.class)); // "-"
		transferencia.put(Accion.CONSULTAR, EnumSet.of(Rol.SECRETARIO));
		transferencia.put(Accion.ELIMINAR, EnumSet.noneOf(Rol.class)); // "-"
		matriz.put(Entidad.TRANSFERENCIA, transferencia);
 
		return matriz;
	}
 
	/**
	 * Valida si el rol del usuario logueado puede realizar esta accion sobre esta entidad,
	 * segun la matriz de permisos de la catedra. Devuelve null si esta permitido,
	 * o el mensaje de error si no.
	 */
	public String validarPermiso(Rol rolUsuario, Entidad entidad, Accion accion)
	{
		EnumSet<Rol> rolesPermitidos = PERMISOS.get(entidad).get(accion);
 
		if(!rolesPermitidos.contains(rolUsuario))
		{
			return "No tiene permisos para realizar esta acción";
		}
 
		return null;
	}
 
	/**
	 * Combina la validacion de permiso por rol (validarPermiso) con la restriccion
	 * de club propio (RN-013), para los casos en que el rol es SECRETARIO y la entidad
	 * pertenece a un club especifico (Club, Categoria, Jugador).
	 * idClubDelRegistro = club al que pertenece el registro puntual que se quiere modificar.
	 */
	public String validarPermisoSobreClubPropio(Usuario usuario, Entidad entidad, Accion accion, int idClubDelRegistro)
	{
		Rol rol = obtenerRol(usuario);
 
		String errorPermiso = validarPermiso(rol, entidad, accion);
		if(errorPermiso != null)
		{
			return errorPermiso;
		}
 
		if(rol == Rol.SECRETARIO)
		{
			Secretario secretario = (Secretario) usuario;
			String errorClub = new ValidacionUsuario().validarClubPropio(secretario, idClubDelRegistro);
			if(errorClub != null)
			{
				return errorClub;
			}
		}
 
		return null;
	}
 
	/**
	 * Traduce el campo "rol" (String, tal como viene de Usuario.getRol() / la columna ENUM de la base)
	 * al enum Rol de esta clase.
	 */
	public Rol obtenerRol(Usuario usuario)
	{
		switch(usuario.getRol())
		{
			case "Administrador": return Rol.ADMINISTRADOR;
			case "Secretario": return Rol.SECRETARIO;
			case "Arbitro": return Rol.ARBITRO;
			default: return Rol.INVITADO;
		}
	}

}
