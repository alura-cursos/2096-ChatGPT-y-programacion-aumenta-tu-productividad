package med.voll.api.domain.consulta.validaciones;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import jakarta.validation.ValidationException;
import med.voll.api.domain.consulta.dto.DatosAgendarConsulta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.time.LocalDateTime;

class HorarioDeFuncionamientoClinicaTest {

    private HorarioDeFuncionamientoClinica validador;
    private DatosAgendarConsulta datosMock;

    @BeforeEach
    void setUp() {
        validador = new HorarioDeFuncionamientoClinica();
        datosMock = mock(DatosAgendarConsulta.class);
    }

    @Test
    void testValidar_ConsultaEnDomingo() {
        when(datosMock.fecha()).thenReturn(LocalDateTime.of(2024, 5, 26, 10, 0)); // Domingo

        Executable executable = () -> validador.validar(datosMock);

        ValidationException exception = assertThrows(ValidationException.class, executable);
        assertEquals("El horario de atención de la clínica es de lunes a sábado, de 07:00 a 19:00 horas", exception.getMessage());
    }

    @Test
    void testValidar_ConsultaAntesDeApertura() {
        when(datosMock.fecha()).thenReturn(LocalDateTime.of(2024, 5, 27, 6, 0)); // Lunes antes de las 7:00

        Executable executable = () -> validador.validar(datosMock);

        ValidationException exception = assertThrows(ValidationException.class, executable);
        assertEquals("El horario de atención de la clínica es de lunes a sábado, de 07:00 a 19:00 horas", exception.getMessage());
    }

    @Test
    void testValidar_ConsultaDespuesDeCierre() {
        when(datosMock.fecha()).thenReturn(LocalDateTime.of(2024, 5, 27, 20, 0)); // Lunes después de las 19:00

        Executable executable = () -> validador.validar(datosMock);

        ValidationException exception = assertThrows(ValidationException.class, executable);
        assertEquals("El horario de atención de la clínica es de lunes a sábado, de 07:00 a 19:00 horas", exception.getMessage());
    }

    @Test
    void testValidar_ConsultaDentroDeHorario() {
        when(datosMock.fecha()).thenReturn(LocalDateTime.of(2024, 5, 27, 10, 0)); // Lunes dentro del horario

        assertDoesNotThrow(() -> validador.validar(datosMock));
    }
    @Test
    void testValidar_ConsultaJustoApertura() {
        when(datosMock.fecha()).thenReturn(LocalDateTime.of(2024, 5, 27, 7, 0)); // Exactamente a las 07:00

        assertDoesNotThrow(() -> validador.validar(datosMock));
    }

    @Test
    void testValidar_ConsultaJustoCierre() {
        when(datosMock.fecha()).thenReturn(LocalDateTime.of(2024, 5, 27, 19, 0)); // Exactamente a las 19:00

        assertDoesNotThrow(() -> validador.validar(datosMock));
    }

    @Test
    void testValidar_ConsultaJustoDespuesDeApertura() {
        when(datosMock.fecha()).thenReturn(LocalDateTime.of(2024, 5, 27, 7, 1)); // Exactamente a las 07:01

        assertDoesNotThrow(() -> validador.validar(datosMock));
    }

    @Test
    void testValidar_ConsultaJustoDespuesDeCierre() {
        when(datosMock.fecha()).thenReturn(LocalDateTime.of(2024, 5, 27, 19, 1)); // Exactamente a las 19:01

        Executable executable = () -> validador.validar(datosMock);

        ValidationException exception = assertThrows(ValidationException.class, executable);
        assertEquals("El horario de atención de la clínica es de lunes a sábado, de 07:00 a 19:00 horas", exception.getMessage());
    }


}
