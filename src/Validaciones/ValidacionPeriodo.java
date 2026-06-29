package Validaciones;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;

public class ValidacionPeriodo {
	
	public boolean estaEnPeriodoInvierno(LocalDate fecha)
	{
		LocalDate inicio = inicioInvierno(fecha.getYear());
		LocalDate fin = finInvierno(fecha.getYear());
 
		return !fecha.isBefore(inicio) && !fecha.isAfter(fin);
	}
 
	/**
	 * RN-002/RN-003: el periodo de verano va desde el 2do lunes de diciembre
	 * hasta el ultimo viernes de febrero, CRUZANDO el anio nuevo.
	 * Por eso hay que evaluar dos casos: que "fecha" este en diciembre (mismo anio que el inicio)
	 * o que este en enero/febrero (anio siguiente, antes del fin de febrero).
	 */
	public boolean estaEnPeriodoVerano(LocalDate fecha)
	{
		int anio = fecha.getYear();
 
		// Caso A: fecha cae en diciembre -> el periodo de verano que arranca ESE diciembre
		LocalDate inicioEsteAnio = inicioVerano(anio);
		LocalDate finDeAnio = LocalDate.of(anio, Month.DECEMBER, 31);
 
		if(!fecha.isBefore(inicioEsteAnio) && !fecha.isAfter(finDeAnio))
		{
			return true;
		}
 
		// Caso B: fecha cae en enero o febrero -> el periodo de verano que arranco el diciembre ANTERIOR
		LocalDate finEsteAnio = finVerano(anio);
 
		if(fecha.getMonth() != Month.DECEMBER && !fecha.isAfter(finEsteAnio))
		{
			return !fecha.isBefore(LocalDate.of(anio, Month.JANUARY, 1));
		}
 
		return false;
	}
 
	/**
	 * RN-015 (interpretacion ampliada): dada una fecha de emision, devuelve el
	 * INICIO del periodo SIGUIENTE al que esa fecha pertenece. Ese momento es
	 * cuando el carnet pasa a estar vencido (a partir de ahi arranca la ventana
	 * de renovacion sin sancion, que dura hasta el fin de ese periodo siguiente).
	 * Devuelve null si la fecha no esta dentro de ningun periodo habilitado.
	 */
	public LocalDate inicioProximoPeriodo(LocalDate fecha)
	{
		if(estaEnPeriodoInvierno(fecha))
		{
			// el periodo siguiente al invierno es el verano que arranca en diciembre del MISMO anio
			return inicioVerano(fecha.getYear());
		}
 
		if(estaEnPeriodoVerano(fecha))
		{
			// si la fecha cae en diciembre, el periodo siguiente (invierno) es del anio SIGUIENTE
			// si la fecha cae en enero/febrero, el periodo siguiente (invierno) es del MISMO anio
			if(fecha.getMonth() == Month.DECEMBER)
			{
				return inicioInvierno(fecha.getYear() + 1);
			}
			return inicioInvierno(fecha.getYear());
		}
 
		return null;
	}
 
	/**
	 * RN-015 (interpretacion ampliada): fin de la ventana de renovacion sin sancion,
	 * es decir, el ULTIMO DIA del periodo siguiente al que se emitio el carnet.
	 * Pasada esta fecha, el carnet queda vencido sin posibilidad de renovacion de gracia.
	 */
	public LocalDate finVentanaRenovacion(LocalDate fechaEmision)
	{
		if(estaEnPeriodoInvierno(fechaEmision))
		{
			// el periodo siguiente es el verano que arranca en diciembre del mismo anio,
			// y termina en febrero del anio siguiente
			return finVerano(fechaEmision.getYear() + 1);
		}
 
		if(estaEnPeriodoVerano(fechaEmision))
		{
			if(fechaEmision.getMonth() == Month.DECEMBER)
			{
				return finInvierno(fechaEmision.getYear() + 1);
			}
			return finInvierno(fechaEmision.getYear());
		}
 
		return null;
	}
 
	private LocalDate inicioInvierno(int anio)
	{
		return LocalDate.of(anio, Month.JUNE, 1)
				.with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY))
				.plusWeeks(1); // primer lunes + 1 semana = segundo lunes
	}
 
	private LocalDate finInvierno(int anio)
	{
		return LocalDate.of(anio, Month.AUGUST, 1)
				.with(TemporalAdjusters.lastInMonth(DayOfWeek.FRIDAY));
	}
 
	private LocalDate inicioVerano(int anio)
	{
		return LocalDate.of(anio, Month.DECEMBER, 1)
				.with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY))
				.plusWeeks(1);
	}
 
	private LocalDate finVerano(int anio)
	{
		return LocalDate.of(anio, Month.FEBRUARY, 1)
				.with(TemporalAdjusters.lastInMonth(DayOfWeek.FRIDAY));
	}

}
