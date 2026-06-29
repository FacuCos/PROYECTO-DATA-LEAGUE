package Vista;


import java.time.LocalDate;
import java.util.List;

import ClasesObjetos.Clubes;
import ClasesObjetos.Jugador;
import ClasesObjetos.Secretario;
import ClasesObjetos.Tranferencias;
import ClasesObjetos.Usuario;
import Controladores.ClubControlador;
import Controladores.JugadorControlador;
import Controladores.TransferenciaControlador;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class TransferenciaVista {
	
	private Usuario usuario;
	private TransferenciaControlador transferenciaControlador = new TransferenciaControlador();
	private JugadorControlador jugadorControlador = new JugadorControlador();
	private ClubControlador clubControlador = new ClubControlador();
	private TableView<Tranferencias> tabla;
 
	public TransferenciaVista(Usuario usuario)
	{
		this.usuario = usuario;
	}
 
	public void mostrar(Stage stage)
	{
		VBox sidebar = ConstructorBarras.construir(usuario, stage);
 
		Label lblTitulo = new Label("Transferencias");
		lblTitulo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #1a1a2e;");
 
		Button btnNueva = new Button("+ Nueva Transferencia");
		btnNueva.setStyle(
				"-fx-background-color: #1a1a2e; -fx-text-fill: white; " +
				"-fx-font-size: 13px; -fx-cursor: hand; -fx-background-radius: 6px; -fx-padding: 8 16 8 16;"
		);
		btnNueva.setOnAction(e -> abrirFormularioNuevaTransferencia());
 
		HBox encabezado = new HBox(16, lblTitulo, btnNueva);
		encabezado.setStyle("-fx-alignment: CENTER-LEFT;");
		HBox.setHgrow(lblTitulo, Priority.ALWAYS);
 
		VBox seccionPendientes = construirSeccionPendientes();
 
		Label lblTodas = new Label("Todas las transferencias");
		lblTodas.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #1a1a2e;");
 
		tabla = construirTabla();
 
		VBox contenido = new VBox(16, encabezado, seccionPendientes, lblTodas, tabla);
		contenido.setPadding(new Insets(24));
		contenido.setStyle("-fx-background-color: #f0f2f5;");
		VBox.setVgrow(tabla, Priority.ALWAYS);
 
		HBox root = new HBox(sidebar, contenido);
		HBox.setHgrow(contenido, Priority.ALWAYS);
 
		Scene scene = new Scene(root, 950, 620);
 
		stage.setTitle("DataLeague — Transferencias");
		stage.setScene(scene);
		stage.show();
	}
 
	/**
	 * RF-046/RF-049: muestra las transferencias PENDIENTES donde el club destino
	 * es el del secretario logueado, con botones para Aceptar/Rechazar cada una.
	 * Si el usuario no es Secretario, o no tiene pendientes, no se muestra nada.
	 */
	private VBox construirSeccionPendientes()
	{
		if(!(usuario instanceof Secretario))
		{
			return new VBox();
		}
 
		Secretario secretario = (Secretario) usuario;
		List<Tranferencias> pendientes = transferenciaControlador.listarTransferenciasPendientesPorClubDestino(secretario.getIdClub());
 
		if(pendientes.isEmpty())
		{
			return new VBox();
		}
 
		Label lblPendientes = new Label("Transferencias pendientes de tu respuesta");
		lblPendientes.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #c0392b;");
 
		VBox tarjetas = new VBox(8);
 
		for(Tranferencias pendiente : pendientes)
		{
			tarjetas.getChildren().add(crearTarjetaPendiente(pendiente));
		}
 
		VBox seccion = new VBox(8, lblPendientes, tarjetas);
		return seccion;
	}
 
	private HBox crearTarjetaPendiente(Tranferencias transferencia)
	{
		String nombreJugador = transferencia.getJugador() != null
				? transferencia.getJugador().getNombre() + " " + transferencia.getJugador().getApellido() : "";
		String clubOrigen = transferencia.getClubOrigen() != null ? transferencia.getClubOrigen().getNombre() : "";
 
		Label lblInfo = new Label(nombreJugador + " — proviene de " + clubOrigen);
		lblInfo.setStyle("-fx-font-size: 13px;");
		HBox.setHgrow(lblInfo, Priority.ALWAYS);
 
		Button btnAceptar = new Button("Aceptar");
		btnAceptar.setStyle(
				"-fx-background-color: #27ae60; -fx-text-fill: white; " +
				"-fx-font-size: 12px; -fx-cursor: hand; -fx-background-radius: 4px;"
		);
		btnAceptar.setOnAction(e -> responder(transferencia, true));
 
		Button btnRechazar = new Button("Rechazar");
		btnRechazar.setStyle(
				"-fx-background-color: #c0392b; -fx-text-fill: white; " +
				"-fx-font-size: 12px; -fx-cursor: hand; -fx-background-radius: 4px;"
		);
		btnRechazar.setOnAction(e -> responder(transferencia, false));
 
		HBox tarjeta = new HBox(12, lblInfo, btnAceptar, btnRechazar);
		tarjeta.setPadding(new Insets(10));
		tarjeta.setStyle(
				"-fx-background-color: white; -fx-background-radius: 8px; " +
				"-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2);"
		);
		tarjeta.setStyle(tarjeta.getStyle() + " -fx-alignment: CENTER-LEFT;");
 
		return tarjeta;
	}
 
	/**
	 * RF-049: aceptar o rechazar la transferencia pendiente. Si se acepta,
	 * TransferenciaControlador ya se encarga de actualizar el club del jugador.
	 */
	private void responder(Tranferencias transferencia, boolean aceptar)
	{
		String error = transferenciaControlador.responderTransferencia(usuario, transferencia, aceptar);
 
		if(error != null)
		{
			mostrarAlerta(AlertType.ERROR, "No se pudo registrar la respuesta", error);
		}
		else
		{
			mostrarAlerta(AlertType.INFORMATION, "Listo", aceptar ? "Transferencia aceptada" : "Transferencia rechazada");
			// Se refresca toda la pantalla para que la tarjeta respondida desaparezca de pendientes
			// y la tabla general muestre el nuevo estado
			Stage stage = (Stage) tabla.getScene().getWindow();
			mostrar(stage);
		}
	}
 
	/**
	 * RN-013 confirmada antes: el secretario solo puede transferir jugadores de SU PROPIO
	 * club (clubOrigen = club del secretario logueado, no se elige). Solo se eligen
	 * el jugador (filtrado a los de ese club) y el club destino.
	 */
	private void abrirFormularioNuevaTransferencia()
	{
		if(!(usuario instanceof Secretario))
		{
			mostrarAlerta(AlertType.ERROR, "No autorizado", "Solo un Secretario puede solicitar una transferencia");
			return;
		}
 
		Secretario secretario = (Secretario) usuario;
 
		Dialog<Void> dialogo = new Dialog<>();
		dialogo.setTitle("Nueva Transferencia");
		dialogo.setHeaderText("Transferir un jugador de " + secretario.getNombreClub() + " a otro club");
 
		ComboBox<Jugador> comboJugador = new ComboBox<>();
		List<Jugador> jugadoresDelClub = jugadorControlador.listarJugadoresPorClub(secretario.getIdClub());
		comboJugador.setItems(FXCollections.observableArrayList(jugadoresDelClub));
		comboJugador.setConverter(convertidorJugador());
		comboJugador.setPromptText("Seleccionar jugador");
 
		ComboBox<Clubes> comboClubDestino = new ComboBox<>();
		List<Clubes> todosLosClubes = clubControlador.listarClubes();
		List<Clubes> clubesDestinoPosibles = todosLosClubes.stream()
				.filter(club -> club.getId() != secretario.getIdClub())
				.toList();
		comboClubDestino.setItems(FXCollections.observableArrayList(clubesDestinoPosibles));
		comboClubDestino.setConverter(convertidorClub());
		comboClubDestino.setPromptText("Seleccionar club destino");
 
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20));
		grid.addRow(0, new Label("Jugador:"), comboJugador);
		grid.addRow(1, new Label("Club destino:"), comboClubDestino);
 
		dialogo.getDialogPane().setContent(grid);
		dialogo.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
 
		dialogo.setResultConverter(boton ->
		{
			if(boton == ButtonType.OK)
			{
				confirmarNuevaTransferencia(secretario, comboJugador.getValue(), comboClubDestino.getValue());
			}
			return null;
		});
 
		dialogo.showAndWait();
	}
 
	private void confirmarNuevaTransferencia(Secretario secretario, Jugador jugador, Clubes clubDestino)
	{
		if(jugador == null || clubDestino == null)
		{
			mostrarAlerta(AlertType.WARNING, "Datos incompletos", "Debe seleccionar un jugador y un club destino");
			return;
		}
 
		Tranferencias transferencia = new Tranferencias();
		transferencia.setJugador(jugador);
		transferencia.setClubOrigen(clubControlador.buscarClub(secretario.getIdClub()));
		transferencia.setClubDestino(clubDestino);
		transferencia.setFechaSolicitud(LocalDate.now());
		transferencia.setEstado("Pendiente");
 
		String error = transferenciaControlador.solicitarTransferencia(usuario, transferencia);
 
		if(error != null)
		{
			mostrarAlerta(AlertType.ERROR, "No se pudo crear la transferencia", error);
		}
		else
		{
			mostrarAlerta(AlertType.INFORMATION, "Listo", "Solicitud de transferencia creada correctamente");
			tabla.setItems(FXCollections.observableArrayList(transferenciaControlador.listarTransferencias()));
		}
	}
 
	private StringConverter<Jugador> convertidorJugador()
	{
		return new StringConverter<Jugador>()
		{
			@Override
			public String toString(Jugador jugador)
			{
				return jugador == null ? "" : jugador.getNombre() + " " + jugador.getApellido() + " (DNI " + jugador.getDni() + ")";
			}
 
			@Override
			public Jugador fromString(String texto)
			{
				return null; // no se necesita conversion inversa, el ComboBox no es editable
			}
		};
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
 
	private TableView<Tranferencias> construirTabla()
	{
		TableView<Tranferencias> tabla = new TableView<>();
 
		TableColumn<Tranferencias, String> colJugador = new TableColumn<>("Jugador");
		colJugador.setCellValueFactory(fila -> new SimpleStringProperty(
				fila.getValue().getJugador() != null
						? fila.getValue().getJugador().getNombre() + " " + fila.getValue().getJugador().getApellido()
						: ""));
 
		TableColumn<Tranferencias, String> colOrigen = new TableColumn<>("Club Origen");
		colOrigen.setCellValueFactory(fila -> new SimpleStringProperty(
				fila.getValue().getClubOrigen() != null ? fila.getValue().getClubOrigen().getNombre() : ""));
 
		TableColumn<Tranferencias, String> colDestino = new TableColumn<>("Club Destino");
		colDestino.setCellValueFactory(fila -> new SimpleStringProperty(
				fila.getValue().getClubDestino() != null ? fila.getValue().getClubDestino().getNombre() : ""));
 
		TableColumn<Tranferencias, String> colFecha = new TableColumn<>("Fecha Solicitud");
		colFecha.setCellValueFactory(fila -> new SimpleStringProperty(
				fila.getValue().getFechaSolicitud() != null ? fila.getValue().getFechaSolicitud().toString() : ""));
 
		TableColumn<Tranferencias, String> colEstado = new TableColumn<>("Estado");
		colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
 
		tabla.getColumns().addAll(colJugador, colOrigen, colDestino, colFecha, colEstado);
 
		List<Tranferencias> transferencias = transferenciaControlador.listarTransferencias();
		tabla.setItems(FXCollections.observableArrayList(transferencias));
 
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
