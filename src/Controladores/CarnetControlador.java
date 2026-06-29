package Controladores;

import java.time.LocalDate;

import ClasesObjetos.Carnets;
import ConsultasBD.CarnetBD;
import Validaciones.ValidacionCarnet;

public class CarnetControlador {

	private CarnetBD carnetBD = new CarnetBD();
	private ValidacionCarnet validacionCarnet = new ValidacionCarnet();
 
	/**
	 * RF-017 + RF-023: emite un carnet nuevo para un jugador recien dado de alta.
	 * Calcula automaticamente fechaVencimiento y fechaLimiteRenovacion segun RN-015.
	 * Solo se puede emitir si la fecha de hoy esta dentro de un periodo habilitado.
	 */
	public String emitirCarnet(int idJugador, String rutaFotoJugador)
	{
		LocalDate hoy = LocalDate.now();
 
		String errorPeriodo = validacionCarnet.validarEmisionEnPeriodo(hoy);
		if(errorPeriodo != null)
		{
			return errorPeriodo;
		}
 
		Carnets carnet = new Carnets();
		carnet.setFechaEmision(hoy);
		carnet.setFechaVencimiento(validacionCarnet.calcularFechaVencimiento(hoy));
		carnet.setFechaLimiteRenovacion(validacionCarnet.calcularLimiteVentanaRenovacion(hoy));
		carnet.setEstado(true); // true = vigente, recien emitido
		carnet.setRutaFotoJugador(rutaFotoJugador);
 
		int idGenerado = carnetBD.insertarCarnet(carnet, idJugador);
		if(idGenerado == -1)
		{
			return "No se pudo emitir el carnet. Intente nuevamente";
		}
 
		return null;
	}
 
	/**
	 * RF-051: renueva un carnet vencido (o por vencer), siempre dentro de un periodo habilitado.
	 * Recalcula las fechas de vigencia desde cero, a partir de la fecha de renovacion (hoy).
	 */
	public String renovarCarnet(Carnets carnetExistente)
	{
		LocalDate hoy = LocalDate.now();
 
		String errorPeriodo = validacionCarnet.validarEmisionEnPeriodo(hoy);
		if(errorPeriodo != null)
		{
			return errorPeriodo;
		}
 
		carnetExistente.setFechaEmision(hoy);
		carnetExistente.setFechaVencimiento(validacionCarnet.calcularFechaVencimiento(hoy));
		carnetExistente.setFechaLimiteRenovacion(validacionCarnet.calcularLimiteVentanaRenovacion(hoy));
		carnetExistente.setEstado(true);
 
		boolean exito = carnetBD.actualizarCarnet(carnetExistente);
		if(!exito)
		{
			return "No se pudo renovar el carnet. Intente nuevamente";
		}
 
		return null;
	}
 
	/**
	 * RF-027: refresca Carnets.estado en base a la fecha actual, antes de mostrarlo
	 * o de usarlo para convocar a un jugador a un partido.
	 * Devuelve true si el carnet sigue vigente, false si ya venció (este metodo
	 * NO distingue si esta en la ventana de gracia; para eso usar estaEnVentanaDeRenovacion).
	 */
	public boolean actualizarEstadoVigencia(Carnets carnet)
	{
		boolean vencido = validacionCarnet.estaVencido(carnet.getFechaVencimiento());
		carnet.setEstado(!vencido);
 
		return !vencido;
	}
 
	public Carnets buscarCarnetDeJugador(int idJugador)
	{
		Carnets carnet = carnetBD.obtenerCarnetPorJugador(idJugador);
 
		if(carnet != null)
		{
			actualizarEstadoVigencia(carnet);
		}
 
		return carnet;
	}

}
