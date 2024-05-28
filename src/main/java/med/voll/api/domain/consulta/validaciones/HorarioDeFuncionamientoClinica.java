package med.voll.api.domain.consulta.validaciones;

import jakarta.validation.ValidationException;
import med.voll.api.domain.consulta.dto.DatosAgendarConsulta;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

@Component
public class HorarioDeFuncionamientoClinica implements ValidadorDeConsultas{
    public void validar(DatosAgendarConsulta datos) {
        LocalDateTime fechaConsulta = datos.fecha();
        DayOfWeek diaDeLaSemana = fechaConsulta.getDayOfWeek();

        boolean esDomingo = DayOfWeek.SUNDAY.equals(diaDeLaSemana);
        boolean antesDeApertura = fechaConsulta.getHour() < 7;
        boolean despuesDeCierre = fechaConsulta.getHour() > 19 ||
                (fechaConsulta.getHour() == 19 && fechaConsulta.getMinute() > 0);

        if (esDomingo || antesDeApertura || despuesDeCierre) {
            throw new ValidationException("El horario de atención de la clínica es de lunes a sábado, de 07:00 a 19:00 horas");
        }
    }
}
