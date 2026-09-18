package br.com.argos.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class GracePeriod {

    // ATRIBUTOS
    private UUID idGracePeriod; // ID DO PERIODO DE CARENCIA
    private UUID animalId; // ID DO ANIMAL DO PERIODO DE CARENCIA
    private String notes; // OBSERVACOES DO PERIODO DE CARENCIA
    private LocalDate startDate; // DATA DE INICIO DO PERIODO DE CARENCIA
    private LocalDate endDate; // DATA DO FIM DO PERIODO DE CARENCIA
    private LocalDateTime updatedAt; // ATUALIZADO EM
    private boolean active; // ATIVO

    // MÉTODOS CONSTRUTORES
    public GracePeriod() {
    }

    public GracePeriod(UUID idGracePeriod, UUID animalId, String notes, LocalDate startDate, LocalDate endDate, LocalDateTime updatedAt, boolean active) {
        this.idGracePeriod = idGracePeriod;
        this.animalId = animalId;
        this.notes = notes;
        this.startDate = startDate;
        this.endDate = endDate;
        this.updatedAt = updatedAt;
        this.active = active;
    }

    public GracePeriod(UUID animalId, String notes, LocalDate startDate, LocalDate endDate, boolean active) {
        this.animalId = animalId;
        this.notes = notes;
        this.startDate = startDate;
        this.endDate = endDate;
        this.active = active;
    }

    // MÉTODOS GETTERS
    public UUID getIdGracePeriod() {
        return idGracePeriod;
    }

    public UUID getAnimalId() {
        return animalId;
    }

    public String getNotes() {
        return notes;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public boolean isActive() {
        return active;
    }

    // MÉTODOS SETTERS
    public void setIdGracePeriod(UUID idGracePeriod) {
        this.idGracePeriod = idGracePeriod;
    }

    public void setAnimalId(UUID animalId) {
        this.animalId = animalId;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    // MÉTODOS TOSTRING
    @Override
    public String toString() {
        return "GracePeriod{" +
                "idGracePeriod=" + idGracePeriod +
                ", animalId=" + animalId +
                ", notes='" + notes + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", updatedAt=" + updatedAt +
                ", active=" + active +
                '}';
    }
}
