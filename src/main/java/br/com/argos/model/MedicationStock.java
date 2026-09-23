package br.com.argos.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class MedicationStock {
    // ATRIBUTOS
    private UUID idMedicationStock; // ID DO ESTOQUE DE MEDICAMMENTO
    private UUID medicationBatchId; // ID DO LOTE DO MEDICAMENTO DO ESTOQUE DO MEDICAMENTO
    private Integer availableQuantity; // QUANTIDADE DISPONIVEL NO ESTOQUE DO MEDICAMENTO
    private Integer minimumStock; // ESTOQUE MINIMO DO ESTOQUE DO MEDICAMENTO
    private Integer maximumStock; // ESTOQUE MAXIMO DO ESTOQUE DO MEDICAMENTO
    private String storageLocation; // LOCAL ARMAZENAMENTO DO ESTOQUE DO MEDICAMENTO
    private LocalDateTime updatedAt; // ATUALIZADO EM
    private boolean active; // ATIVO

    // MÉTODOS CONSTRUTORES

    public MedicationStock() {
    }

    // MÉTODOS GETTERS
    public UUID getIdMedicationStock() {
        return idMedicationStock;
    }

    public UUID getMedicationBatchId() {
        return medicationBatchId;
    }

    public Integer getAvailableQuantity() {
        return availableQuantity;
    }

    public Integer getMinimumStock() {
        return minimumStock;
    }

    public Integer getMaximumStock() {
        return maximumStock;
    }

    public String getStorageLocation() {
        return storageLocation;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public boolean isActive() {
        return active;
    }

    // MÉTODOS SETTERS
    public void setIdMedicationStock(UUID idMedicationStock) {
        this.idMedicationStock = idMedicationStock;
    }

    public void setMedicationBatchId(UUID medicationBatchId) {
        this.medicationBatchId = medicationBatchId;
    }

    public void setAvailableQuantity(Integer availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public void setMinimumStock(Integer minimumStock) {
        this.minimumStock = minimumStock;
    }

    public void setMaximumStock(Integer maximumStock) {
        this.maximumStock = maximumStock;
    }

    public void setStorageLocation(String storageLocation) {
        this.storageLocation = storageLocation;
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
        return "MedicationStock{" +
                "idMedicationStock=" + idMedicationStock +
                ", medicationBatchId=" + medicationBatchId +
                ", availableQuantity=" + availableQuantity +
                ", minimumStock=" + minimumStock +
                ", maximumStock=" + maximumStock +
                ", storageLocation='" + storageLocation + '\'' +
                ", updatedAt=" + updatedAt +
                ", active=" + active +
                '}';
    }
}