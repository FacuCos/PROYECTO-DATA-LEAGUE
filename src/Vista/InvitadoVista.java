package Vista;

import java.util.List;
import ClasesObjetos.Clubes;
import ClasesObjetos.Jugador;
import ClasesObjetos.Partidos;
import Controladores.ClubControlador;
import Controladores.JugadorControlador;
import Controladores.PartidoControlador;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class InvitadoVista {
	
	private ClubControlador clubControlador = new ClubControlador();
	private JugadorControlador jugadorControlador = new JugadorControlador();
	private PartidoControlador partidoControlador = new PartidoControlador();
 
	public void mostrar(Stage stage)
	{
		VBox sidebar = construirSidebarSimplificado(stage);
 
		Label lblTitulo = new Label("Modo Invitado — Solo lectura");
		lblTitulo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #1a1a2e;");
 
		TabPane pestañas = new TabPane();
		pestañas.getTabs().addAll(
				crearPestañaPartidos(),
				crearPestañaClubes(),
				crearPestañaJugadores()
		);
		pestañas.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
 
		VBox contenido = new VBox(16, lblTitulo, pestañas);
		contenido.setPadding(new Insets(24));
		contenido.setStyle("-fx-background-color: #f0f2f5;");
		VBox.setVgrow(pestañas, Priority.ALWAYS);
 
		HBox root = new HBox(sidebar, contenido);
		HBox.setHgrow(contenido, Priority.ALWAYS);
 
		Scene scene = new Scene(root, 950, 600);
 
		stage.setTitle("DataLeague — Invitado");
		stage.setScene(scene);
		stage.show();
	}
 
	private VBox construirSidebarSimplificado(Stage stage)
	{
		VBox sidebar = new VBox(16);
		sidebar.setPrefWidth(200);
		sidebar.setPadding(new Insets(20));
		sidebar.setStyle("-fx-background-color: #1a1a2e;");
 
		Label lblTitulo = new Label("DataLeague");
		lblTitulo.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");
 
		Label lblModo = new Label("Modo Invitado");
		lblModo.setStyle("-fx-text-fill: #aaa; -fx-font-size: 12px;");
 
		Separator sep = new Separator();
 
		Region spacer = new Region();
		VBox.setVgrow(spacer, Priority.ALWAYS);
 
		Button btnVolver = new Button("← Volver al inicio de sesión");
		btnVolver.setStyle(
				"-fx-background-color: transparent; -fx-text-fill: #4a9eff; " +
				"-fx-font-size: 13px; -fx-cursor: hand; -fx-padding: 8 0 8 0;"
		);
		btnVolver.setOnAction(e -> new LoginVista().start(stage));
 
		sidebar.getChildren().addAll(lblTitulo, lblModo, sep, spacer, btnVolver);
 
		return sidebar;
	}
 
	private Tab crearPestañaPartidos()
	{
		TableView<Partidos> tabla = new TableView<>();
 
		TableColumn<Partidos, String> colLocal = new TableColumn<>("Local");
		colLocal.setCellValueFactory(fila -> new SimpleStringProperty(
				fila.getValue().getClubLocal() != null ? fila.getValue().getClubLocal().getNombre() : ""));
 
		TableColumn<Partidos, String> colVisitante = new TableColumn<>("Visitante");
		colVisitante.setCellValueFactory(fila -> new SimpleStringProperty(
				fila.getValue().getClubVisitante() != null ? fila.getValue().getClubVisitante().getNombre() : ""));
 
		TableColumn<Partidos, String> colFecha = new TableColumn<>("Fecha");
		colFecha.setCellValueFactory(fila -> new SimpleStringProperty(
				fila.getValue().getFechaPartido() != null ? fila.getValue().getFechaPartido().toString() : ""));
 
		TableColumn<Partidos, String> colResultado = new TableColumn<>("Resultado");
		colResultado.setCellValueFactory(fila -> new SimpleStringProperty(
				fila.getValue().getGolLocal() + " - " + fila.getValue().getGolVisitante()));
 
		tabla.getColumns().addAll(colLocal, colVisitante, colFecha, colResultado);
 
		List<Partidos> partidos = partidoControlador.listarPartidos();
		tabla.setItems(FXCollections.observableArrayList(partidos));
 
		Tab tab = new Tab("Partidos", tabla);
		return tab;
	}
 
	private Tab crearPestañaClubes()
	{
		TableView<Clubes> tabla = new TableView<>();
 
		TableColumn<Clubes, String> colNombre = new TableColumn<>("Nombre");
		colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
 
		TableColumn<Clubes, String> colEstadio = new TableColumn<>("Estadio");
		colEstadio.setCellValueFactory(new PropertyValueFactory<>("estadioNombre"));
 
		tabla.getColumns().addAll(colNombre, colEstadio);
 
		List<Clubes> clubes = clubControlador.listarClubes();
		tabla.setItems(FXCollections.observableArrayList(clubes));
 
		Tab tab = new Tab("Clubes", tabla);
		return tab;
	}
 
	private Tab crearPestañaJugadores()
	{
		TableView<Jugador> tabla = new TableView<>();
 
		TableColumn<Jugador, String> colNombre = new TableColumn<>("Nombre");
		colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
 
		TableColumn<Jugador, String> colApellido = new TableColumn<>("Apellido");
		colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
 
		TableColumn<Jugador, String> colClub = new TableColumn<>("Club");
		colClub.setCellValueFactory(fila -> new SimpleStringProperty(
				fila.getValue().getClub() != null ? fila.getValue().getClub().getNombre() : ""));
 
		TableColumn<Jugador, String> colCategoria = new TableColumn<>("Categoría");
		colCategoria.setCellValueFactory(fila -> new SimpleStringProperty(
				fila.getValue().getCategoria() != null ? fila.getValue().getCategoria().getDivision() : ""));
 
		tabla.getColumns().addAll(colNombre, colApellido, colClub, colCategoria);
 
		List<Jugador> jugadores = jugadorControlador.listarJugadores();
		tabla.setItems(FXCollections.observableArrayList(jugadores));
 
		Tab tab = new Tab("Jugadores", tabla);
		return tab;
	}

}
