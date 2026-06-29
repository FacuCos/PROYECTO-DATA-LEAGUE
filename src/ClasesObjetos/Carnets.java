package ClasesObjetos;

import java.time.LocalDate;

public class Carnets {

	private int idCarnet;

	public int getIdCarnet() {
		return idCarnet;
	}

	public void setIdCarnet(int idCarnet) {
		this.idCarnet = idCarnet;
	}
	
	private LocalDate fechaEmision;

	public LocalDate getFechaEmision() {
		return fechaEmision;
	}

	public void setFechaEmision(LocalDate fechaEmision) {
		this.fechaEmision = fechaEmision;
	}
	
	private LocalDate fechaVencimiento;

	public LocalDate getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(LocalDate fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}
	
	private boolean estado;

	public boolean isEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	
	private Jugador jugador;

	public Jugador getJugador() {
		return jugador;
	}

	public void setJugador(Jugador jugador) {
		this.jugador = jugador;
	}
	
	private String rutaFotoJugador;

	public String getRutaFotoJugador() {
		return rutaFotoJugador;
	}

	public void setRutaFotoJugador(String rutaFotoJugador) {
		this.rutaFotoJugador = rutaFotoJugador;
	}
	
	public LocalDate getFechaLimiteRenovacion() {
		return FechaLimiteRenovacion;
	}

	public void setFechaLimiteRenovacion(LocalDate fechaLimiteRenovacion) {
		FechaLimiteRenovacion = fechaLimiteRenovacion;
	}

	private LocalDate FechaLimiteRenovacion;
}
