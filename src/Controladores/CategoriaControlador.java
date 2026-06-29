package Controladores;

import java.util.List;

import ClasesObjetos.Categorias;
import ClasesObjetos.Usuario;
import ConsultasBD.CategoriaBD;
import Validaciones.ValidacionPermisos;
import Validaciones.ValidacionPermisos.Accion;
import Validaciones.ValidacionPermisos.Entidad;

public class CategoriaControlador {
	
	private CategoriaBD categoriaBD = new CategoriaBD();
	private ValidacionPermisos validacionPermisos = new ValidacionPermisos();
 
	public String crearCategoria(Usuario usuarioLogueado, Categorias categoria)
	{
		String errorPermiso = validacionPermisos.validarPermiso(
				validacionPermisos.obtenerRol(usuarioLogueado), Entidad.CATEGORIA, Accion.CREAR);
		if(errorPermiso != null)
		{
			return errorPermiso;
		}
 
		int idGenerado = categoriaBD.insertarCategoria(categoria);
		if(idGenerado == -1)
		{
			return "No se pudo registrar la categoría. Intente nuevamente";
		}
 
		return null;
	}
 
	public String modificarCategoria(Usuario usuarioLogueado, Categorias categoria)
	{
		String errorPermiso = validacionPermisos.validarPermisoSobreClubPropio(
				usuarioLogueado, Entidad.CATEGORIA, Accion.MODIFICAR, categoria.getClub().getId());
		if(errorPermiso != null)
		{
			return errorPermiso;
		}
 
		boolean exito = categoriaBD.actualizarCategoria(categoria);
		if(!exito)
		{
			return "No se pudo actualizar la categoría. Intente nuevamente";
		}
 
		return null;
	}
 
	public String eliminarCategoria(Usuario usuarioLogueado, Categorias categoria)
	{
		String errorPermiso = validacionPermisos.validarPermiso(
				validacionPermisos.obtenerRol(usuarioLogueado), Entidad.CATEGORIA, Accion.ELIMINAR);
		if(errorPermiso != null)
		{
			return errorPermiso;
		}
 
		boolean exito = categoriaBD.eliminarCategoria(categoria.getId());
		if(!exito)
		{
			return "No se pudo eliminar la categoría";
		}
 
		return null;
	}
 
	public Categorias buscarCategoria(int idCategoria)
	{
		return categoriaBD.buscarCategoriaPorId(idCategoria);
	}
 
	public List<Categorias> listarCategorias()
	{
		return categoriaBD.listarCategorias();
	}
 
	public List<Categorias> listarCategoriasPorClub(int idClub)
	{
		return categoriaBD.listarCategoriasPorClub(idClub);
	}

}
