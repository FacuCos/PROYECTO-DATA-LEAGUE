package Controladores;

import java.time.LocalDate;
import java.util.List;
 
import ClasesObjetos.Tranferencias;
import ClasesObjetos.Usuario;
import ConsultasBD.TransferenciaBD;
import Validaciones.ValidacionPermisos;
import Validaciones.ValidacionPermisos.Accion;
import Validaciones.ValidacionPermisos.Entidad;
import Validaciones.ValidacionTransferencia;

public class TransferenciaControlador {
	
	private TransferenciaBD transferenciaBD = new TransferenciaBD();
	private ValidacionTransferencia validacionTransferencia = new ValidacionTransferencia();
	private ValidacionPermisos validacionPermisos = new ValidacionPermisos();
 
	/**
	 * RF-023 + RF-034: crea una solicitud de traspaso, validando que la fecha de
	 * solicitud este dentro de un periodo habilitado (invierno o verano).
	 */
	public String solicitarTransferencia(Usuario usuarioLogueado, Tranferencias transferencia)
	{
		String errorPermiso = validacionPermisos.validarPermiso(
				validacionPermisos.obtenerRol(usuarioLogueado), Entidad.TRANSFERENCIA, Accion.CREAR);
		if(errorPermiso != null)
		{
			return errorPermiso;
		}
 
		String errorPeriodo = validacionTransferencia.validarPeriodoHabilitado(transferencia.getFechaSolicitud());
		if(errorPeriodo != null)
		{
			return errorPeriodo;
		}
 
		int idGenerado = transferenciaBD.insertarTransferencia(transferencia);
		if(idGenerado == -1)
		{
			return "No se pudo registrar la solicitud de transferencia";
		}
 
		return null;
	}
 
	/**
	 * RF-045 + RF-049: el secretario receptor acepta o rechaza la transferencia,
	 * validando que no haya vencido el plazo de 72 horas habiles. Si se acepta,
	 * el jugador pasa a pertenecer al club destino (se actualiza via JugadorControlador).
	 */
	public String responderTransferencia(Usuario usuarioLogueado, Tranferencias transferencia, boolean aceptar)
	{
		String errorPlazo = validacionTransferencia.validarPlazoRespuesta(transferencia);
		if(errorPlazo != null)
		{
			return errorPlazo;
		}
 
		String nuevoEstado = aceptar ? "Aceptada" : "Rechazada";
		boolean exito = transferenciaBD.actualizarEstadoTransferencia(
				transferencia.getIdTransferencia(), nuevoEstado, LocalDate.now());
 
		if(!exito)
		{
			return "No se pudo registrar la respuesta a la transferencia";
		}
 
		if(aceptar)
		{
			// RF-049: el jugador pasa a pertenecer al club destino.
			// Se obtiene el jugador completo (con su categoria actual, que no cambia),
			// se le actualiza SOLO el club, y se guarda una unica vez via JugadorControlador.
			JugadorControlador jugadorControlador = new JugadorControlador();
			ClasesObjetos.Jugador jugadorCompleto = jugadorControlador.buscarJugador(transferencia.getJugador().getIdJugador());
 
			if(jugadorCompleto != null)
			{
				jugadorCompleto.setClub(transferencia.getClubDestino());
				String errorJugador = jugadorControlador.modificarJugador(jugadorCompleto, jugadorCompleto.getCategoria());
 
				if(errorJugador != null)
				{
					return "La transferencia se registró, pero no se pudo actualizar el club del jugador: " + errorJugador;
				}
			}
		}
 
		return null;
	}
 
	public Tranferencias buscarTransferencia(int idTransferencia)
	{
		return transferenciaBD.buscarTransferenciaPorId(idTransferencia);
	}
 
	public List<Tranferencias> listarTransferencias()
	{
		return transferenciaBD.listarTransferencias();
	}
 
	public List<Tranferencias> listarTransferenciasPendientesPorClubDestino(int idClubDestino)
	{
		return transferenciaBD.listarTransferenciasPendientesPorClubDestino(idClubDestino);
	}

}
