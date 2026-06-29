package Vista;

import java.time.LocalDate;
import java.util.List;
 
import ClasesObjetos.Categorias;
import ClasesObjetos.Clubes;
import ClasesObjetos.Partidos;
import ClasesObjetos.Usuario;
import Controladores.CategoriaControlador;
import Controladores.ClubControlador;
import Controladores.PartidoControlador;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;

public class ArbitroVista {
	
	private Usuario usuario;
	private PartidoControlador partidoControlador = new PartidoControlador();
	private ClubControlador clubControlador = new ClubControlador();
	private CategoriaControlador categoriaControlador = new CategoriaControlador();
	private TableView<Partidos> tabla;
 
	public ArbitroVista(Usuario usuario)
	{
		this.usuario = usuario;
	}
 
	public void mostrar(Stage stage)
	{
		VBox sidebar = ConstructorBarras.construir(usuario, stage);
 
		Label lblTitulo = new Label("Partidos");
		lblTitulo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #1a1a2e;");
 
		Button btnNuevo = new Button("+ Nuevo Partido");
		btnNuevo.setStyle(
				"-fx-background-color: #1a1a2e; -fx-text-fill: white; " +
				"-fx-font-size: 13px; -fx-cursor: hand; -fx-background-radius: 6px; -fx-padding: 8 16 8 16;"
		);
		btnNuevo.setOnAction(e -> abrirFormularioNuevoPartido());
 
		HBox encabezado = new HBox(16, lblTitulo, btnNuevo);
		encabezado.setStyle("-fx-alignment: CENTER-LEFT;");
		HBox.setHgrow(lblTitulo, Priority.ALWAYS);
 
		Label lblAyuda = new Label("Ingresá los goles y presioná Guardar para cargar el resultado");
		lblAyuda.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;");
 
		tabla = construirTabla();
 
		VBox contenido = new VBox(12, encabezado, lblAyuda, tabla);
		contenido.setPadding(new Insets(24));
		contenido.setStyle("-fx-background-color: #f0f2f5;");
		VBox.setVgrow(tabla, Priority.ALWAYS);
 
		HBox root = new HBox(sidebar, contenido);
		HBox.setHgrow(contenido, Priority.ALWAYS);
 
		Scene scene = new Scene(root, 1000, 560);
 
		stage.setTitle("DataLeague — Árbitro");
		stage.setScene(scene);
		stage.show();
	}
 
