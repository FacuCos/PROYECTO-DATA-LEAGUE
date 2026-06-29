package ConsultasBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import ClasesObjetos.Administrador;
import ClasesObjetos.Arbitro;
import ClasesObjetos.Secretario;
import ClasesObjetos.Usuario;
import Utilidades.conexionBD;

public class UsuariosBD {

	public Usuario encontrarUsuario(String dni, String contraseña)
	{
			String consulta = "SELECT u.*, c.id AS id_club, c.nombre AS nombre_club, c.escudo_Ruta " +
			          "FROM usuarios u " +
			          "LEFT JOIN secretarios s ON u.id = s.usuario_id " +
			          "LEFT JOIN clubes c ON s.club_id = c.id " +
			          "WHERE u.dni = ? AND u.contraseña = ?";
			Usuario usuario = null;
			
			try
			{
				Connection BD = conexionBD.ConexionBD();
				PreparedStatement peticionSQL = BD.prepareStatement(consulta);
				
				peticionSQL.setString(1, dni);
				peticionSQL.setString (2, contraseña);
				ResultSet filas = peticionSQL.executeQuery();
				
				if(filas.next())
					{	
						usuario = obtenerUsuario(filas);
					}

			}
				catch(Exception error)
				{
					error.printStackTrace();
				}
			
			return usuario;
	}
	
	public Usuario obtenerUsuario(ResultSet resultados) throws SQLException 
	{
		String rol = resultados.getString ("rol");
		Usuario usuario = null;
		
		switch (rol)
		{
			case "Arbitro":
			{
				usuario = new Arbitro();
				break;
			}
			case "Secretario":
			{
				usuario = new Secretario ();
				((Secretario) usuario).setNombreClub(resultados.getString("nombre_club"));
				((Secretario) usuario).setEscudoRuta(resultados.getString("escudo_Ruta"));
				((Secretario) usuario).setIdClub(resultados.getInt("id_club"));
				break;
			}
			
			case "Administrador":
			{
				usuario = new Administrador ();
				break;
			}
			default:
			{
				//MOSTRAR NO ENCONTRADO
				return null;
			}
		
		}
		
		usuario.setId(resultados.getInt("id"));
		usuario.setNombre(resultados.getString("nombre"));
		usuario.setApellido(resultados.getString("apellido"));
		usuario.setDni(resultados.getString("dni"));
		usuario.setTelefono(resultados.getString("telefono"));
		usuario.setGmail(resultados.getString("gmail"));
	
		return usuario;
	}
		
	public int contarSecretariosPorClub(int idClub)
	{
	    String consulta = "SELECT COUNT(*) AS total FROM secretarios WHERE club_id = ?";
	    int total = 0;

	    try
	    {
	        Connection BD = conexionBD.ConexionBD();
	        PreparedStatement peticionSQL = BD.prepareStatement(consulta);
	        peticionSQL.setInt(1, idClub);
	        ResultSet filas = peticionSQL.executeQuery();

	        if(filas.next())
	        {
	            total = filas.getInt("total");
	        }
	    }
	    catch(Exception error)
	    {
	        error.printStackTrace();
	    }

	    return total;
	}

	public boolean insertarSecretario(Secretario secretario)
	{
	    String consultaUsuario = "INSERT INTO usuarios (nombre, apellido, dni, telefono, gmail, contraseña, rol) "
	            + "VALUES (?, ?, ?, ?, ?, ?, 'Secretario')";
	    boolean exito = false;

	    try
	    {
	        Connection BD = conexionBD.ConexionBD();
	        PreparedStatement peticionUsuario = BD.prepareStatement(consultaUsuario, Statement.RETURN_GENERATED_KEYS);

	        peticionUsuario.setString(1, secretario.getNombre());
	        peticionUsuario.setString(2, secretario.getApellido());
	        peticionUsuario.setString(3, secretario.getDni());
	        peticionUsuario.setString(4, secretario.getTelefono());
	        peticionUsuario.setString(5, secretario.getGmail());
	        peticionUsuario.setString(6, secretario.getContraseña());

	        int filasAfectadas = peticionUsuario.executeUpdate();

	        if(filasAfectadas > 0)
	        {
	            ResultSet claveGenerada = peticionUsuario.getGeneratedKeys();
	            if(claveGenerada.next())
	            {
	                int idUsuarioGenerado = claveGenerada.getInt(1);
	                secretario.setId(idUsuarioGenerado);

	                String consultaSecretario = "INSERT INTO secretarios (usuario_id, club_id) VALUES (?, ?)";
	                PreparedStatement peticionSecretario = BD.prepareStatement(consultaSecretario);
	                peticionSecretario.setInt(1, idUsuarioGenerado);
	                peticionSecretario.setInt(2, secretario.getIdClub());
	                exito = peticionSecretario.executeUpdate() > 0;
	            }
	        }
	    }
	    catch(Exception error)
	    {
	        error.printStackTrace();
	    }

	    return exito;
	}

