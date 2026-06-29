package Validaciones;

import java.time.LocalDate;
import java.time.Period;
 
import ClasesObjetos.Categorias;
import ClasesObjetos.Jugador;
import ConsultasBD.JugadorBD;

public class ValidacionJugador {
	private static final int EDAD_MINIMA_GENERAL = 5;   // RF-018
	private static final int EDAD_MAXIMA_GENERAL = 60;  // RF-018
	private static final int CUPO_MINIMO_CATEGORIA = 22; // RF-016
	private static final int CUPO_MAXIMO_CATEGORIA = 28; // RF-016
 
	/**
	 * RF-018: el jugador debe tener entre 5 y 60 anios.
	 * Devuelve null si es valido, o el mensaje de error si no lo es.
	 */
	public String validarEdadGeneral(Jugador jugador)
	{
		int edad = calcularEdad(jugador.getFechaNacimiento());
 
		if(edad < EDAD_MINIMA_GENERAL || edad > EDAD_MAXIMA_GENERAL)
		{
			return "El jugador debe tener entre " + EDAD_MINIMA_GENERAL + " y " + EDAD_MAXIMA_GENERAL + " años. Edad actual: " + edad;
		}
 
		return null;
	}
 
	/**
	 * RN-006 a RN-011: calcula el nombre de division que LE CORRESPONDE a un jugador
	 * segun su edad, de forma exacta (no segun lo cargado en la tabla categorias).
	 * - 5 a 14 años: una categoria por cada edad puntual, formato "Sub-N" (RN-007)
	 * - 15 a 16: "5ta division" (RN-008)
	 * - 17 a 18: "4ta division" (RN-009)
	 * - 19 a 29: "Primera division" (RN-010)
	 * - 30 a 60: "Veteranos" (RN-011)
	 * Devuelve null si la edad esta fuera de 5-60 (RN-006 / RF-018), caso que ya cubre validarEdadGeneral.
	 */
	public String calcularDivisionEsperada(int edad)
	{
		if(edad >= 5 && edad <= 14)
		{
			return "Sub-" + edad;
		}
		else if(edad >= 15 && edad <= 16)
		{
			return "5ta division";
		}
		else if(edad >= 17 && edad <= 18)
		{
			return "4ta division";
		}
		else if(edad >= 19 && edad <= 29)
		{
			return "Primera division";
		}
		else if(edad >= 30 && edad <= 60)
		{
			return "Veteranos";
		}
 
		return null;
	}
 
	/**
	 * RF-020 + RN-006 a RN-011: la categoria asignada debe ser EXACTAMENTE
	 * la que corresponde a la edad del jugador, segun la division esperada
	 */
	public String validarEdadParaCategoria(Jugador jugador, Categorias categoria)
	{
		int edad = calcularEdad(jugador.getFechaNacimiento());
		String divisionEsperada = calcularDivisionEsperada(edad);
 
		if(divisionEsperada == null)
		{
			// edad fuera de 5-60: ya deberia haber sido rechazada por validarEdadGeneral antes
			return "La edad del jugador no corresponde a ninguna categoría válida";
		}
 
		if(!divisionEsperada.equalsIgnoreCase(categoria.getDivision()))
		{
			return "El jugador tiene " + edad + " años y le corresponde la categoría " + divisionEsperada
					+ ", no " + categoria.getDivision();
		}
 
		return null;
	}
 
	/**
	 * RF-016: cada categoria debe tener entre 22 y 28 jugadores.
	 * Se valida ANTES de agregar un jugador nuevo, para no superar el cupo maximo.
	 * cantidadActual = cuantos jugadores tiene HOY esa categoria (sin contar al que se quiere agregar).
	 */
	public String validarCupoCategoria(int cantidadActual)
	{
		if(cantidadActual + 1 > CUPO_MAXIMO_CATEGORIA)
		{
			return "La categoría ya alcanzó el cupo máximo de " + CUPO_MAXIMO_CATEGORIA + " jugadores";
		}
 
		return null;
	}
 
	/**
	 * RF-016 (caso de baja): antes de eliminar/dar de baja un jugador, avisar si la
	 * categoria quedaria por debajo del minimo. No bloquea, solo informa
	 * (la baja puede ser necesaria igual, por ejemplo un jugador lesionado de por vida).
	 */
	public String advertirCupoMinimoAlEliminar(int cantidadActual)
	{
		if(cantidadActual - 1 < CUPO_MINIMO_CATEGORIA)
		{
			return "Atención: al eliminar este jugador la categoría quedará con menos de " + CUPO_MINIMO_CATEGORIA + " jugadores";
		}
 
		return null;
	}
 
	/**
	 * RF-019: un jugador solo puede pertenecer a un club a la vez.
	 * Se valida comparando el club actual del jugador (ya guardado en la base)
	 * contra el club nuevo que se le quiere asignar. Si son distintos y el jugador
	 * ya existe en la base, hay que pasar primero por el proceso de Transferencia (RF-034),
	 * no permitir el cambio directo de club.
	 */
	public String validarClubUnico(int idJugador, int idClubNuevo)
	{
		Jugador jugadorActual = new JugadorBD().buscarJugadorPorId(idJugador);
 
		if(jugadorActual == null)
		{
			// El jugador todavia no existe en la base (alta nueva): no hay club previo que comparar
			return null;
		}
 
		if(jugadorActual.getClub().getId() != idClubNuevo)
		{
			return "El jugador ya pertenece a otro club. Para cambiarlo de club debe generarse una Transferencia";
		}
 
		return null;
	}
 
	/**
	 * RF-027: notificar si el jugador ya jugo un partido o tiene el carnet vencido,
	 * antes de convocarlo a un nuevo partido.
	 */
	public String validarConvocatoria(Jugador jugador)
	{
		if(jugador.isYaJugo())
		{
			return "El jugador ya jugó un partido en este período";
		}
 
		if(jugador.getCarnet() != null && jugador.getCarnet().isEstado() == false)
		{
			return "El jugador tiene el carnet vencido";
		}
 
		return null;
	}
 
	private int calcularEdad(LocalDate fechaNacimiento)
	{
		return Period.between(fechaNacimiento, LocalDate.now()).getYears();
	}
}
