package br.com.argos.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class Batch {

    // ATRIBUTOS
    private UUID idBatch; // ID DO LOTE
    private UUID herdId; // ID DO REBANHO DO LOTE
    private Integer originalHeadCount; // QUANTIDADE ORIGINAL DE CABEÇAS DO LOTE
    private String category;// CATEGORIA DO LOTE
    private String batchCode; // CODIGO DO LOTE
    private LocalDate openingDate; // DATA ABERTURA DO LOTE
    private LocalDateTime updatedAt; // ATUALIZADO EM
    private boolean active; // ATIVO

    // MÉTODOS CONSTRUTORES

    public Batch() {
    }

    public Batch(UUID idBatch, UUID herdId, Integer originalHeadCount, String category, String batchCode, LocalDate openingDate, LocalDateTime updatedAt, boolean active) {
        this.idBatch = idBatch;
        this.herdId = herdId;
        this.originalHeadCount = originalHeadCount;
        this.category = category;
        this.batchCode = batchCode;
        this.openingDate = openingDate;
        this.updatedAt = updatedAt;
        this.active = active;
    }

    public Batch(UUID herdId, Integer originalHeadCount, String category, String batchCode, LocalDate openingDate, boolean active) {
        this.herdId = herdId;
        this.originalHeadCount = originalHeadCount;
        this.category = category;
        this.batchCode = batchCode;
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

    public Integer getOriginalHeadCount() {
        return originalHeadCount;
    }

    public String getCategory() {
        return category;
    }

    public String getBatchCode() {
        return batchCode;
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

    public void setOriginalHeadCount(Integer originalHeadCount) {
        this.originalHeadCount = originalHeadCount;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
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
                ", originalHeadCount=" + originalHeadCount +
                ", category='" + category + '\'' +
                ", batchCode='" + batchCode + '\'' +
                ", openingDate=" + openingDate +
                ", updatedAt=" + updatedAt +
                ", active=" + active +
                '}';
    }
}
