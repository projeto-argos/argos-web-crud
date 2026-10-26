package br.com.argos.util;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/** Formatação de datas Java Time para exibição nas JSPs. */
public final class JspDateFormat {
    private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DATE_TIME = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private JspDateFormat() { }

    public static String formatDate(Object value) {
        if (value == null) return "";
        if (value instanceof LocalDate date) return DATE.format(date);
        if (value instanceof LocalDateTime dateTime) return DATE.format(dateTime.toLocalDate());
        if (value instanceof Timestamp timestamp) return DATE.format(timestamp.toLocalDateTime().toLocalDate());
        if (value instanceof java.sql.Date date) return DATE.format(date.toLocalDate());
        if (value instanceof Date date) return DATE.format(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        return "";
    }

    public static String formatDateTime(Object value) {
        if (value == null) return "";
        if (value instanceof LocalDateTime dateTime) return DATE_TIME.format(dateTime);
        if (value instanceof Timestamp timestamp) return DATE_TIME.format(timestamp.toLocalDateTime());
        if (value instanceof Date date) return DATE_TIME.format(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        return "";
    }
}
