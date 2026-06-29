package Vista;

import java.util.List;

import ClasesObjetos.Partidos;
import ClasesObjetos.Usuario;
import Controladores.LoginControlador;
import Controladores.PartidoControlador;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginVista extends Application{


	private LoginControlador controlador = new LoginControlador();
	private PartidoControlador partidoControlador = new PartidoControlador();
 
    public void start(Stage stage) {
        // ── PANEL IZQUIERDO: LOGIN ──────────────────────────────
        VBox panelLogin = construirPanelLogin(stage);
 
        // ── PANEL DERECHO: PROXIMOS PARTIDOS ────────────────────
        VBox panelPartidos = construirPanelProximosPartidos(stage);
 
        // ── LAYOUT PRINCIPAL: dos paneles lado a lado ───────────
        HBox root = new HBox(panelLogin, panelPartidos);
        HBox.setHgrow(panelLogin, Priority.NEVER);
        HBox.setHgrow(panelPartidos, Priority.ALWAYS);
 
        Scene scene = new Scene(root, 1100, 650);
        stage.setTitle("DataLeague — Login");
        try {
            stage.getIcons().add(new Image("file:Imagenes/softwareImagen.png"));
        } catch (Exception e) {
            // sin icono
        }
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
    }
 
    private VBox construirPanelLogin(Stage stage)
    {
        // TITULO
        Text titulo = new Text("DataLeague");
        titulo.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-fill: #1a1a2e;");
        Text subtitulo = new Text("Sistema de Gestión de Liga de Fútbol");
        subtitulo.setStyle("-fx-font-size: 13px; -fx-fill: #666;");
 
        // CAMPOS
        Label lblDni = new Label("DNI");
        lblDni.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");
        TextField campoDni = new TextField();
        campoDni.setPromptText("Ingresá tu DNI");
        campoDni.setStyle("-fx-pref-height: 40px; -fx-font-size: 14px;");
 
        Label lblContraseña = new Label("Contraseña");
        lblContraseña.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");
        PasswordField campoContraseña = new PasswordField();
        campoContraseña.setPromptText("Ingresá tu contraseña");
        campoContraseña.setStyle("-fx-pref-height: 40px; -fx-font-size: 14px;");
 
        // MENSAJE DE ERROR
        Label lblError = new Label("");
        lblError.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
 
        // BOTON LOGIN
        Button btnLogin = new Button("Ingresar");
        btnLogin.setMaxWidth(Double.MAX_VALUE);
        btnLogin.setStyle(
            "-fx-background-color: #1a1a2e; -fx-text-fill: white; " +
            "-fx-font-size: 15px; -fx-font-weight: bold; " +
            "-fx-pref-height: 45px; " +
            "-fx-cursor: hand; -fx-background-radius: 6px;"
        );
        btnLogin.setOnAction(e -> {
            String dni = campoDni.getText().trim();
            String contraseña = campoContraseña.getText();
            Usuario usuario = controlador.iniciarSesion(dni, contraseña);
            if (usuario == null) {
                lblError.setText("DNI o contraseña incorrectos.");
            } else {
                lblError.setText("");
                if (usuario instanceof ClasesObjetos.Administrador) {
                    new Vista.AdministradorVista(usuario).mostrar(stage);
                } else if (usuario instanceof ClasesObjetos.Arbitro) {
                    new Vista.ArbitroVista(usuario).mostrar(stage);
                } else {
                    SecretarioVista vista = new SecretarioVista(usuario);
                    vista.mostrar(stage);
                }
            }
        });
 
        // BOTON INVITADO
        Button btnInvitado = new Button("Ingresar como invitado");
        btnInvitado.setMaxWidth(Double.MAX_VALUE);
        btnInvitado.setStyle(
            "-fx-background-color: transparent; -fx-text-fill: #1a1a2e; " +
            "-fx-font-size: 13px; -fx-underline: true; " +
            "-fx-cursor: hand; -fx-pref-height: 35px;"
        );
        btnInvitado.setOnAction(e -> new InvitadoVista().mostrar(stage));
 
        // LAYOUT
        VBox formulario = new VBox(10);
        formulario.getChildren().addAll(
            lblDni, campoDni,
            lblContraseña, campoContraseña,
            lblError,
            btnLogin,
            btnInvitado
        );
        formulario.setAlignment(Pos.CENTER_LEFT);
        formulario.setPadding(new Insets(30));
        formulario.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 12px; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 20, 0,0, 4);"
        );
        formulario.setMaxWidth(360);
 
        VBox encabezado = new VBox(6, titulo, subtitulo);
        encabezado.setAlignment(Pos.CENTER);
 
        VBox contenido = new VBox(24, encabezado, formulario);
        contenido.setAlignment(Pos.CENTER);
        contenido.setPadding(new Insets(40));
 
        VBox panel = new VBox(contenido);
        panel.setAlignment(Pos.CENTER);
        panel.setPrefWidth(480);
        panel.setMinWidth(420);
        panel.setStyle("-fx-background-color: #f0f2f5;");
 
        return panel;
    }
 
    /**
     * RF-029: panel publico de "Proximos Partidos", visible sin necesidad de loguearse.
     */
    private VBox construirPanelProximosPartidos(Stage stage)
    {
        Label lblTitulo = new Label("Próximos Partidos");
        lblTitulo.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: white;");
 
        VBox listaPartidos = new VBox(12);
        listaPartidos.setPadding(new Insets(16, 0, 0, 0));
 
        List<Partidos> proximos = partidoControlador.listarProximosPartidos();
 
        if(proximos.isEmpty())
        {
            Label lblVacio = new Label("No hay partidos programados por el momento");
            lblVacio.setStyle("-fx-text-fill: #ccc; -fx-font-size: 13px;");
            listaPartidos.getChildren().add(lblVacio);
        }
        else
        {
            for(Partidos partido : proximos)
            {
                listaPartidos.getChildren().add(crearTarjetaPartido(partido));
            }
        }
 
        ScrollPane scroll = new ScrollPane(listaPartidos);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        VBox.setVgrow(scroll, Priority.ALWAYS);
 
        VBox panel = new VBox(16, lblTitulo, scroll);
        panel.setPadding(new Insets(40));
        panel.setStyle("-fx-background-color: #1a1a2e;");
 
        return panel;
    }
 
    private VBox crearTarjetaPartido(Partidos partido)
    {
        String nombreLocal = partido.getClubLocal() != null ? partido.getClubLocal().getNombre() : "—";
        String nombreVisitante = partido.getClubVisitante() != null ? partido.getClubVisitante().getNombre() : "—";
        String division = partido.getCategoriaLocal() != null ? partido.getCategoriaLocal().getDivision() : "";
 
        Label lblDivision = new Label(division);
        lblDivision.setStyle("-fx-font-size: 11px; -fx-text-fill: #aaa; -fx-font-weight: bold;");
 
        Label lblEnfrentamiento = new Label(nombreLocal + "  vs  " + nombreVisitante);
        lblEnfrentamiento.setStyle("-fx-font-size: 14px; -fx-text-fill: white; -fx-font-weight: bold;");
 
        Label lblFecha = new Label(partido.getFechaPartido() != null ? partido.getFechaPartido().toString() : "");
        lblFecha.setStyle("-fx-font-size: 12px; -fx-text-fill: #ccc;");
 
        VBox tarjeta = new VBox(4, lblDivision, lblEnfrentamiento, lblFecha);
        tarjeta.setPadding(new Insets(14));
        tarjeta.setStyle(
                "-fx-background-color: #2a2a4e; " +
                "-fx-background-radius: 8px;"
        );
 
        return tarjeta;
    }
}


