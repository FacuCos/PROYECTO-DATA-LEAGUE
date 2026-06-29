package Validaciones;

import java.time.LocalDate;

public class ValidacionCarnet {
	

	private ValidacionPeriodo validacionPeriodo = new ValidacionPeriodo();
 
	/**
	 * RF-023: la emision o renovacion de un carnet solo puede hacerse
	 * dentro de un periodo habilitado (invierno o verano).
	 */
	public String validarEmisionEnPeriodo(LocalDate fechaEmision)
	{
		if(!validacionPeriodo.estaEnPeriodoInvierno(fechaEmision) && !validacionPeriodo.estaEnPeriodoVerano(fechaEmision))
		{
			return "No se puede emitir o renovar un carnet fuera de los períodos de inscripción habilitados";
		}
 
		return null;
	}
 
	/**
	 * RN-015 (interpretacion ampliada confirmada con el usuario): el carnet se considera
	 * vencido (Carnets.estado = false) recien al INICIO del periodo siguiente al que fue
	 * emitido/renovado. Por ejemplo: emitido en verano -> vence al inicio del invierno que sigue.
	 */
	public LocalDate calcularFechaVencimiento(LocalDate fechaEmision)
	{
		return validacionPeriodo.inicioProximoPeriodo(fechaEmision);
	}
 
	/**
	 * RN-015 (interpretacion ampliada): aunque el carnet ya este vencido (paso fechaVencimiento),
	 * el jugador tiene una "ventana de gracia" para renovar sin sancion hasta el FIN del
	 * periodo siguiente. Pasada esta fecha, ya no hay ventana de gracia.
	 */
	public LocalDate calcularLimiteVentanaRenovacion(LocalDate fechaEmision)
	{
		return validacionPeriodo.finVentanaRenovacion(fechaEmision);
	}
 
	/**
	 * RN-015: indica si, a la fecha de hoy, el carnet ya esta vencido
	 * (paso el inicio del periodo siguiente al de su emision).
	 */
	public boolean estaVencido(LocalDate fechaVencimiento)
	{
		return !LocalDate.now().isBefore(fechaVencimiento);
	}
 
	/**
	 * RN-015: indica si el jugador todavia esta dentro de la ventana de gracia
	 * para renovar sin sancion (vencido, pero no mas alla del limite de renovacion).
	 */
	public boolean estaEnVentanaDeRenovacion(LocalDate fechaVencimiento, LocalDate limiteRenovacion)
	{
		LocalDate hoy = LocalDate.now();
		return !hoy.isBefore(fechaVencimiento) && !hoy.isAfter(limiteRenovacion);
	}

}
