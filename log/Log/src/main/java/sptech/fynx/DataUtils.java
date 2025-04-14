package sptech.fynx;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class DataUtils {

    public static LocalDateTime ajustarParaHorarioBrasilia(LocalDateTime localDateTime) {
        if (localDateTime == null) return null;

        // Ajuste para o fuso horário de Brasília
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());  // Assume o fuso horário do sistema
        ZonedDateTime brasiliaTime = zonedDateTime.withZoneSameInstant(ZoneId.of("America/Sao_Paulo"));

        return brasiliaTime.toLocalDateTime();  // Retorna o LocalDateTime ajustado para Brasília
    }
}

