package Vista;

import java.time.LocalDate;
import java.util.List;
 
import ClasesObjetos.Carnets;
import ClasesObjetos.Categorias;
import ClasesObjetos.Jugador;
import ClasesObjetos.Secretario;
import ClasesObjetos.Usuario;
import Controladores.CarnetControlador;
import Controladores.JugadorControlador;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
 

public class JugadoresVista {
	

	private Usuario usuario;
	private JugadorControlador jugadorControlador = new JugadorControlador();
	private CarnetControlador carnetControlador = new CarnetControlador();
	private TableView<Jugador> tabla;
 
	public JugadoresVista(Usuario usuario)
	{
		this.usuario = usuario;
	}
 
	public void mostrar(Stage stage)
	{
		VBox sidebar = ConstructorBarras.construir(usuario, stage);
 
		Label lblTitulo = new Label("Jugadores");
		lblTitulo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #1a1a2e;");
 
		Button btnNuevo = new Button("+ Nuevo Jugador");
		btnNuevo.setStyle(
				"-fx-background-color: #1a1a2e; -fx-text-fill: white; " +
				"-fx-font-size: 13px; -fx-cursor: hand; -fx-background-radius: 6px; -fx-padding: 8 16 8 16;"
		);
		btnNuevo.setOnAction(e -> abrirFormularioNuevoJugador());
 
		HBox encabezado = new HBox(16, lblTitulo, btnNuevo);
		encabezado.setStyle("-fx-alignment: CENTER-LEFT;");
		HBox.setHgrow(lblTitulo, Priority.ALWAYS);
 
		tabla = construirTabla();
 
		VBox contenido = new VBox(16, encabezado, tabla);
		contenido.setPadding(new Insets(24));
		contenido.setStyle("-fx-background-color: #f0f2f5;");
		VBox.setVgrow(tabla, Priority.ALWAYS);
 
		HBox root = new HBox(sidebar, contenido);
		HBox.setHgrow(contenido, Priority.ALWAYS);
 
		Scene scene = new Scene(root, 1000, 560);
 
		stage.setTitle("DataLeague — Jugadores");
		stage.setScene(scene);
		stage.show();
	}
 
