package Validaciones;

import ClasesObjetos.Secretario;

public class ValidacionUsuario {


	private static final int MAXIMO_SECRETARIOS_POR_CLUB = 3; // RF-035
 
	/**
	 * RF-035: el sistema debe validar el registro de 3 SC por club unicamente.
	 * cantidadActual = cuantos secretarios tiene HOY ese club (via UsuariosBD.contarSecretariosPorClub),
	 * sin contar al que se quiere registrar.
	 */
	public String validarLimiteSecretarios(int cantidadActual)
	{
		if(cantidadActual + 1 > MAXIMO_SECRETARIOS_POR_CLUB)
		{
			return "El club ya tiene el máximo de " + MAXIMO_SECRETARIOS_POR_CLUB + " secretarios permitidos";
		}
 
		return null;
	}
 
	/**
	 * RN-013: un secretario solo puede modificar datos (club, categorias, jugadores, etc.)
	 * que pertenezcan a SU PROPIO club. Se llama antes de cualquier insert/update/delete
	 * que un Secretario intente hacer sobre un registro de un club determinado.
	 */
	public String validarClubPropio(Secretario secretarioLogueado, int idClubDelRegistro)
	{
		if(secretarioLogueado.getIdClub() != idClubDelRegistro)
		{
			return "No tiene permisos para modificar datos de otro club";
		}
 
		return null;
	}
}
