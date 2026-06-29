package Vista;

import java.util.List;

import ClasesObjetos.Administrador;
import ClasesObjetos.Clubes;
import ClasesObjetos.Usuario;
import Controladores.ClubControlador;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ClubVista {

	private Usuario usuario;
	private ClubControlador clubControlador = new ClubControlador();
	private TableView<Clubes> tabla;
 
	public ClubVista(Usuario usuario)
	{
		this.usuario = usuario;
	}
 
	public void mostrar(Stage stage)
	{
		VBox sidebar = ConstructorBarras.construir(usuario, stage);
 
		Label lblTitulo = new Label("Clubes");
		lblTitulo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #1a1a2e;");
 
		HBox encabezado;
 
		// RF-007: Crear Club es exclusivo del Administrador (matriz de permisos)
		if(usuario instanceof Administrador)
		{
			Button btnNuevo = new Button("+ Nuevo Club");
			btnNuevo.setStyle(
					"-fx-background-color: #1a1a2e; -fx-text-fill: white; " +
					"-fx-font-size: 13px; -fx-cursor: hand; -fx-background-radius: 6px; -fx-padding: 8 16 8 16;"
			);
			btnNuevo.setOnAction(e -> abrirFormularioNuevoClub());
 
			encabezado = new HBox(16, lblTitulo, btnNuevo);
		}
		else
		{
			encabezado = new HBox(16, lblTitulo);
		}
 
		encabezado.setStyle("-fx-alignment: CENTER-LEFT;");
		HBox.setHgrow(lblTitulo, Priority.ALWAYS);
 
		tabla = construirTabla();
 
		VBox contenido = new VBox(16, encabezado, tabla);
		contenido.setPadding(new Insets(24));
		contenido.setStyle("-fx-background-color: #f0f2f5;");
		VBox.setVgrow(tabla, Priority.ALWAYS);
 
		HBox root = new HBox(sidebar, contenido);
		HBox.setHgrow(contenido, Priority.ALWAYS);
 
		Scene scene = new Scene(root, 900, 560);
 
		stage.setTitle("DataLeague — Clubes");
		stage.setScene(scene);
		stage.show();
	}
 
	private void abrirFormularioNuevoClub()
	{
		Dialog<Void> dialogo = new Dialog<>();
		dialogo.setTitle("Nuevo Club");
		dialogo.setHeaderText("Registrar un club nuevo");
 
		TextField campoNombre = new TextField();
		campoNombre.setPromptText("Nombre del club");
 
		TextField campoEstadio = new TextField();
		campoEstadio.setPromptText("Nombre del estadio");
 
		TextField campoDireccion = new TextField();
		campoDireccion.setPromptText("Dirección del estadio");
 
		TextField campoEscudo = new TextField();
		campoEscudo.setPromptText("Ruta del escudo (ej: Imagenes/escudos/club.png)");
 
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20));
		grid.addRow(0, new Label("Nombre:"), campoNombre);
		grid.addRow(1, new Label("Estadio:"), campoEstadio);
		grid.addRow(2, new Label("Dirección:"), campoDireccion);
		grid.addRow(3, new Label("Escudo:"), campoEscudo);
 
		dialogo.getDialogPane().setContent(grid);
		dialogo.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
 
		dialogo.setResultConverter(boton ->
		{
			if(boton == ButtonType.OK)
			{
				confirmarNuevoClub(campoNombre.getText(), campoEstadio.getText(),
						campoDireccion.getText(), campoEscudo.getText());
			}
			return null;
		});
 
		dialogo.showAndWait();
	}
 
	private void confirmarNuevoClub(String nombre, String estadio, String direccion, String escudo)
	{
		if(nombre.isBlank())
		{
			mostrarAlerta(AlertType.WARNING, "Datos incompletos", "El nombre del club es obligatorio");
			return;
		}
 
		Clubes club = new Clubes();
		club.setNombre(nombre);
		club.setEstadioNombre(estadio);
		club.setDireccionEstadio(direccion);
		club.setEscudoRuta(escudo);
 
		String error = clubControlador.crearClub(usuario, club);
 
		if(error != null)
		{
			mostrarAlerta(AlertType.ERROR, "No se pudo registrar el club", error);
		}
		else
		{
			mostrarAlerta(AlertType.INFORMATION, "Listo", "Club registrado correctamente");
			tabla.setItems(FXCollections.observableArrayList(clubControlador.listarClubes()));
		}
	}
 
	private TableView<Clubes> construirTabla()
	{
		TableView<Clubes> tabla = new TableView<>();
 
		TableColumn<Clubes, String> colNombre = new TableColumn<>("Nombre");
		colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
 
		TableColumn<Clubes, String> colEstadio = new TableColumn<>("Estadio");
		colEstadio.setCellValueFactory(new PropertyValueFactory<>("estadioNombre"));
 
		TableColumn<Clubes, String> colDireccion = new TableColumn<>("Dirección");
		colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccionEstadio"));
 
		tabla.getColumns().addAll(colNombre, colEstadio, colDireccion);
 
		List<Clubes> clubes = clubControlador.listarClubes();
		tabla.setItems(FXCollections.observableArrayList(clubes));
 
		return tabla;
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
