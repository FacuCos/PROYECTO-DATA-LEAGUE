package ClasesObjetos;

import java.time.LocalDate;
import java.util.List;

public class Jugador {

	private int idJugador;
	
	public int getIdJugador() {
		return idJugador;
	}

	public void setIdJugador(int idJugador) {
		this.idJugador = idJugador;
	}

	private String nombre;

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	private String apellido;

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	
	private String dni;
	
	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	private String domicilio;

	public String getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}
	
	private String telefono;

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	
	private String gmail;

	public String getGmail() {
		return gmail;
	}

	public void setGmail(String gmail) {
		this.gmail = gmail;
	}
	
	private Clubes club;

	public Clubes getClub() {
		return club;
	}

	public void setClub(Clubes club) {
		this.club = club;
	}
	
	private Categorias categoria;

	public Categorias getCategoria() {
		return categoria;
	}

	public void setCategoria(Categorias categoria) {
		this.categoria = categoria;
	}
	
	private boolean yaJugo;

	public boolean isYaJugo() {
		return yaJugo;
	}

	public void setYaJugo(boolean yaJugo) {
		this.yaJugo = yaJugo;
	}
	
	private List<String> historialPartido;
	
	public List<String> getHistorialPartido() {
		return historialPartido;
	}

	public void setHistorialPartido(List<String> historialPartido) {
		this.historialPartido = historialPartido;
	}

	private List<Clubes> historialClubes;

	public List<Clubes> getHistorialClubes() {
		return historialClubes;
	}

	public void setHistorialClubes(List<Clubes> historialClubes) {
		this.historialClubes = historialClubes;
	}
	
	public Carnets getCarnet() {
		return carnet;
	}

	public void setCarnet(Carnets carnet) {
		this.carnet = carnet;
	}

	public LocalDate getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(LocalDate fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	private Carnets carnet;
	
	private LocalDate fechaNacimiento;

}
