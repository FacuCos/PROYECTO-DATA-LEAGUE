package Validaciones;

import ClasesObjetos.Partidos;

public class ValidacionPartido {
	
	private static final int MAXIMO_CONVOCADOS_POR_PARTIDO = 16; // RF-039
	 
	/**
	 * RF-032: solo se permite UNA modificacion del registro de un partido,
	 * una vez que este ya se jugo (es decir, ya tiene resultado/fue modificado antes).
	 * Se llama ANTES de invocar PartidoBD.actualizarResultado().
	 */
	public String validarUnicaModificacion(Partidos partido)
	{
		if(partido.isModificado())
		{
			return "Este partido ya fue modificado una vez. No se permiten más modificaciones";
		}
 
		return null;
	}
 
	/**
	 * RF-040: solo el arbitro que dirigio el partido puede modificarlo.
	 * idUsuarioLogueado = id del usuario que esta intentando hacer la modificacion (de la sesion actual).
	 */
	public String validarArbitroAutorizado(Partidos partido, int idUsuarioLogueado)
	{
		if(partido.getArbitro().getId() != idUsuarioLogueado)
		{
			return "Solo el árbitro que dirigió este partido puede modificarlo";
		}
 
		return null;
	}
 
	/**
	 * RF-039: maximo 16 jugadores convocados por partido, por cada club enfrentado.
	 * cantidadConvocados = jugadores ya convocados a ese equipo en ese partido
	 * (via PartidoBD.obtenerJugadoresLocal/obtenerJugadoresVisitante .size()),
	 * sin contar al que se quiere agregar.
	 */
	public String validarLimiteConvocados(int cantidadConvocados)
	{
		if(cantidadConvocados + 1 > MAXIMO_CONVOCADOS_POR_PARTIDO)
		{
			return "Ya se alcanzó el máximo de " + MAXIMO_CONVOCADOS_POR_PARTIDO + " convocados para este partido";
		}
 
		return null;
	}

}
