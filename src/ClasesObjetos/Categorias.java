	package ClasesObjetos;
	
	public class Categorias {
		private Clubes club;
	
		public Clubes getClub() {
			return club;
		}
	
		public void setClub(Clubes club) {
			this.club = club;
		}
		
		private int id;
	
		public int getId() {
			return id;
		}
	
		public void setId(int id) {
			this.id = id;
		}
		
		private String division;
		
		public String getDivision() {
			return division;
		}

		public void setDivision(String division) {
			this.division = division;
		}

		private int cantidadDeJugadores;
	
		public int getCantidadDeJugadores() {
			return cantidadDeJugadores;
		}
	
		public void setCantidadDeJugadores(int cantidadDeJugadores) {
			this.cantidadDeJugadores = cantidadDeJugadores;
		}
		
		private int edadMinima;

		public int getEdadMinima() {
			return edadMinima;
		}

		public void setEdadMinima(int edadMinima) {
			this.edadMinima = edadMinima;
		}
		
		private int edadMaxima;

		public int getEdadMaxima() {
			return edadMaxima;
		}

		public void setEdadMaxima(int edadMaxima) {
			this.edadMaxima = edadMaxima;
		}
	}
