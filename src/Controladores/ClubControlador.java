package Controladores;

import java.util.List;
import ClasesObjetos.Clubes;
import ClasesObjetos.Usuario;
import ConsultasBD.ClubesBD;
import Validaciones.ValidacionPermisos;
import Validaciones.ValidacionPermisos.Accion;
import Validaciones.ValidacionPermisos.Entidad;


public class ClubControlador {

	private ClubesBD clubesBD = new ClubesBD();
	private ValidacionPermisos validacionPermisos = new ValidacionPermisos();
 
	public String crearClub(Usuario usuarioLogueado, Clubes club)
	{
		String errorPermiso = validacionPermisos.validarPermiso(
				validacionPermisos.obtenerRol(usuarioLogueado), Entidad.CLUB, Accion.CREAR);
		if(errorPermiso != null)
		{
			return errorPermiso;
		}
 
		int idGenerado = clubesBD.insertarClub(club);
		if(idGenerado == -1)
		{
			return "No se pudo registrar el club. Intente nuevamente";
		}
 
		return null;
	}
 
	/**
	 * RN-013 incluida: si el usuarioLogueado es Secretario, solo puede modificar SU club

	 */
	public String modificarClub(Usuario usuarioLogueado, Clubes club)
	{
		String errorPermiso = validacionPermisos.validarPermisoSobreClubPropio(
				usuarioLogueado, Entidad.CLUB, Accion.MODIFICAR, club.getId());
		if(errorPermiso != null)
		{
			return errorPermiso;
		}
 
		boolean exito = clubesBD.actualizarClub(club);
		if(!exito)
		{
			return "No se pudo actualizar el club. Intente nuevamente";
		}
 
		return null;
	}
 
	public String eliminarClub(Usuario usuarioLogueado, int idClub)
	{
		String errorPermiso = validacionPermisos.validarPermiso(
				validacionPermisos.obtenerRol(usuarioLogueado), Entidad.CLUB, Accion.ELIMINAR);
		if(errorPermiso != null)
		{
			return errorPermiso;
		}
 
		boolean exito = clubesBD.eliminarClub(idClub);
		if(!exito)
		{
			return "No se pudo eliminar el club";
		}
 
		return null;
	}
 
	public Clubes buscarClub(int idClub)
	{
		return clubesBD.buscarClubPorId(idClub);
	}
 
	public List<Clubes> listarClubes()
	{
		return clubesBD.listarClubes();
	}
}