	/**
	 * RF-028: el arbitro logueado crea un partido nuevo. El arbitro asignado a ese
	 * partido es automaticamente el usuario logueado (no se elige de un combo),
	 * de forma analoga a como clubOrigen se fija solo en el formulario de Transferencia.
	 */
	private void abrirFormularioNuevoPartido()
	{
		Dialog<Void> dialogo = new Dialog<>();
		dialogo.setTitle("Nuevo Partido");
		dialogo.setHeaderText("Cargar un partido nuevo");
 
		List<Clubes> clubes = clubControlador.listarClubes();
 
		ComboBox<Clubes> comboClubLocal = new ComboBox<>(FXCollections.observableArrayList(clubes));
		comboClubLocal.setConverter(convertidorClub());
		comboClubLocal.setPromptText("Club local");
 
		ComboBox<Clubes> comboClubVisitante = new ComboBox<>(FXCollections.observableArrayList(clubes));
		comboClubVisitante.setConverter(convertidorClub());
		comboClubVisitante.setPromptText("Club visitante");
 
		// Las categorias dependen del club elegido (Categorias.club), por eso el combo
		// de categoria arranca vacio y se completa recien cuando se elige el club correspondiente
		ComboBox<Categorias> comboCategoriaLocal = new ComboBox<>();
		comboCategoriaLocal.setConverter(convertidorCategoria());
		comboCategoriaLocal.setPromptText("Elegir club local primero");
 
		ComboBox<Categorias> comboCategoriaVisitante = new ComboBox<>();
		comboCategoriaVisitante.setConverter(convertidorCategoria());
		comboCategoriaVisitante.setPromptText("Elegir club visitante primero");
 
		comboClubLocal.setOnAction(e ->
		{
			Clubes clubElegido = comboClubLocal.getValue();
			if(clubElegido != null)
			{
				List<Categorias> categoriasDelClub = categoriaControlador.listarCategoriasPorClub(clubElegido.getId());
				comboCategoriaLocal.setItems(FXCollections.observableArrayList(categoriasDelClub));
				comboCategoriaLocal.setPromptText("Categoría local");
			}
		});
 
		comboClubVisitante.setOnAction(e ->
		{
			Clubes clubElegido = comboClubVisitante.getValue();
			if(clubElegido != null)
			{
				List<Categorias> categoriasDelClub = categoriaControlador.listarCategoriasPorClub(clubElegido.getId());
				comboCategoriaVisitante.setItems(FXCollections.observableArrayList(categoriasDelClub));
				comboCategoriaVisitante.setPromptText("Categoría visitante");
			}
		});
 
		DatePicker selectorFecha = new DatePicker(LocalDate.now());
 
		TextField campoFechaLiga = new TextField();
		campoFechaLiga.setPromptText("Ej: Fecha 5");
 
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20));
		grid.addRow(0, new Label("Club local:"), comboClubLocal);
		grid.addRow(1, new Label("Categoría local:"), comboCategoriaLocal);
		grid.addRow(2, new Label("Club visitante:"), comboClubVisitante);
		grid.addRow(3, new Label("Categoría visitante:"), comboCategoriaVisitante);
		grid.addRow(4, new Label("Fecha del partido:"), selectorFecha);
		grid.addRow(5, new Label("Fecha de liga:"), campoFechaLiga);
 
		dialogo.getDialogPane().setContent(grid);
		dialogo.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
 
		dialogo.setResultConverter(boton ->
		{
			if(boton == ButtonType.OK)
			{
				confirmarNuevoPartido(
						comboClubLocal.getValue(), comboClubVisitante.getValue(),
						comboCategoriaLocal.getValue(), comboCategoriaVisitante.getValue(),
						selectorFecha.getValue(), campoFechaLiga.getText());
			}
			return null;
		});
 
		dialogo.showAndWait();
	}
 
	private void confirmarNuevoPartido(Clubes clubLocal, Clubes clubVisitante, Categorias categoriaLocal,
			Categorias categoriaVisitante, LocalDate fechaPartido, String fechaLiga)
	{
		if(clubLocal == null || clubVisitante == null || categoriaLocal == null
				|| categoriaVisitante == null || fechaPartido == null)
		{
			mostrarAlerta(AlertType.WARNING, "Datos incompletos", "Complete todos los campos antes de guardar");
			return;
		}
 
		if(clubLocal.getId() == clubVisitante.getId())
		{
			mostrarAlerta(AlertType.WARNING, "Datos inválidos", "El club local y visitante no pueden ser el mismo");
			return;
		}
 
		Partidos partido = new Partidos();
		partido.setClubLocal(clubLocal);
		partido.setClubVisitante(clubVisitante);
		partido.setCategoriaLocal(categoriaLocal);
		partido.setCategoriaVisitante(categoriaVisitante);
		partido.setFechaPartido(fechaPartido);
		partido.setFechaLiga(fechaLiga);
		partido.setGolLocal(0);
		partido.setGolVisitante(0);
		// El arbitro asignado al partido es el usuario logueado (debe ser instancia de Arbitro,
		// ya que esta pantalla es exclusiva de ese rol)
		partido.setArbitro((ClasesObjetos.Arbitro) usuario);
 
		String error = partidoControlador.crearPartido(usuario, partido);
 
		if(error != null)
		{
			mostrarAlerta(AlertType.ERROR, "No se pudo crear el partido", error);
		}
		else
		{
			mostrarAlerta(AlertType.INFORMATION, "Listo", "Partido creado correctamente");
			tabla.setItems(FXCollections.observableArrayList(partidoControlador.listarPartidos()));
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
 
	private StringConverter<Categorias> convertidorCategoria()
	{
		return new StringConverter<Categorias>()
		{
			@Override
			public String toString(Categorias categoria)
			{
				return categoria == null ? "" : categoria.getDivision();
			}
 
			@Override
			public Categorias fromString(String texto)
			{
				return null;
			}
		};
	}
 
	private TableView<Partidos> construirTabla()
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
 
		// Goles Local/Visitante: TextField editable. El propio campo escribe el valor
		// tipeado directo en el objeto Partidos de esa fila (via setGolLocal/setGolVisitante)
		// con un listener, asi "Guardar" solo necesita leer partido.getGolLocal()/getGolVisitante(),
		// sin tener que rastrear el TextField por indice de fila/columna.
		TableColumn<Partidos, Void> colGolLocal = new TableColumn<>("Goles Local");
		colGolLocal.setCellFactory(crearCeldaGol(true));
 
		TableColumn<Partidos, Void> colGolVisitante = new TableColumn<>("Goles Visitante");
		colGolVisitante.setCellFactory(crearCeldaGol(false));
 
		TableColumn<Partidos, Void> colAccion = new TableColumn<>("");
		colAccion.setCellFactory(crearColumnaGuardar());
 
		tabla.getColumns().addAll(colLocal, colVisitante, colFecha, colGolLocal, colGolVisitante, colAccion);
 
		List<Partidos> partidos = partidoControlador.listarPartidos();
		tabla.setItems(FXCollections.observableArrayList(partidos));
 
		return tabla;
	}
 
	private Callback<TableColumn<Partidos, Void>, TableCell<Partidos, Void>> crearCeldaGol(boolean esLocal)
	{
		return columna -> new TableCell<Partidos, Void>()
		{
			private final TextField campo = new TextField();
 
			{
				campo.setPrefWidth(60);
 
				// Cada vez que el arbitro tipea, se guarda directo en el objeto Partidos de ESTA fila
				campo.textProperty().addListener((obs, valorViejo, valorNuevo) ->
				{
					if(getTableRow() == null || getTableRow().getItem() == null)
					{
						return;
					}
 
					Partidos partido = (Partidos) getTableRow().getItem();
					try
					{
						int gol = valorNuevo.isBlank() ? 0 : Integer.parseInt(valorNuevo);
						if(esLocal)
						{
							partido.setGolLocal(gol);
						}
						else
						{
							partido.setGolVisitante(gol);
						}
					}
					catch(NumberFormatException ex)
					{
						// el arbitro todavia esta escribiendo (ej: borro el campo), se ignora hasta que sea un numero valido
					}
				});
			}
 
			@Override
			protected void updateItem(Void valor, boolean vacio)
			{
				super.updateItem(valor, vacio);
 
				if(vacio || getTableRow() == null || getTableRow().getItem() == null)
				{
					setGraphic(null);
					return;
				}
 
				Partidos partido = (Partidos) getTableRow().getItem();
				int golActual = esLocal ? partido.getGolLocal() : partido.getGolVisitante();
				campo.setText(String.valueOf(golActual));
				setGraphic(campo);
			}
		};
	}
 
	private Callback<TableColumn<Partidos, Void>, TableCell<Partidos, Void>> crearColumnaGuardar()
	{
		return columna -> new TableCell<Partidos, Void>()
		{
			private final Button btnGuardar = new Button("Guardar");
 
			{
				btnGuardar.setStyle(
						"-fx-background-color: #1a1a2e; -fx-text-fill: white; " +
						"-fx-font-size: 11px; -fx-cursor: hand; -fx-background-radius: 4px;"
				);
				btnGuardar.setOnAction(e ->
				{
					Partidos partido = (Partidos) getTableRow().getItem();
					if(partido != null)
					{
						guardarResultado(partido);
					}
				});
			}
 
			@Override
			protected void updateItem(Void valor, boolean vacio)
			{
				super.updateItem(valor, vacio);
				setGraphic(vacio ? null : btnGuardar);
			}
		};
	}
 
	/**
	 * RF-032 + RF-040: PartidoControlador.modificarResultado ya valida que el
	 * usuario logueado sea el arbitro asignado a ESE partido, y que no haya
	 * sido modificado antes. Si alguna de esas reglas falla, devuelve el
	 * mensaje de error en vez de null, y se muestra en un Alert.
	 */
	private void guardarResultado(Partidos partido)
	{
		String error = partidoControlador.modificarResultado(usuario, partido, partido.getGolLocal(), partido.getGolVisitante());
 
		if(error != null)
		{
			mostrarAlerta(AlertType.ERROR, "No se pudo guardar", error);
		}
		else
		{
			mostrarAlerta(AlertType.INFORMATION, "Listo", "Resultado guardado correctamente");
		}
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
