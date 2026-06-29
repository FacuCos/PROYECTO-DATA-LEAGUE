package Vista;

import java.util.List;

import ClasesObjetos.Administrador;
import ClasesObjetos.Categorias;
import ClasesObjetos.Clubes;
import ClasesObjetos.Usuario;
import Controladores.CategoriaControlador;
import Controladores.ClubControlador;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
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
import javafx.util.StringConverter;

public class CategoriaVista {
	
	private Usuario usuario;
	private CategoriaControlador categoriaControlador = new CategoriaControlador();
	private ClubControlador clubControlador = new ClubControlador();
	private TableView<Categorias> tabla;
 
	public CategoriaVista(Usuario usuario)
	{
		this.usuario = usuario;
	}
 
	public void mostrar(Stage stage)
	{
		VBox sidebar = ConstructorBarras.construir(usuario, stage);
 
		Label lblTitulo = new Label("Categorías");
		lblTitulo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #1a1a2e;");
 
		HBox encabezado;
 
		// RF-012/RN-007 a 011: Crear Categoria es exclusivo del Administrador (matriz de permisos)
		if(usuario instanceof Administrador)
		{
			Button btnNueva = new Button("+ Nueva Categoría");
			btnNueva.setStyle(
					"-fx-background-color: #1a1a2e; -fx-text-fill: white; " +
					"-fx-font-size: 13px; -fx-cursor: hand; -fx-background-radius: 6px; -fx-padding: 8 16 8 16;"
			);
			btnNueva.setOnAction(e -> abrirFormularioNuevaCategoria());
 
			encabezado = new HBox(16, lblTitulo, btnNueva);
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
 
		stage.setTitle("DataLeague — Categorías");
		stage.setScene(scene);
		stage.show();
	}
 
	/**
	 * RF-012 + RN-007 a 011: el Administrador crea una categoria nueva para un club,
	 * indicando manualmente la division (ej: "Sub-9", "Primera division") y el rango
	 * de edad/cupo. No se valida aca que la division siga exactamente el formato de
	 * ValidacionJugador.calcularDivisionEsperada: eso se revisa recien cuando un jugador
	 * intenta asignarse a esta categoria (RF-020), no en el alta de la categoria misma.
	 */
	private void abrirFormularioNuevaCategoria()
	{
		Dialog<Void> dialogo = new Dialog<>();
		dialogo.setTitle("Nueva Categoría");
		dialogo.setHeaderText("Registrar una categoría nueva");
 
		ComboBox<Clubes> comboClub = new ComboBox<>(FXCollections.observableArrayList(clubControlador.listarClubes()));
		comboClub.setConverter(convertidorClub());
		comboClub.setPromptText("Club");
 
		TextField campoDivision = new TextField();
		campoDivision.setPromptText("Ej: Sub-9, 5ta division, Primera division, Veteranos");
 
		TextField campoCantidadJugadores = new TextField();
		campoCantidadJugadores.setPromptText("Cantidad de jugadores (22 a 28, RN-004)");
 
		TextField campoEdadMinima = new TextField();
		campoEdadMinima.setPromptText("Edad mínima");
 
		TextField campoEdadMaxima = new TextField();
		campoEdadMaxima.setPromptText("Edad máxima");
 
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20));
		grid.addRow(0, new Label("Club:"), comboClub);
		grid.addRow(1, new Label("División:"), campoDivision);
		grid.addRow(2, new Label("Cantidad de jugadores:"), campoCantidadJugadores);
		grid.addRow(3, new Label("Edad mínima:"), campoEdadMinima);
		grid.addRow(4, new Label("Edad máxima:"), campoEdadMaxima);
 
		dialogo.getDialogPane().setContent(grid);
		dialogo.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
 
		dialogo.setResultConverter(boton ->
		{
			if(boton == ButtonType.OK)
			{
				confirmarNuevaCategoria(comboClub.getValue(), campoDivision.getText(),
						campoCantidadJugadores.getText(), campoEdadMinima.getText(), campoEdadMaxima.getText());
			}
			return null;
		});
 