	public boolean insertarArbitro(Arbitro arbitro)
	{
	    String consulta = "INSERT INTO usuarios (nombre, apellido, dni, telefono, gmail, contraseña, rol) "
	            + "VALUES (?, ?, ?, ?, ?, ?, 'Arbitro')";
	    boolean exito = false;

	    try
	    {
	        Connection BD = conexionBD.ConexionBD();
	        PreparedStatement peticionSQL = BD.prepareStatement(consulta, Statement.RETURN_GENERATED_KEYS);

	        peticionSQL.setString(1, arbitro.getNombre());
	        peticionSQL.setString(2, arbitro.getApellido());
	        peticionSQL.setString(3, arbitro.getDni());
	        peticionSQL.setString(4, arbitro.getTelefono());
	        peticionSQL.setString(5, arbitro.getGmail());
	        peticionSQL.setString(6, arbitro.getContraseña());

	        int filasAfectadas = peticionSQL.executeUpdate();

	        if(filasAfectadas > 0)
	        {
	            ResultSet claveGenerada = peticionSQL.getGeneratedKeys();
	            if(claveGenerada.next())
	            {
	                arbitro.setId(claveGenerada.getInt(1));
	                exito = true;
	            }
	        }
	    }
	    catch(Exception error)
	    {
	        error.printStackTrace();
	    }

	    return exito;
	}

	public boolean eliminarUsuario(int idUsuario)
	{
	    String consultaSecretario = "DELETE FROM secretarios WHERE usuario_id = ?";
	    String consultaUsuario = "DELETE FROM usuarios WHERE id = ?";
	    boolean exito = false;
	 
	    try
	    {
	        Connection BD = conexionBD.ConexionBD();
	 
	        PreparedStatement peticionSecretario = BD.prepareStatement(consultaSecretario);
	        peticionSecretario.setInt(1, idUsuario);
	        peticionSecretario.executeUpdate();
	 
	        PreparedStatement peticionUsuario = BD.prepareStatement(consultaUsuario);
	        peticionUsuario.setInt(1, idUsuario);
	 
	        int filasAfectadas = peticionUsuario.executeUpdate();
	        exito = filasAfectadas > 0;
	    }
	    catch(Exception error)
	    {
	        error.printStackTrace();
	    }
	 
	    return exito;
	}
	
	public List<Usuario> listarUsuarios()
	{
	    List<Usuario> usuarios = new ArrayList<>();
	 
	    String consulta = "SELECT u.*, c.id AS id_club, c.nombre AS nombre_club, c.escudo_Ruta " +
	              "FROM usuarios u " +
	              "LEFT JOIN secretarios s ON u.id = s.usuario_id " +
	              "LEFT JOIN clubes c ON s.club_id = c.id " +
	              "ORDER BY u.apellido, u.nombre";
	 
	    try
	    {
	        Connection BD = conexionBD.ConexionBD();
	        PreparedStatement peticionSQL = BD.prepareStatement(consulta);
	        ResultSet filas = peticionSQL.executeQuery();
	 
	        while(filas.next())
	        {
	            Usuario usuario = obtenerUsuario(filas);
	            if(usuario != null)
	            {
	                usuarios.add(usuario);
	            }
	        }
	    }
	    catch(Exception error)
	    {
	        error.printStackTrace();
	    }
	 
	    return usuarios;
	}

}


