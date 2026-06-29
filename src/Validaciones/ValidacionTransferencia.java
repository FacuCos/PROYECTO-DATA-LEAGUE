package Validaciones;

import java.time.DayOfWeek;
import java.time.LocalDate;
import ClasesObjetos.Tranferencias;

public class ValidacionTransferencia {
	
	private ValidacionPeriodo validacionPeriodo = new ValidacionPeriodo();
	 
	/**
	 * RN-002/RN-003 + RF-023: solo se permite inscripcion, renovacion o transferencia
	 * de jugadores entre clubes durante los periodos de verano o invierno.
	 */
	public String validarPeriodoHabilitado(LocalDate fecha)
	{
		if(!validacionPeriodo.estaEnPeriodoInvierno(fecha) && !validacionPeriodo.estaEnPeriodoVerano(fecha))
		{
			return "La fecha indicada no está dentro de un período de traspasos habilitado (invierno o verano)";
		}
 
		return null;
	}
 
	/**
	 * RF-045: el secretario receptor tiene 72 horas habiles para aceptar/rechazar
	 * la transferencia, contadas desde fecha_solicitud.
	 * Simplificacion: se cuentan 72 horas corridas sobre dias habiles (lunes a viernes),sin contar feriados
	 */
	public String validarPlazoRespuesta(Tranferencias transferencia)
	{
		if(transferencia.getFechaRespuesta() != null)
		{
			// ya fue respondida, no aplica el plazo
			return null;
		}
 
		LocalDate limite = sumarHorasHabiles(transferencia.getFechaSolicitud(), 72);
 
		if(LocalDate.now().isAfter(limite))
		{
			return "El plazo de 72 horas hábiles para responder esta transferencia ya venció";
		}
 
		return null;
	}
 
	private LocalDate sumarHorasHabiles(LocalDate fechaInicio, int horas)
	{
		// 72 horas habiles = 3 dias habiles completos (asumiendo jornada de 24hs administrativas por dia habil)
		int diasHabilesNecesarios = horas / 24;
		LocalDate fecha = fechaInicio;
 
		while(diasHabilesNecesarios > 0)
		{
			fecha = fecha.plusDays(1);
			if(fecha.getDayOfWeek() != DayOfWeek.SATURDAY && fecha.getDayOfWeek() != DayOfWeek.SUNDAY)
			{
				diasHabilesNecesarios--;
			}
		}
 
		return fecha;
	}

}
