package Vista;

import java.util.List;
 
import ClasesObjetos.Arbitro;
import ClasesObjetos.Clubes;
import ClasesObjetos.Secretario;
import ClasesObjetos.Usuario;
import Controladores.ClubControlador;
import Controladores.UsuarioControlador;
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class UsuariosVista {

	private Usuario usuario;
	private UsuarioControlador usuarioControlador = new UsuarioControlador();
	private ClubControlador clubControlador = new ClubControlador();
	private TableView<Usuario> tabla;
 
	public UsuariosVista(Usuario usuario)
	{
		this.usuario = usuario;
	}
 
	public void mostrar(Stage stage)
	{
		VBox sidebar = ConstructorBarras.construir(usuario, stage);
 
		Label lblTitulo = new Label("Usuarios (Secretarios y Árbitros)");
		lblTitulo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #1a1a2e;");
 
		Button btnNuevo = new Button("+ Nuevo Usuario");
		btnNuevo.setStyle(
				"-fx-background-color: #1a1a2e; -fx-text-fill: white; " +
				"-fx-font-size: 13px; -fx-cursor: hand; -fx-background-radius: 6px; -fx-padding: 8 16 8 16;"
		);
		btnNuevo.setOnAction(e -> abrirFormularioNuevoUsuario());
 
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
 
		Scene scene = new Scene(root, 900, 560);
 
		stage.setTitle("DataLeague — Usuarios");
		stage.setScene(scene);
		stage.show();
	}
 
	/**
	 * RF-035/036/037: el Administrador crea un Secretario (con club asignado, validando
	 * el limite de 3 por club) o un Arbitro (sin club). El RadioButton elegido determina
	 * que campos son relevantes; el combo de Club se habilita solo para Secretario.
	 */
	private void abrirFormularioNuevoUsuario()
	{
		Dialog<Void> dialogo = new Dialog<>();
		dialogo.setTitle("Nuevo Usuario");
		dialogo.setHeaderText("Registrar un nuevo Secretario o Árbitro");
 
		ToggleGroup grupoTipo = new ToggleGroup();
		RadioButton radioSecretario = new RadioButton("Secretario");
		radioSecretario.setToggleGroup(grupoTipo);
		radioSecretario.setSelected(true);
		RadioButton radioArbitro = new RadioButton("Árbitro");
		radioArbitro.setToggleGroup(grupoTipo);
 
		HBox filaTipo = new HBox(16, radioSecretario, radioArbitro);
 
		TextField campoNombre = new TextField();
		campoNombre.setPromptText("Nombre");
 
		TextField campoApellido = new TextField();
		campoApellido.setPromptText("Apellido");
 
		TextField campoDni = new TextField();
		campoDni.setPromptText("DNI");
 
		TextField campoTelefono = new TextField();
		campoTelefono.setPromptText("Teléfono");
 
		TextField campoGmail = new TextField();
		campoGmail.setPromptText("Gmail");
 
		PasswordField campoContraseña = new PasswordField();
		campoContraseña.setPromptText("Contraseña");
 
		ComboBox<Clubes> comboClub = new ComboBox<>(FXCollections.observableArrayList(clubControlador.listarClubes()));
		comboClub.setConverter(convertidorClub());
		comboClub.setPromptText("Club asignado");
 
		Label lblClub = new Label("Club:");
 
		// El combo de Club solo tiene sentido para Secretario; se deshabilita para Arbitro
		radioArbitro.selectedProperty().addListener((obs, antes, esArbitro) ->
		{
			comboClub.setDisable(esArbitro);
			if(esArbitro)
			{
				comboClub.setValue(null);
			}
		});
 
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20));
		grid.addRow(0, new Label("Tipo:"), filaTipo);
		grid.addRow(1, new Label("Nombre:"), campoNombre);
		grid.addRow(2, new Label("Apellido:"), campoApellido);
		grid.addRow(3, new Label("DNI:"), campoDni);
		grid.addRow(4, new Label("Teléfono:"), campoTelefono);
		grid.addRow(5, new Label("Gmail:"), campoGmail);
		grid.addRow(6, new Label("Contraseña:"), campoContraseña);
		grid.addRow(7, lblClub, comboClub);
 
		dialogo.getDialogPane().setContent(grid); // Nota: Si lanza error en "grido", cambialo a dialogo.
		dialogo.getDialogPane().setContent(grid);
		dialogo.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
 
		dialogo.setResultConverter(boton ->
		{
			if(boton == ButtonType.OK)
			{
				boolean esSecretario = radioSecretario.isSelected();
				confirmarNuevoUsuario(esSecretario, campoNombre.getText(), campoApellido.getText(),
						campoDni.getText(), campoTelefono.getText(), campoGmail.getText(),
						campoContraseña.getText(), comboClub.getValue());
			}
			return null;
		});
 
		dialogo.showAndWait();
	}
 
	private void confirmarNuevoUsuario(boolean esSecretario, String nombre, String apellido, String dni,
			String telefono, String gmail, String contraseña, Clubes club)
	{
		if(nombre.isBlank() || apellido.isBlank() || dni.isBlank() || contraseña.isBlank())
		{
			mostrarAlerta(AlertType.WARNING, "Datos incompletos", "Nombre, apellido, DNI y contraseña son obligatorios");
			return;
		}
 
		if(esSecretario && club == null)
		{
			mostrarAlerta(AlertType.WARNING, "Datos incompletos", "Debe seleccionar un club para el secretario");
			return;
		}
 
		String error;
 
		if(esSecretario)
		{
			Secretario secretario = new Secretario();
			secretario.setNombre(nombre);
			secretario.setApellido(apellido);
			secretario.setDni(dni);
			secretario.setTelefono(telefono);
			secretario.setGmail(gmail);
			secretario.setConstraseña(contraseña); 
			secretario.setIdClub(club.getId());
			secretario.setNombreClub(club.getNombre());
			secretario.setEscudoRuta(club.getEscudoRuta());
 
			error = usuarioControlador.crearSecretario(usuario, secretario);
		}
		else
		{
			Arbitro arbitro = new Arbitro();
			arbitro.setNombre(nombre);
			arbitro.setApellido(apellido);
			arbitro.setDni(dni);
			arbitro.setTelefono(telefono);
			arbitro.setGmail(gmail);
			arbitro.setConstraseña(contraseña);
 
			error = usuarioControlador.crearArbitro(usuario, arbitro);
		}
 
		if(error != null)
		{
			mostrarAlerta(AlertType.ERROR, "No se pudo registrar el usuario", error);
		}
		else
		{
			mostrarAlerta(AlertType.INFORMATION, "Listo", "Usuario registrado correctamente");
			tabla.setItems(FXCollections.observableArrayList(usuarioControlador.listarUsuarios()));
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
 
	private void confirmarEliminarUsuario(Usuario usuarioAEliminar)
	{
	    Alert confirmacion = new Alert(AlertType.CONFIRMATION);
	    confirmacion.setTitle("Confirmar eliminación");
	    confirmacion.setHeaderText(null);
	    confirmacion.setContentText("¿Está seguro que desea eliminar a "
	            + usuarioAEliminar.getNombre() + " " + usuarioAEliminar.getApellido() + "?");

	    confirmacion.showAndWait().ifPresent(respuesta ->
	    {
	        if(respuesta == ButtonType.OK)
	        {
	            String error = usuarioControlador.eliminarUsuario(usuario, usuarioAEliminar.getId());

	            if(error != null)
	            {
	                mostrarAlerta(AlertType.ERROR, "No se pudo eliminar", error);
	            }
	            else
	            {
	                mostrarAlerta(AlertType.INFORMATION, "Listo", "Usuario eliminado correctamente");
	                tabla.setItems(FXCollections.observableArrayList(usuarioControlador.listarUsuarios()));
	            }
	        }
	    });
	}
	
	private TableView<Usuario> construirTabla()
	{
		TableView<Usuario> tabla = new TableView<>();
 
		TableColumn<Usuario, String> colNombre = new TableColumn<>("Nombre");
		colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
 
		TableColumn<Usuario, String> colApellido = new TableColumn<>("Apellido");
		colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
 
		TableColumn<Usuario, String> colDni = new TableColumn<>("DNI");
		colDni.setCellValueFactory(new PropertyValueFactory<>("dni"));
 
		TableColumn<Usuario, String> colRol = new TableColumn<>("Rol");
		colRol.setCellValueFactory(fila -> new SimpleStringProperty(fila.getValue().getRol()));
 
		TableColumn<Usuario, String> colClub = new TableColumn<>("Club");
		colClub.setCellValueFactory(fila -> new SimpleStringProperty(
				fila.getValue() instanceof Secretario ? ((Secretario) fila.getValue()).getNombreClub() : ""));
 
		// --- COLACCION INTEGRADA CORRECTAMENTE ADENTRO DEL MÉTODO ---
		TableColumn<Usuario, Void> colAccion = new TableColumn<>("");
		colAccion.setCellFactory(columna -> new javafx.scene.control.TableCell<Usuario, Void>()
		{
			private final Button btnEliminar = new Button("Eliminar");

			{
				btnEliminar.setStyle(
						"-fx-background-color: #c0392b; -fx-text-fill: white; " +
						"-fx-font-size: 11px; -fx-cursor: hand; -fx-background-radius: 4px;"
				);
				btnEliminar.setOnAction(e ->
				{
					Usuario usuarioDeLaFila = getTableRow().getItem();
					if(usuarioDeLaFila != null)
					{
						confirmarEliminarUsuario(usuarioDeLaFila);
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

				Usuario usuarioDeLaFila = getTableRow().getItem();
				setGraphic(usuarioDeLaFila.getId() == usuario.getId() ? null : btnEliminar);
			}
		});
 
		tabla.getColumns().addAll(colNombre, colApellido, colDni, colRol, colClub, colAccion);
 
		List<Usuario> usuarios = usuarioControlador.listarUsuarios();
		tabla.setItems(FXCollections.observableArrayList(usuarios));
 
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