		dialogo.showAndWait();
	}
 
	private void confirmarNuevaCategoria(Clubes club, String division, String textoCantidad,
			String textoEdadMinima, String textoEdadMaxima)
	{
		if(club == null || division.isBlank())
		{
			mostrarAlerta(AlertType.WARNING, "Datos incompletos", "Club y división son obligatorios");
			return;
		}
 
		int cantidadJugadores;
		int edadMinima;
		int edadMaxima;
 
		try
		{
			cantidadJugadores = textoCantidad.isBlank() ? 0 : Integer.parseInt(textoCantidad);
			edadMinima = textoEdadMinima.isBlank() ? 0 : Integer.parseInt(textoEdadMinima);
			edadMaxima = textoEdadMaxima.isBlank() ? 0 : Integer.parseInt(textoEdadMaxima);
		}
		catch(NumberFormatException ex)
		{
			mostrarAlerta(AlertType.WARNING, "Datos inválidos", "Cantidad de jugadores y edades deben ser números");
			return;
		}
 
		// RN-004: cupo entre 22 y 28. Se valida aca, en el alta de la categoria misma
		// (distinto de ValidacionJugador.validarCupoCategoria
		if(cantidadJugadores != 0 && (cantidadJugadores < 22 || cantidadJugadores > 28))
		{
			mostrarAlerta(AlertType.WARNING, "Dato inválido", "La cantidad de jugadores debe estar entre 22 y 28 (RN-004)");
			return;
		}
 
		Categorias categoria = new Categorias();
		categoria.setClub(club);
		categoria.setDivision(division);
		categoria.setCantidadDeJugadores(cantidadJugadores);
		categoria.setEdadMinima(edadMinima);
		categoria.setEdadMaxima(edadMaxima);
 
		String error = categoriaControlador.crearCategoria(usuario, categoria);
 
		if(error != null)
		{
			mostrarAlerta(AlertType.ERROR, "No se pudo registrar la categoría", error);
		}
		else
		{
			mostrarAlerta(AlertType.INFORMATION, "Listo", "Categoría registrada correctamente");
			tabla.setItems(FXCollections.observableArrayList(categoriaControlador.listarCategorias()));
		}
	}
 
	private StringConverter<Clubes> convertidorClub()
	{
		return new StringConverter<Clubes>()
		{
			@Override
			public String toString(Clubes club)
			{
				return club == null ? "" : club.getNombre();
			}
 
			@Override
			public Clubes fromString(String texto)
			{
				return null;
			}
		};
	}
 
	private TableView<Categorias> construirTabla()
	{
		TableView<Categorias> tabla = new TableView<>();
 
		TableColumn<Categorias, String> colDivision = new TableColumn<>("División");
		colDivision.setCellValueFactory(new PropertyValueFactory<>("division"));
 
		TableColumn<Categorias, String> colClub = new TableColumn<>("Club");
		colClub.setCellValueFactory(fila -> new javafx.beans.property.SimpleStringProperty(
				fila.getValue().getClub() != null ? fila.getValue().getClub().getNombre() : ""));
 
		TableColumn<Categorias, Number> colCantidad = new TableColumn<>("Cantidad Jugadores");
		colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidadDeJugadores"));
 
		TableColumn<Categorias, Number> colEdadMin = new TableColumn<>("Edad Mín.");
		colEdadMin.setCellValueFactory(new PropertyValueFactory<>("edadMinima"));
 
		TableColumn<Categorias, Number> colEdadMax = new TableColumn<>("Edad Máx.");
		colEdadMax.setCellValueFactory(new PropertyValueFactory<>("edadMaxima"));
 
		tabla.getColumns().addAll(colDivision, colClub, colCantidad, colEdadMin, colEdadMax);
 
		List<Categorias> categorias = categoriaControlador.listarCategorias();
		tabla.setItems(FXCollections.observableArrayList(categorias));
 
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
