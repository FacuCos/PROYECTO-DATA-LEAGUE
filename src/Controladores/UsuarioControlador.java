package Controladores;

import ClasesObjetos.Secretario;
import ClasesObjetos.Usuario;
import ConsultasBD.UsuariosBD;
import Validaciones.ValidacionPermisos;
import Validaciones.ValidacionPermisos.Accion;
import Validaciones.ValidacionPermisos.Entidad;
import Validaciones.ValidacionUsuario;

public class UsuarioControlador {

	private UsuariosBD usuariosBD = new UsuariosBD();
	private ValidacionUsuario validacionUsuario = new ValidacionUsuario();
	private ValidacionPermisos validacionPermisos = new ValidacionPermisos();
 
	/**
	 * RF-036 + RF-035: el ADM crea un usuario Secretario, validando que el club
	 * no supere el limite de 3 secretarios.
	 */
	public String crearSecretario(Usuario usuarioLogueado, Secretario nuevoSecretario)
	{
		String errorPermiso = validacionPermisos.validarPermiso(
				validacionPermisos.obtenerRol(usuarioLogueado), Entidad.USUARIO_SC, Accion.CREAR);
		if(errorPermiso != null)
		{
			return errorPermiso;
		}
 
		int cantidadActual = usuariosBD.contarSecretariosPorClub(nuevoSecretario.getIdClub());
		String errorLimite = validacionUsuario.validarLimiteSecretarios(cantidadActual);
		if(errorLimite != null)
		{
			return errorLimite;
		}
 

		boolean exito = usuariosBD.insertarSecretario(nuevoSecretario);
		if(!exito)
		{
			return "No se pudo registrar el secretario. Intente nuevamente";
		}
 
		return null;
	}
 
	/**
	 * RF-037: el ADM crea un usuario Arbitro
	 */
	public String crearArbitro(Usuario usuarioLogueado, ClasesObjetos.Arbitro nuevoArbitro)
	{
		String errorPermiso = validacionPermisos.validarPermiso(
				validacionPermisos.obtenerRol(usuarioLogueado), Entidad.USUARIO_ARBITRO, Accion.CREAR);
		if(errorPermiso != null)
		{
			return errorPermiso;
		}
 
		boolean exito = usuariosBD.insertarArbitro(nuevoArbitro);
		if(!exito)
		{
			return "No se pudo registrar el árbitro. Intente nuevamente";
		}
 
		return null;
	}
 
	/**
	 * RF-038: el ADM da de baja un usuario.
	 */
	public String eliminarUsuario(Usuario usuarioLogueado, int idUsuario)
	{
		String errorPermiso = validacionPermisos.validarPermiso(
				validacionPermisos.obtenerRol(usuarioLogueado), Entidad.USUARIO_SC, Accion.ELIMINAR);
		if(errorPermiso != null)
		{
			return errorPermiso;
		}
 
		boolean exito = usuariosBD.eliminarUsuario(idUsuario);
		if(!exito)
		{
			return "No se pudo eliminar el usuario";
		}
 
		return null;
	}
 
	public java.util.List<Usuario> listarUsuarios()
	{
		return usuariosBD.listarUsuarios();
	}

}
