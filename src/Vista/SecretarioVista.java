package Vista;

import java.util.List;

import ClasesObjetos.Partidos;
import ClasesObjetos.Secretario;
import ClasesObjetos.Usuario;
import Controladores.PartidoControlador;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SecretarioVista {
	
	private Usuario usuario;
	private PartidoControlador partidoControlador = new PartidoControlador();
 
	public SecretarioVista(Usuario usuario) {
		this.usuario = usuario;
	}
 
	public void mostrar(Stage stage) {
 
		// ── SIDEBAR (compartido con las demas vistas) ──
		VBox sidebar = ConstructorBarras.construir(usuario, stage);
 
		// ── CONTENIDO PRINCIPAL ──────────────────────────────
		Label lblTitulo = new Label("Próximos Partidos");
		lblTitulo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #1a1a2e;");
 
		HBox tarjetas = new HBox(16);
		tarjetas.setPadding(new Insets(16, 0, 0, 0));
 
		List<Partidos> proximos = obtenerProximosPartidosDelClub();
 
		if(proximos.isEmpty())
		{
			Label lblVacio = new Label("No hay partidos próximos programados para tu club");
			lblVacio.setStyle("-fx-font-size: 13px; -fx-text-fill: #888;");
			tarjetas.getChildren().add(lblVacio);
		}
		else
		{
			for(Partidos partido : proximos)
			{
				tarjetas.getChildren().add(crearTarjetaPartido(partido));
			}
		}
 
		VBox contenido = new VBox(16, lblTitulo, tarjetas);
		contenido.setPadding(new Insets(24));
		contenido.setStyle("-fx-background-color: #f0f2f5;");
		VBox.setVgrow(tarjetas, Priority.ALWAYS);
 
		// ── LAYOUT PRINCIPAL ─────────────────────────────────
		HBox root = new HBox(sidebar, contenido);
		HBox.setHgrow(contenido, Priority.ALWAYS);
 
		Scene scene = new Scene(root, 900, 560);
 
		// Icono del software
		try {
			stage.getIcons().add(new Image("file:imagenes/icono.png"));
		} catch (Exception e) {
			System.out.println("Icono no encontrado");
		}
 
		stage.setTitle("DataLeague — Secretario");
		stage.setScene(scene);
		stage.setResizable(true);
		stage.show();
	}
 
	/**
	 * RF-029: partidos del club del secretario logueado, filtrados a fecha >= hoy.
	 * Si el usuario no es Secretario (no deberia pasar en esta Vista, pero por las dudas),
	 * devuelve lista vacia en vez de fallar.
	 */
	private List<Partidos> obtenerProximosPartidosDelClub()
	{
		if(!(usuario instanceof Secretario))
		{
			return List.of();
		}
 
		Secretario secretario = (Secretario) usuario;
		java.time.LocalDate hoy = java.time.LocalDate.now();
 
		List<Partidos> partidosDelClub = partidoControlador.listarPartidosPorClub(secretario.getIdClub());
 
		return partidosDelClub.stream()
				.filter(p -> p.getFechaPartido() != null && !p.getFechaPartido().isBefore(hoy))
				.sorted((p1, p2) -> p1.getFechaPartido().compareTo(p2.getFechaPartido()))
				.toList();
	}
 
	private VBox crearTarjetaPartido(Partidos partido) {
		VBox tarjeta = new VBox(10);
		tarjeta.setPrefWidth(180);
		tarjeta.setPrefHeight(280);
		tarjeta.setPadding(new Insets(14));
		tarjeta.setStyle(
			"-fx-background-color: white; " +
			"-fx-background-radius: 10px; " +
			"-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 3);"
		);
 
		String division = partido.getCategoriaLocal() != null ? partido.getCategoriaLocal().getDivision() : "";
		Label lblDivision = new Label(division);
		lblDivision.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #1a1a2e;");
 
		Separator sep = new Separator();
 
		String nombreLocal = partido.getClubLocal() != null ? partido.getClubLocal().getNombre() : "—";
		Label lblLocal = new Label(nombreLocal);
		lblLocal.setStyle("-fx-font-size: 12px; -fx-text-fill: #333;");
		lblLocal.setWrapText(true);
 
		Label lblVs = new Label("vs");
		lblVs.setStyle("-fx-font-size: 11px; -fx-text-fill: #999;");
 
		String nombreVisitante = partido.getClubVisitante() != null ? partido.getClubVisitante().getNombre() : "—";
		Label lblVisitante = new Label(nombreVisitante);
		lblVisitante.setStyle("-fx-font-size: 12px; -fx-text-fill: #333;");
		lblVisitante.setWrapText(true);
 
		String textoFecha = partido.getFechaPartido() != null ? partido.getFechaPartido().toString() : "--/--/----";
		Label lblFecha = new Label("Fecha: " + textoFecha);
		lblFecha.setStyle("-fx-font-size: 11px; -fx-text-fill: #888;");
 
		tarjeta.getChildren().addAll(lblDivision, sep, lblLocal, lblVs, lblVisitante, lblFecha);
		return tarjeta;
	}

}
