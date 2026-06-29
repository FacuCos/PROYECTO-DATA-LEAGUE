package ClasesObjetos;

public class Secretario extends Usuario{
	
	private String nombreClub;
	public String getNombreClub() {
		return nombreClub;
	}

	public void setNombreClub(String nombreClub) {
		this.nombreClub = nombreClub;
	}

	private String escudoRuta;

	public String getEscudoRuta() {
		return escudoRuta;
	}

	public void setEscudoRuta(String escudoRuta) {
		this.escudoRuta = escudoRuta;
	}

	@Override
	public String getRol() {
		return "Secretario";
	}
	
	public int getIdClub() {
		return idClub;
	}

	public void setIdClub(int idClub) {
		this.idClub = idClub;
	}

	private int idClub;
}
