package br.com.argos.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** Uma linha da tabela "Últimas atualizações" da página inicial. */
public class RecentRecord {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final String name;
    private final String type;
    private final LocalDateTime updatedAt;

    public RecentRecord(String name, String type, LocalDateTime updatedAt) {
        this.name = name;
        this.type = type;
        this.updatedAt = updatedAt;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /** Data já no formato dd/MM/yyyy, para a JSP só exibir. */
    public String getFormattedDate() {
        return updatedAt == null ? "-" : updatedAt.format(DATE_FORMAT);
    }
}
