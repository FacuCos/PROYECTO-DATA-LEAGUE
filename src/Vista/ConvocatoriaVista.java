package Vista;

import java.util.List;

import ClasesObjetos.Jugador;
import ClasesObjetos.Partidos;
import ClasesObjetos.Secretario;
import ClasesObjetos.Usuario;
import Controladores.JugadorControlador;
import Controladores.PartidoControlador;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class ConvocatoriaVista {
	
	private Usuario usuario;
	private PartidoControlador partidoControlador = new PartidoControlador();
	private JugadorControlador jugadorControlador = new JugadorControlador();
 
	private ListView<Jugador> listaDisponibles;
	private ListView<Jugador> listaConvocados;
	private Partidos partidoSeleccionado;
	private boolean clubEsLocalEnElPartido;
 
	public ConvocatoriaVista(Usuario usuario)
	{
		this.usuario = usuario;
	}
 
	public void mostrar(Stage stage)
	{
		VBox sidebar = ConstructorBarras.construir(usuario, stage);
 
		Label lblTitulo = new Label("Convocatoria de Jugadores");
		lblTitulo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #1a1a2e;");
 
		if(!(usuario instanceof Secretario))
		{
			Label lblNoAutorizado = new Label("Solo un Secretario puede convocar jugadores");
			VBox contenidoVacio = new VBox(16, lblTitulo, lblNoAutorizado);
			contenidoVacio.setPadding(new Insets(24));
			HBox rootVacio = new HBox(sidebar, contenidoVacio);
			stage.setScene(new Scene(rootVacio, 900, 560));
			stage.setTitle("DataLeague — Convocatoria");
			stage.show();
			return;
		}
 
		Secretario secretario = (Secretario) usuario;
 
		ComboBox<Partidos> comboPartido = new ComboBox<>(
				FXCollections.observableArrayList(partidoControlador.listarPartidosPorClub(secretario.getIdClub())));
		comboPartido.setConverter(convertidorPartido());
		comboPartido.setPromptText("Seleccionar partido");
 
		Label lblDisponibles = new Label("Jugadores del club");
		lblDisponibles.setStyle("-fx-font-size: 13px; -fx-font-weight: bold;");
		listaDisponibles = new ListView<>();
		listaDisponibles.setCellFactory(lv -> crearCeldaJugador());
 
		Label lblConvocados = new Label("Convocados (máx. 16)");
		lblConvocados.setStyle("-fx-font-size: 13px; -fx-font-weight: bold;");
		listaConvocados = new ListView<>();
		listaConvocados.setCellFactory(lv -> crearCeldaJugador());
 
		Button btnConvocar = new Button("Convocar →");
		btnConvocar.setOnAction(e -> convocarSeleccionado(secretario));
 
		VBox columnaDisponibles = new VBox(8, lblDisponibles, listaDisponibles);
		VBox columnaBoton = new VBox(btnConvocar);
		columnaBoton.setStyle("-fx-alignment: CENTER;");
		VBox columnaConvocados = new VBox(8, lblConvocados, listaConvocados);
 
		HBox columnas = new HBox(16, columnaDisponibles, columnaBoton, columnaConvocados);
		HBox.setHgrow(columnaDisponibles, Priority.ALWAYS);
		HBox.setHgrow(columnaConvocados, Priority.ALWAYS);
 
		comboPartido.setOnAction(e ->
		{
			Partidos partido = comboPartido.getValue();
			if(partido != null)
			{
				seleccionarPartido(partido, secretario);
			}
		});
 
		VBox contenido = new VBox(16, lblTitulo, comboPartido, columnas);
		contenido.setPadding(new Insets(24));
		contenido.setStyle("-fx-background-color: #f0f2f5;");
		VBox.setVgrow(columnas, Priority.ALWAYS);
 
		HBox root = new HBox(sidebar, contenido);
		HBox.setHgrow(contenido, Priority.ALWAYS);
 
		Scene scene = new Scene(root, 950, 600);
 
		stage.setTitle("DataLeague — Convocatoria");
		stage.setScene(scene);
		stage.show();
	}
 
	/**
	 * Al elegir un partido, se determina si el club del secretario es local o visitante
	 * en ESE partido en particular (un mismo club puede ser local en un partido y
	 * visitante en otro), y se cargan las listas correspondientes.
	 */
	private void seleccionarPartido(Partidos partido, Secretario secretario)
	{
		partidoSeleccionado = partido;
		clubEsLocalEnElPartido = partido.getClubLocal() != null
				&& partido.getClubLocal().getId() == secretario.getIdClub();
 
		List<Jugador> todosLosJugadoresDelClub = jugadorControlador.listarJugadoresPorClub(secretario.getIdClub());
		List<Jugador> yaConvocados = clubEsLocalEnElPartido
				? partidoControlador.obtenerJugadoresLocal(partido.getIdPartido())
				: partidoControlador.obtenerJugadoresVisitante(partido.getIdPartido());
 
		// Disponibles = todos los del club MENOS los que ya estan convocados a este partido
		List<Jugador> disponibles = todosLosJugadoresDelClub.stream()
				.filter(j -> yaConvocados.stream().noneMatch(c -> c.getIdJugador() == j.getIdJugador()))
				.toList();
 
		listaDisponibles.setItems(FXCollections.observableArrayList(disponibles));
		listaConvocados.setItems(FXCollections.observableArrayList(yaConvocados));
	}
 
	private void convocarSeleccionado(Secretario secretario)
	{
		if(partidoSeleccionado == null)
		{
			mostrarAlerta(AlertType.WARNING, "Atención", "Primero seleccione un partido");
			return;
		}
 
		Jugador jugador = listaDisponibles.getSelectionModel().getSelectedItem();
		if(jugador == null)
		{
			mostrarAlerta(AlertType.WARNING, "Atención", "Seleccione un jugador de la lista de disponibles");
			return;
		}
 
		// RF-027: advertencia (no bloqueo) si el jugador ya jugo o tiene carnet vencido
		String advertencia = jugadorControlador.validarConvocatoria(jugador);
		if(advertencia != null)
		{
			mostrarAlerta(AlertType.WARNING, "Advertencia", advertencia + "\n\nSe convocará igual, pero revise la situación del jugador.");
		}
 
		String error = clubEsLocalEnElPartido
				? partidoControlador.convocarJugadorLocal(partidoSeleccionado.getIdPartido(), jugador.getIdJugador())
				: partidoControlador.convocarJugadorVisitante(partidoSeleccionado.getIdPartido(), jugador.getIdJugador());
 
		if(error != null)
		{
			mostrarAlerta(AlertType.ERROR, "No se pudo convocar", error);
		}
		else
		{
			// Se refresca recargando el partido seleccionado para mover al jugador de lista
			seleccionarPartido(partidoSeleccionado, secretario);
		}
	}
 
	private ListCell<Jugador> crearCeldaJugador()
	{
		return new ListCell<Jugador>()
		{
			@Override
			protected void updateItem(Jugador jugador, boolean vacio)
			{
				super.updateItem(jugador, vacio);
				setText(vacio || jugador == null ? null : jugador.getNombre() + " " + jugador.getApellido() + " (DNI " + jugador.getDni() + ")");
			}
		};
	}
 
	private StringConverter<Partidos> convertidorPartido()
	{
		return new StringConverter<Partidos>()
		{
			@Override
			public String toString(Partidos partido)
			{
				if(partido == null)
				{
					return "";
				}
				String local = partido.getClubLocal() != null ? partido.getClubLocal().getNombre() : "?";
				String visitante = partido.getClubVisitante() != null ? partido.getClubVisitante().getNombre() : "?";
				String fecha = partido.getFechaPartido() != null ? partido.getFechaPartido().toString() : "";
				return local + " vs " + visitante + " (" + fecha + ")";
			}
 
			@Override
			public Partidos fromString(String texto)
			{
				return null;
			}
		};
	}
 
	private void mostrarAlerta(AlertType tipo, String titulo, String mensaje)
	{
		Alert alerta = new Alert(tipo);
		alerta.setTitle(titulo);
		alerta.setHeaderText(null);
		alerta.setContentText(mensaje);
		alerta.showAndWait();
	}

}
