package Controladores;

import ClasesObjetos.Categorias;
import ClasesObjetos.Jugador;
import ConsultasBD.CategoriaBD;
import ConsultasBD.JugadorBD;
import Validaciones.ValidacionJugador;
public class JugadorControlador {
	

	private JugadorBD jugadorBD = new JugadorBD();
	private CategoriaBD categoriaBD = new CategoriaBD();
	private ValidacionJugador validacionJugador = new ValidacionJugador();
	private CarnetControlador carnetControlador = new CarnetControlador();
 
	/**
	 * Da de alta un jugador nuevo, aplicando las validaciones de negocio
	 * antes de persistir (RF-016, RF-018, RF-020), y EMITIENDO SU CARNET
	 * automaticamente (RF-017): sin esto, JugadorBD excluye al jugador de
	 * cualquier listado porque usa JOIN (no LEFT JOIN) contra carnets.
	 */
	public String registrarJugador(Jugador jugador, Categorias categoria)
	{
		String errorEdad = validacionJugador.validarEdadGeneral(jugador);
		if(errorEdad != null)
		{
			return errorEdad;
		}
 
		String errorCategoria = validacionJugador.validarEdadParaCategoria(jugador, categoria);
		if(errorCategoria != null)
		{
			return errorCategoria;
		}
 
		int cantidadActual = categoriaBD.contarJugadoresEnCategoria(categoria.getId());
		String errorCupo = validacionJugador.validarCupoCategoria(cantidadActual);
		if(errorCupo != null)
		{
			return errorCupo;
		}
 
		int idGenerado = jugadorBD.insertarJugador(jugador);
		if(idGenerado == -1)
		{
			return "No se pudo registrar el jugador. Intente nuevamente";
		}
 
		// RF-017 + RF-023: emitir el carnet del jugador recien creado. Si la emision
		// falla (por ejemplo, hoy no es un periodo habilitado), el jugador YA QUEDO
		// guardado en la base pero sin carnet, lo cual lo deja invisible en los listados
		// hasta que se le cree uno manualmente. Se informa este caso especial en el mensaje.
		String errorCarnet = carnetControlador.emitirCarnet(idGenerado, null);
		if(errorCarnet != null)
		{
			return "El jugador se registró, pero no se pudo emitir su carnet: " + errorCarnet
					+ ". El jugador no aparecerá en los listados hasta que se le emita un carnet.";
		}
 
		return null;
	}
 
	/**
	 * Modifica un jugador existente, validando que no quede en una categoria
	 * que no corresponde a su edad.
	 */
	public String modificarJugador(Jugador jugador, Categorias categoria)
	{
		String errorCategoria = validacionJugador.validarEdadParaCategoria(jugador, categoria);
		if(errorCategoria != null)
		{
			return errorCategoria;
		}
 
		boolean exito = jugadorBD.actualizarJugador(jugador);
		if(!exito)
		{
			return "No se pudo actualizar el jugador. Intente nuevamente";
		}
 
		return null;
	}
 
	/**
	 * Da de baja un jugador (RF-025). Solo advierte si la categoria queda
	 * por debajo del cupo minimo, no bloquea la baja.
	 */
	public String eliminarJugador(int idJugador, int idCategoria)
	{
		int cantidadActual = categoriaBD.contarJugadoresEnCategoria(idCategoria);
		String advertencia = validacionJugador.advertirCupoMinimoAlEliminar(cantidadActual);
 
		boolean exito = jugadorBD.eliminarJugador(idJugador);
		if(!exito)
		{
			return "No se pudo eliminar el jugador";
		}
 
		// se devuelve la advertencia (puede ser null) para que la Vista decida si mostrarla,
		// aunque la baja ya se realizo con exito
		return advertencia;
	}
 
	/**
	 * Valida si un jugador puede ser convocado a un partido (RF-027).
	 */
	public String validarConvocatoria(Jugador jugador)
	{
		return validacionJugador.validarConvocatoria(jugador);
	}
 
	/**
	 * RF-020 + RN-006 a RN-011: busca y devuelve la categoria que le corresponde
	 * a un jugador segun su edad, dentro de las categorias YA EXISTENTES de un club.
	 * Devuelve null si no se encontro ninguna categoria de ese club con la division correspondiente
	 * (en ese caso, antes de poder registrar al jugador, el secretario debe crear esa categoria).
	 */
	public Categorias buscarCategoriaPorEdad(Jugador jugador, int idClub)
	{
		int edad = java.time.Period.between(jugador.getFechaNacimiento(), java.time.LocalDate.now()).getYears();
		String divisionEsperada = validacionJugador.calcularDivisionEsperada(edad);
 
		if(divisionEsperada == null)
		{
			return null;
		}
 
		java.util.List<Categorias> categoriasDelClub = categoriaBD.listarCategoriasPorClub(idClub);
 
		for(Categorias categoria : categoriasDelClub)
		{
			if(divisionEsperada.equalsIgnoreCase(categoria.getDivision()))
			{
				return categoria;
			}
		}
 
		return null;
	}
 
	public Jugador buscarJugador(int idJugador)
	{
		return jugadorBD.buscarJugadorPorId(idJugador);
	}
 
	public java.util.List<Jugador> listarJugadores()
	{
		return jugadorBD.listarJugadores();
	}
 
	/**
	 * Filtra el listado completo por club. JugadorBD no tiene un metodo de consulta
	 * directa "por club" todavia, asi que se filtra en memoria sobre listarJugadores().
	 * Si el listado general crece mucho, conviene agregar un SELECT con WHERE club_id = ?
	 * directo en JugadorBD en vez de filtrar aca.
	 */
	public java.util.List<Jugador> listarJugadoresPorClub(int idClub)
	{
		java.util.List<Jugador> todos = jugadorBD.listarJugadores();
		java.util.List<Jugador> filtrados = new java.util.ArrayList<>();
 
		for(Jugador jugador : todos)
		{
			if(jugador.getClub() != null && jugador.getClub().getId() == idClub)
			{
				filtrados.add(jugador);
			}
		}
 
		return filtrados;
	}
}
