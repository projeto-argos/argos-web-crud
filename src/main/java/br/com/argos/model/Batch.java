package br.com.argos.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class Batch {

    // ATRIBUTOS
    private UUID idBatch;
    private UUID herdId;
    private String category;
    private LocalDate openingDate;
    private LocalDateTime updatedAt;
    private boolean active;

    // MÉTODOS CONSTRUTORES

    public Batch() {
    }

    public Batch(UUID idBatch, UUID herdId, String category, LocalDate openingDate, LocalDateTime updatedAt, boolean active) {
        this.idBatch = idBatch;
        this.herdId = herdId;
        this.category = category;
        this.openingDate = openingDate;
        this.updatedAt = updatedAt;
        this.active = active;
    }

    public Batch(UUID herdId, String category, LocalDate openingDate, boolean active) {
        this.herdId = herdId;
        this.category = category;
        this.openingDate = openingDate;
        this.active = active;
    }

    // MÉTODOS GETTERS
    public UUID getIdBatch() {
        return idBatch;
    }

    public UUID getHerdId() {
        return herdId;
    }

    public String getCategory() {
        return category;
    }

    public LocalDate getOpeningDate() {
        return openingDate;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public boolean isActive() {
        return active;
    }

    // MÉTODOS SETTERS
    public void setIdBatch(UUID idBatch) {
        this.idBatch = idBatch;
    }

    public void setHerdId(UUID herdId) {
        this.herdId = herdId;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setOpeningDate(LocalDate openingDate) {
        this.openingDate = openingDate;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    // MÉTODO TOSTRING
    @Override
    public String toString() {
        return "Batch{" +
                "idBatch=" + idBatch +
                ", herdId=" + herdId +
                ", category='" + category + '\'' +
                ", openingDate=" + openingDate +
                ", updatedAt=" + updatedAt +
                ", active=" + active +
                '}';
    }
}
