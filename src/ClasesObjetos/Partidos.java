package ClasesObjetos;

import java.time.LocalDate;
import java.util.List;

public class Partidos {
	
	private int idPartido;

	public int getIdPartido() {
		return idPartido;
	}

	public void setIdPartido(int idPartido) {
		this.idPartido = idPartido;
	}
	
	private String fechaLiga;
	
	public String getFechaLiga() {
		return fechaLiga;
	}

	public void setFechaLiga(String fechaLiga) {
		this.fechaLiga = fechaLiga;
	}

	private Clubes clubLocal;
	
	public Clubes getClubLocal() {
		return clubLocal;
	}

	public void setClubLocal(Clubes clubLocal) {
		this.clubLocal = clubLocal;
	}
	
	private Clubes clubVisitante;

	public Clubes getClubVisitante() {
		return clubVisitante;
	}

	public void setClubVisitante(Clubes clubVisitante) {
		this.clubVisitante = clubVisitante;
	}
	
	private Categorias categoriaLocal;

	public Categorias getCategoriaLocal() {
		return categoriaLocal;
	}

	public void setCategoriaLocal(Categorias categoriaLocal) {
		this.categoriaLocal = categoriaLocal;
	}
	
	private Categorias categoriaVisitante;

	public Categorias getCategoriaVisitante() {
		return categoriaVisitante;
	}

	public void setCategoriaVisitante(Categorias categoriaVisitante) {
		this.categoriaVisitante = categoriaVisitante;
	}
	
	private LocalDate fechaPartido;

	public LocalDate getFechaPartido() {
		return fechaPartido;
	}

	public void setFechaPartido(LocalDate fechaPartido) {
		this.fechaPartido = fechaPartido;
	}

	private List<Jugador> jugadoresLocal;

	public List<Jugador> getJugadoresLocal() {
		return jugadoresLocal;
	}

	public void setJugadoresLocal(List<Jugador> jugadoresLocal) {
		this.jugadoresLocal = jugadoresLocal;
	}
	
	private List <Jugador> jugadorVisitante;

	public List<Jugador> getJugadorVisitante() {
		return jugadorVisitante;
	}

	public void setJugadorVisitante(List<Jugador> jugadorVisitante) {
		this.jugadorVisitante = jugadorVisitante;
	}
	
	private int golLocal;

	public int getGolLocal() {
		return golLocal;
	}

	public void setGolLocal(int golLocal) {
		this.golLocal = golLocal;
	}
	
	private int golVisitante;

	public int getGolVisitante() {
		return golVisitante;
	}

	public void setGolVisitante(int golVisitante) {
		this.golVisitante = golVisitante;
	}
	
	public Arbitro getArbitro() {
		return arbitro;
	}

	public void setArbitro(Arbitro arbitro) {
		this.arbitro = arbitro;
	}

	public boolean isModificado() {
		return modificado;
	}

	public void setModificado(boolean modificado) {
		this.modificado = modificado;
	}

	private Arbitro arbitro;
	
	private boolean modificado;
	
}
