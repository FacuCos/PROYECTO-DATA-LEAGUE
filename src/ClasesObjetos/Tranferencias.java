package ClasesObjetos;

import java.time.LocalDate;

public class Tranferencias {
	
	private int idTransferencia;

	public int getIdTransferencia() {
		return idTransferencia;
	}

	public void setIdTransferencia(int idTransferencia) {
		this.idTransferencia = idTransferencia;
	}
	
	private Clubes clubOrigen;

	public Clubes getClubOrigen() {
		return clubOrigen;
	}

	public void setClubOrigen(Clubes clubOrigen) {
		this.clubOrigen = clubOrigen;
	}

	private Clubes clubDestino;

	public Clubes getClubDestino() {
		return clubDestino;
	}

	public void setClubDestino(Clubes clubDestino) {
		this.clubDestino = clubDestino;
	}
	
	private LocalDate fechaSolicitud;
	
	public LocalDate getFechaSolicitud() {
		return fechaSolicitud;
	}

	public void setFechaSolicitud(LocalDate fechaSolicitud) {
		this.fechaSolicitud = fechaSolicitud;
	}

	private String estado;
	
	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	private LocalDate fechaRespuesta;
	
	public LocalDate getFechaRespuesta() {
		return fechaRespuesta;
	}

	public void setFechaRespuesta(LocalDate fechaRespuesta) {
		this.fechaRespuesta = fechaRespuesta;
	}

	private Jugador jugador;

	public Jugador getJugador() {
		return jugador;
	}

	public void setJugador(Jugador jugador) {
		this.jugador = jugador;
	}
}
