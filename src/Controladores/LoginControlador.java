package Controladores;

import ClasesObjetos.Usuario;
import Validaciones.ValidacionLogin;

public class LoginControlador {
	
	private ValidacionLogin validacionLogin = new ValidacionLogin();
	 
	/**
	 * Intenta loguear al usuario. Devuelve el Usuario (Administrador/Secretario/Arbitro)
	 * si las credenciales son correctas, o null si fallaron (dni/contraseña vacios o incorrectos).
	 * La Vista es responsable de mostrar el mensaje de error generico ("Usuario o contraseña incorrectos")
	 * cuando este metodo devuelve null, sin distinguir la causa exacta por seguridad.
	 */
	public Usuario iniciarSesion(String dni, String contraseña)
	{
		return validacionLogin.login(dni, contraseña);
	}

}
