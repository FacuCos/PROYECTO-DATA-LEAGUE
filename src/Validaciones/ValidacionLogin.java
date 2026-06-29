package Validaciones;

import ClasesObjetos.Usuario;
import ConsultasBD.UsuariosBD;

public class ValidacionLogin {
	
	private UsuariosBD usuarioDAO = new UsuariosBD();
	 
	public Usuario login(String dni, String contraseña)
	{
		if(dni == null || dni.isBlank() || contraseña == null || contraseña.isBlank())
		{
			return null;
		}
 
		return usuarioDAO.encontrarUsuario(dni, contraseña);
	}
}