	/**
	 * RF-017 + RF-020: el secretario logueado da de alta un jugador en SU club
	 * (igual patron que clubOrigen en TransferenciaVista: el club no se elige,
	 * es siempre el del secretario). La categoria se calcula sola segun la fecha
	 * de nacimiento (RN-006 a RN-011, via JugadorControlador.buscarCategoriaPorEdad)
	 * y se muestra como texto NO editable.
	 */
	private void abrirFormularioNuevoJugador()
	{
		if(!(usuario instanceof Secretario))
		{
			mostrarAlerta(AlertType.ERROR, "No autorizado", "Solo un Secretario puede registrar jugadores");
			return;
		}
 
		Secretario secretario = (Secretario) usuario;
 
		Dialog<Void> dialogo = new Dialog<>();
		dialogo.setTitle("Nuevo Jugador");
		dialogo.setHeaderText("Registrar un jugador en " + secretario.getNombreClub());
 
		TextField campoNombre = new TextField();
		campoNombre.setPromptText("Nombre");
 
		TextField campoApellido = new TextField();
		campoApellido.setPromptText("Apellido");
 
		TextField campoDni = new TextField();
		campoDni.setPromptText("DNI");
 
		DatePicker selectorFechaNacimiento = new DatePicker();
 
		TextField campoDomicilio = new TextField();
		campoDomicilio.setPromptText("Domicilio");
 
		TextField campoTelefono = new TextField();
		campoTelefono.setPromptText("Teléfono");
 
		TextField campoGmail = new TextField();
		campoGmail.setPromptText("Gmail");
 
		// Categoria: se calcula sola, no es editable. Se muestra en un Label, no en un campo de texto.
		Label lblCategoriaCalculada = new Label("Ingrese la fecha de nacimiento");
		lblCategoriaCalculada.setStyle("-fx-font-style: italic; -fx-text-fill: #666;");
 
		// Se guarda la categoria encontrada en un arreglo de 1 elemento para poder
		// modificarla desde dentro del listener (las lambdas necesitan variables "efectivamente finales")
		Categorias[] categoriaEncontrada = new Categorias[1];
 
		selectorFechaNacimiento.valueProperty().addListener((obs, fechaVieja, fechaNueva) ->
		{
			if(fechaNueva == null)
			{
				return;
			}
 
			Jugador jugadorTemporal = new Jugador();
			jugadorTemporal.setFechaNacimiento(fechaNueva);
 
			Categorias categoria = jugadorControlador.buscarCategoriaPorEdad(jugadorTemporal, secretario.getIdClub());
			categoriaEncontrada[0] = categoria;
 
			if(categoria != null)
			{
				lblCategoriaCalculada.setText(categoria.getDivision());
				lblCategoriaCalculada.setStyle("-fx-font-weight: bold; -fx-text-fill: #1a1a2e;");
			}
			else
			{
				lblCategoriaCalculada.setText("No existe una categoría para esa edad en este club");
				lblCategoriaCalculada.setStyle("-fx-font-style: italic; -fx-text-fill: #c0392b;");
			}
		});
 
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20));
		grid.addRow(0, new Label("Nombre:"), campoNombre);
		grid.addRow(1, new Label("Apellido:"), campoApellido);
		grid.addRow(2, new Label("DNI:"), campoDni);
		grid.addRow(3, new Label("Fecha de nacimiento:"), selectorFechaNacimiento);
		grid.addRow(4, new Label("Categoría:"), lblCategoriaCalculada);
		grid.addRow(5, new Label("Domicilio:"), campoDomicilio);
		grid.addRow(6, new Label("Teléfono:"), campoTelefono);
		grid.addRow(7, new Label("Gmail:"), campoGmail);
 
		dialogo.getDialogPane().setContent(grid);
		dialogo.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
 
		dialogo.setResultConverter(boton ->
		{
			if(boton == ButtonType.OK)
			{
				confirmarNuevoJugador(secretario, campoNombre.getText(), campoApellido.getText(), campoDni.getText(),
						selectorFechaNacimiento.getValue(), categoriaEncontrada[0], campoDomicilio.getText(),
						campoTelefono.getText(), campoGmail.getText());
			}
			return null;
		});
 
		dialogo.showAndWait();
	}
 
	private void confirmarNuevoJugador(Secretario secretario, String nombre, String apellido, String dni,
			LocalDate fechaNacimiento, Categorias categoria, String domicilio, String telefono, String gmail)
	{
		if(nombre.isBlank() || apellido.isBlank() || dni.isBlank() || fechaNacimiento == null)
		{
			mostrarAlerta(AlertType.WARNING, "Datos incompletos", "Nombre, apellido, DNI y fecha de nacimiento son obligatorios");
			return;
		}
 
		if(categoria == null)
		{
			mostrarAlerta(AlertType.WARNING, "Categoría no encontrada",
					"No existe una categoría creada para la edad de este jugador en el club. "
					+ "Debe crearse esa categoría antes de poder registrar al jugador.");
			return;
		}
 
		Jugador jugador = new Jugador();
		jugador.setNombre(nombre);
		jugador.setApellido(apellido);
		jugador.setDni(dni);
		jugador.setFechaNacimiento(fechaNacimiento);
		jugador.setDomicilio(domicilio);
		jugador.setTelefono(telefono);
		jugador.setGmail(gmail);
		jugador.setClub(construirClubDelSecretario(secretario));
		jugador.setCategoria(categoria);
		jugador.setYaJugo(false);
 
		String error = jugadorControlador.registrarJugador(jugador, categoria);
 
		if(error != null)
		{
			mostrarAlerta(AlertType.ERROR, "No se pudo registrar el jugador", error);
		}
		else
		{
			mostrarAlerta(AlertType.INFORMATION, "Listo", "Jugador registrado correctamente");
			refrescarTabla();
		}
	}
 
	/**
	 * Secretario no guarda un objeto Clubes completo, solo idClub/nombreClub/escudoRuta
	 * sueltos (ver Secretario.java). Como Jugador.club necesita un objeto Clubes,
	 * se construye uno minimo con el id (suficiente para la FK al guardar en la base).
	 */
	private ClasesObjetos.Clubes construirClubDelSecretario(Secretario secretario)
	{
		ClasesObjetos.Clubes club = new ClasesObjetos.Clubes();
		club.setId(secretario.getIdClub());
		club.setNombre(secretario.getNombreClub());
		club.setEscudoRuta(secretario.getEscudoRuta());
		return club;
	}
 
	/**
	 * RF-051: renueva el carnet vencido de un jugador. CarnetControlador valida
	 * internamente que la fecha de hoy este dentro de un periodo habilitado (RF-023)
	 * y recalcula fechaVencimiento/fechaLimiteRenovacion segun RN-015.
	 */
	private void renovarCarnetDe(Jugador jugador)
	{
		Carnets carnet = jugador.getCarnet();
 
		if(carnet == null)
		{
			mostrarAlerta(AlertType.WARNING, "Sin carnet", "Este jugador no tiene un carnet emitido todavía");
			return;
		}
 
		String error = carnetControlador.renovarCarnet(carnet);
 
		if(error != null)
		{
			mostrarAlerta(AlertType.ERROR, "No se pudo renovar", error);
		}
		else
		{
			mostrarAlerta(AlertType.INFORMATION, "Listo", "Carnet renovado correctamente");
			refrescarTabla();
		}
	}
 
	/**
	 * Recarga la tabla respetando el mismo filtro por club que se usa al construirla
	 * (RN-013 ampliada: el Secretario solo ve sus propios jugadores).
	 */
	private void refrescarTabla()
	{
		List<Jugador> jugadores;
 
		if(usuario instanceof Secretario)
		{
			jugadores = jugadorControlador.listarJugadoresPorClub(((Secretario) usuario).getIdClub());
		}
		else
		{
			jugadores = jugadorControlador.listarJugadores();
		}
 
		tabla.setItems(FXCollections.observableArrayList(jugadores));
	}
 
	private TableView<Jugador> construirTabla()
	{
		TableView<Jugador> tabla = new TableView<>();
 
		TableColumn<Jugador, String> colNombre = new TableColumn<>("Nombre");
		colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
 
		TableColumn<Jugador, String> colApellido = new TableColumn<>("Apellido");
		colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
 
		TableColumn<Jugador, String> colDni = new TableColumn<>("DNI");
		colDni.setCellValueFactory(new PropertyValueFactory<>("dni"));
 
		TableColumn<Jugador, String> colClub = new TableColumn<>("Club");
		colClub.setCellValueFactory(fila -> new SimpleStringProperty(
				fila.getValue().getClub() != null ? fila.getValue().getClub().getNombre() : ""));
 
		TableColumn<Jugador, String> colCategoria = new TableColumn<>("Categoría");
		colCategoria.setCellValueFactory(fila -> new SimpleStringProperty(
				fila.getValue().getCategoria() != null ? fila.getValue().getCategoria().getDivision() : ""));
 
		// Estado del carnet: se recalcula la vigencia real (RN-015) en cada lectura,
		// no se confia ciegamente en lo que tenga guardado Carnets.estado en la base
		TableColumn<Jugador, String> colCarnet = new TableColumn<>("Carnet");
		colCarnet.setCellValueFactory(fila ->
		{
			Carnets carnet = fila.getValue().getCarnet();
 
			if(carnet == null)
			{
				return new SimpleStringProperty("Sin carnet");
			}
 
			boolean vigente = carnetControlador.actualizarEstadoVigencia(carnet);
			String texto = vigente ? "Vigente hasta " + carnet.getFechaVencimiento() : "VENCIDO";
			return new SimpleStringProperty(texto);
		});
 
		// Boton Renovar: solo visible si HAY carnet y esta vencido. Si no tiene carnet
		// en absoluto, o si esta vigente, no se muestra (no aplica "renovar" en esos casos).
		TableColumn<Jugador, Void> colRenovar = new TableColumn<>("");
		colRenovar.setCellFactory(columna -> new TableCell<Jugador, Void>()
		{
			private final Button btnRenovar = new Button("Renovar carnet");
 
			{
				btnRenovar.setStyle(
						"-fx-background-color: #1a1a2e; -fx-text-fill: white; " +
						"-fx-font-size: 11px; -fx-cursor: hand; -fx-background-radius: 4px;"
				);
				btnRenovar.setOnAction(e ->
				{
					Jugador jugadorDeLaFila = getTableRow().getItem();
					if(jugadorDeLaFila != null)
					{
						renovarCarnetDe(jugadorDeLaFila);
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
 
				Jugador jugadorDeLaFila = getTableRow().getItem();
				Carnets carnet = jugadorDeLaFila.getCarnet();
 
				boolean mostrarBoton = carnet != null && !carnetControlador.actualizarEstadoVigencia(carnet);
				setGraphic(mostrarBoton ? btnRenovar : null);
			}
		});
 
		tabla.getColumns().addAll(colNombre, colApellido, colDni, colClub, colCategoria, colCarnet, colRenovar);
 
		List<Jugador> jugadores;
		if(usuario instanceof Secretario)
		{
			jugadores = jugadorControlador.listarJugadoresPorClub(((Secretario) usuario).getIdClub());
		}
		else
		{
			jugadores = jugadorControlador.listarJugadores();
		}
 
		tabla.setItems(FXCollections.observableArrayList(jugadores));
 
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
