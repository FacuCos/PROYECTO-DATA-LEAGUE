package Vista;

import ClasesObjetos.Administrador;
import ClasesObjetos.Secretario;
import ClasesObjetos.Usuario;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
public class ConstructorBarras {
	

	public static VBox construir(Usuario usuario, Stage stage)
	{
		VBox sidebar = new VBox(16);
		sidebar.setPrefWidth(200);
		sidebar.setPadding(new Insets(20));
		sidebar.setStyle("-fx-background-color: #1a1a2e;");
 
		Label lblBienvenido = new Label("Bienvenido:");
		lblBienvenido.setStyle("-fx-text-fill: #aaa; -fx-font-size: 12px;");
		Label lblNombre = new Label(usuario.getNombre() + " " + usuario.getApellido());
		lblNombre.setStyle("-fx-text-fill: white; -fx-font-size: 13px; -fx-font-weight: bold;");
		lblNombre.setWrapText(true);
 
		Label lblRol = new Label(usuario.getRol());
		lblRol.setStyle("-fx-text-fill: #aaa; -fx-font-size: 11px;");
 
		Separator sep1 = new Separator();
 
		Button btnInicio = crearBotonMenu("⌂ Inicio");
		btnInicio.setOnAction(e -> irAInicioSegunRol(usuario, stage));
 
		Button btnJugadores = crearBotonMenu("+ Jugadores");
		btnJugadores.setOnAction(e -> new JugadoresVista(usuario).mostrar(stage));
 
		Button btnClub = crearBotonMenu("+ Club");
		btnClub.setOnAction(e -> new ClubVista(usuario).mostrar(stage));
 
		Button btnCategoria = crearBotonMenu("+ Categorías");
		btnCategoria.setOnAction(e -> new CategoriaVista(usuario).mostrar(stage));
 
		Region spacer = new Region();
		VBox.setVgrow(spacer, Priority.ALWAYS);
 
		Separator sep2 = new Separator();
 
		Button btnCerrar = new Button("- Cerrar Sesión");
		btnCerrar.setStyle(
				"-fx-background-color: transparent; -fx-text-fill: #ff6b6b; " +
				"-fx-font-size: 13px; -fx-cursor: hand; -fx-padding: 8 0 8 0;"
		);
		btnCerrar.setOnAction(e -> stage.close());
 
		sidebar.getChildren().addAll(lblBienvenido, lblNombre, lblRol);
 
		// El bloque de escudo/club SOLO aplica a Secretario (tiene club asociado)
		if(usuario instanceof Secretario)
		{
			ImageView escudo = new ImageView();
			escudo.setFitWidth(70);
			escudo.setFitHeight(70);
			escudo.setPreserveRatio(true);
 
			String ruta = ((Secretario) usuario).getEscudoRuta();
			if(ruta != null && !ruta.isBlank())
			{
				try
				{
					escudo.setImage(new Image("file:" + ruta));
				}
				catch(Exception e)
				{
					// sin imagen
				}
			}
 
			String nombreClub = ((Secretario) usuario).getNombreClub() != null
					? ((Secretario) usuario).getNombreClub() : "Club";
			Label lblClub = new Label(nombreClub);
			lblClub.setStyle("-fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
 
			sidebar.getChildren().addAll(escudo, lblClub);
		}
 
		sidebar.getChildren().add(sep1);
		sidebar.getChildren().addAll(btnInicio, btnJugadores, btnClub, btnCategoria);
 
		// "+ Transferencia" solo aplica al Secretario (RF-034, "SC" en la matriz de permisos)
		if(usuario instanceof Secretario)
		{
			Button btnTransferencia = crearBotonMenu("+ Transferencia");
			btnTransferencia.setOnAction(e -> new TransferenciaVista(usuario).mostrar(stage));
			sidebar.getChildren().add(btnTransferencia);
			
			Button btnConvocatoria = crearBotonMenu("+ Convocatoria");
		    btnConvocatoria.setOnAction(e -> new ConvocatoriaVista(usuario).mostrar(stage));
		    sidebar.getChildren().add(btnConvocatoria);
		}
 
		// "+ Usuarios" solo aplica al Administrador (alta de SC/Arbitros, matriz de permisos)
		if(usuario instanceof Administrador)
		{
			Button btnUsuarios = crearBotonMenu("+ Usuarios");
			btnUsuarios.setOnAction(e -> new UsuariosVista(usuario).mostrar(stage));
			sidebar.getChildren().add(btnUsuarios);
			
		}
 
		sidebar.getChildren().addAll(spacer, sep2, btnCerrar);
 
		return sidebar;
	}
 
	/**
	 * Navega a la pantalla de inicio correspondiente segun el rol logueado.
	 * Hoy solo existe AdministradorVista/SecretarioVista; 
	 */
	private static void irAInicioSegunRol(Usuario usuario, Stage stage)
	{
		if(usuario instanceof Administrador)
		{
			new AdministradorVista(usuario).mostrar(stage);
		}
		else if(usuario instanceof ClasesObjetos.Arbitro)
		{
			new ArbitroVista(usuario).mostrar(stage);
		}
		else
		{
			new SecretarioVista(usuario).mostrar(stage);
		}
	}
 
	private static Button crearBotonMenu(String texto)
	{
		Button btn = new Button(texto);
		btn.setMaxWidth(Double.MAX_VALUE);
		btn.setStyle(
				"-fx-background-color: transparent; -fx-text-fill: white; " +
				"-fx-font-size: 13px; -fx-cursor: hand; " +
				"-fx-alignment: CENTER-LEFT; -fx-padding: 8 0 8 0;"
		);
		btn.setOnMouseEntered(e -> btn.setStyle(
				"-fx-background-color: #2a2a4e; -fx-text-fill: white; " +
				"-fx-font-size: 13px; -fx-cursor: hand; " +
				"-fx-alignment: CENTER-LEFT; -fx-padding: 8 0 8 0; " +
				"-fx-background-radius: 6px;"
		));
		btn.setOnMouseExited(e -> btn.setStyle(
				"-fx-background-color: transparent; -fx-text-fill: white; " +
				"-fx-font-size: 13px; -fx-cursor: hand; " +
				"-fx-alignment: CENTER-LEFT; -fx-padding: 8 0 8 0;"
		));
		return btn;
	}

}
