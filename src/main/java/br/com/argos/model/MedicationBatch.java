package br.com.argos.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class MedicationBatch {
    // ATRIBUTOS
    private UUID idMedicationBatch; // ID DO LOTE DO MEDICAMENTO
    private UUID medicationId; // ID DO MEDICAMENTO DO LOTE DO MEDICAMENTO
    private BigDecimal initialQuantity; // QUANTIDADE INICIAL DO LOTE DO MEDICAMENTO
    private String manufacturingBatchCode; // CODIGO DO LOTE DE FABRICAÇÃO DO LOTE DO MEDICAMENTO
    private LocalDate manufacturingDate; // DATA DE FABRICAÇÃO DOMLOTE DO MEDICAMENTO
    private LocalDate expirationDate; // DATA DE VALIDADO DO LOTE DO MEDICAMENTO
    private LocalDate entryDate; // DATA DE ENTRADA DO LOTE DO MEDICAMENTO
    private LocalDateTime updatedAt; // ATUALIZADO EM
    private boolean active; // ATIVO

    // MÉTODOS CONSTRUTORES
    public MedicationBatch() {
    }

    public MedicationBatch(UUID idMedicationBatch, UUID medicationId, BigDecimal initialQuantity, String manufacturingBatchCode, LocalDate manufacturingDate, LocalDate expirationDate, LocalDate entryDate, LocalDateTime updatedAt, boolean active) {
        this.idMedicationBatch = idMedicationBatch;
        this.medicationId = medicationId;
        this.initialQuantity = initialQuantity;
        this.manufacturingBatchCode = manufacturingBatchCode;
        this.manufacturingDate = manufacturingDate;
        this.expirationDate = expirationDate;
        this.entryDate = entryDate;
        this.updatedAt = updatedAt;
        this.active = active;
    }

    public MedicationBatch(UUID medicationId, BigDecimal initialQuantity, String manufacturingBatchCode, LocalDate manufacturingDate, LocalDate expirationDate, LocalDate entryDate, boolean active) {
        this.medicationId = medicationId;
        this.initialQuantity = initialQuantity;
        this.manufacturingBatchCode = manufacturingBatchCode;
        this.manufacturingDate = manufacturingDate;
        this.expirationDate = expirationDate;
        this.entryDate = entryDate;
        this.active = active;
    }

    // MÉTODOS GETTERS
    public UUID getIdMedicationBatch() {
        return idMedicationBatch;
    }

    public UUID getMedicationId() {
        return medicationId;
    }

    public BigDecimal getInitialQuantity() {
        return initialQuantity;
    }

    public String getManufacturingBatchCode() {
        return manufacturingBatchCode;
    }

    public LocalDate getManufacturingDate() {
        return manufacturingDate;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public LocalDate getEntryDate() {
        return entryDate;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public boolean isActive() {
        return active;
    }

    // MÉTODOS SETTERS
    public void setIdMedicationBatch(UUID idMedicationBatch) {
        this.idMedicationBatch = idMedicationBatch;
    }

    public void setMedicationId(UUID medicationId) {
        this.medicationId = medicationId;
    }

    public void setInitialQuantity(BigDecimal initialQuantity) {
        this.initialQuantity = initialQuantity;
    }

    public void setManufacturingBatchCode(String manufacturingBatchCode) {
        this.manufacturingBatchCode = manufacturingBatchCode;
    }

    public void setManufacturingDate(LocalDate manufacturingDate) {
        this.manufacturingDate = manufacturingDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public void setEntryDate(LocalDate entryDate) {
        this.entryDate = entryDate;
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
        return "MedicationBatch{" +
                "idMedicationBatch=" + idMedicationBatch +
                ", medicationId=" + medicationId +
                ", initialQuantity=" + initialQuantity +
                ", manufacturingBatchCode='" + manufacturingBatchCode + '\'' +
                ", manufacturingDate=" + manufacturingDate +
                ", expirationDate=" + expirationDate +
                ", entryDate=" + entryDate +
                ", updatedAt=" + updatedAt +
                ", active=" + active +
                '}';
    }
}
