package Vista;

import ClasesObjetos.Usuario;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AdministradorVista {

	private Usuario usuario;
	 
	public AdministradorVista(Usuario usuario)
	{
		this.usuario = usuario;
	}
 
	public void mostrar(Stage stage)
	{
		VBox sidebar = ConstructorBarras.construir(usuario, stage);
 
		Label lblTitulo = new Label("Panel de Administración");
		lblTitulo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #1a1a2e;");
 
		HBox tarjetas = new HBox(16);
		tarjetas.setPadding(new Insets(16, 0, 0, 0));
 
		tarjetas.getChildren().addAll(
				crearTarjetaAcceso("Usuarios", "Gestionar Secretarios y Árbitros", () -> new UsuariosVista(usuario).mostrar(stage)),
				crearTarjetaAcceso("Clubes", "Alta y baja de clubes", () -> new ClubVista(usuario).mostrar(stage)),
				crearTarjetaAcceso("Categorías", "Crear categorías por club y rango de edad", () -> new CategoriaVista(usuario).mostrar(stage)),
				crearTarjetaAcceso("Jugadores", "Ver todos los jugadores registrados", () -> new JugadoresVista(usuario).mostrar(stage))
		);
 
		VBox contenido = new VBox(16, lblTitulo, tarjetas);
		contenido.setPadding(new Insets(24));
		contenido.setStyle("-fx-background-color: #f0f2f5;");
		VBox.setVgrow(tarjetas, Priority.ALWAYS);
 
		HBox root = new HBox(sidebar, contenido);
		HBox.setHgrow(contenido, Priority.ALWAYS);
 
		Scene scene = new Scene(root, 900, 560);
 
		stage.setTitle("DataLeague — Administrador");
		stage.setScene(scene);
		stage.setResizable(true);
		stage.show();
	}
 
	@SuppressWarnings("unused")
	private VBox crearTarjetaAcceso(String titulo, String descripcion, Runnable accion)
	{
		VBox tarjeta = new VBox(10);
		tarjeta.setPrefWidth(200);
		tarjeta.setPrefHeight(160);
		tarjeta.setPadding(new Insets(16));
		tarjeta.setStyle(
				"-fx-background-color: white; " +
				"-fx-background-radius: 10px; " +
				"-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 3);"
		);
 
		Label lblTitulo = new Label(titulo);
		lblTitulo.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #1a1a2e;");
 
		Label lblDescripcion = new Label(descripcion);
		lblDescripcion.setStyle("-fx-font-size: 11px; -fx-text-fill: #666;");
		lblDescripcion.setWrapText(true);
 
		Button btnAcceder = new Button("Acceder");
		btnAcceder.setStyle(
				"-fx-background-color: #1a1a2e; -fx-text-fill: white; " +
				"-fx-font-size: 12px; -fx-cursor: hand; -fx-background-radius: 6px;"
		);
		btnAcceder.setOnAction(e -> accion.run());
 
		tarjeta.getChildren().addAll(lblTitulo, lblDescripcion, btnAcceder);
		return tarjeta;
	}
}
