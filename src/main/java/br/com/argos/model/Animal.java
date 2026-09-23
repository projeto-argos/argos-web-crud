package br.com.argos.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class Animal {

    // ATRIBUTOS
    private UUID idAnimal; // ID DO ANIMAL
    private UUID batchId; // ID DO LOTE DO ANIMAL
    private BigDecimal weight; // PESO DO ANIMAL
    private String earTag; // BRINCO DO ANIMAL
    private String notes; // OBSERVAÇÕES DO ANIMAL
    private String exceptionReason; // MOTIVO DA EXCEÇÃO DO ANIMAL
    private LocalDate exceptionStartDate; // DATA DE INICIO DA EXCEÇÃO DO ANIMAL
    private LocalDate exceptionEndDate; // DATA DO FIM DA EXCEÇÃO DO ANIMAL
    private LocalDate birthDate; // DATA DE NASCIMENTO DO ANIMAL
    private LocalDateTime updatedAt; // ATUALIZADO EM
    private boolean active; // ATIVO
    private boolean clearedForSlaughter; // LIBERADO PARA ABATE

    // MÉTODOS CONSTRUTORES
    public Animal() {
    }

    public Animal(UUID idAnimal, UUID batchId, BigDecimal weight, String earTag, String notes, String exceptionReason, LocalDate exceptionStartDate, LocalDate exceptionEndDate, LocalDate birthDate, LocalDateTime updatedAt, boolean active, boolean clearedForSlaughter) {
        this.idAnimal = idAnimal;
        this.batchId = batchId;
        this.weight = weight;
        this.earTag = earTag;
        this.notes = notes;
        this.exceptionReason = exceptionReason;
        this.exceptionStartDate = exceptionStartDate;
        this.exceptionEndDate = exceptionEndDate;
        this.birthDate = birthDate;
        this.updatedAt = updatedAt;
        this.active = active;
        this.clearedForSlaughter = clearedForSlaughter;
    }

    public Animal(UUID batchId, BigDecimal weight, String earTag, String notes, String exceptionReason, LocalDate exceptionStartDate, LocalDate exceptionEndDate, LocalDate birthDate, boolean active, boolean clearedForSlaughter) {
        this.batchId = batchId;
        this.weight = weight;
        this.earTag = earTag;
        this.notes = notes;
        this.exceptionReason = exceptionReason;
        this.exceptionStartDate = exceptionStartDate;
        this.exceptionEndDate = exceptionEndDate;
        this.birthDate = birthDate;
        this.active = active;
        this.clearedForSlaughter = clearedForSlaughter;
    }

    // MÉTODOS GETTERS
    public UUID getIdAnimal() {
        return idAnimal;
    }

    public UUID getBatchId() {
        return batchId;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public String getEarTag() {
        return earTag;
    }

    public String getNotes() {
        return notes;
    }

    public String getExceptionReason() {
        return exceptionReason;
    }

    public LocalDate getExceptionStartDate() {
        return exceptionStartDate;
    }

    public LocalDate getExceptionEndDate() {
        return exceptionEndDate;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isClearedForSlaughter() {
        return clearedForSlaughter;
    }

    // MÉTODOS SETTERS
    public void setIdAnimal(UUID idAnimal) {
        this.idAnimal = idAnimal;
    }

    public void setBatchId(UUID batchId) {
        this.batchId = batchId;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public void setEarTag(String earTag) {
        this.earTag = earTag;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setExceptionReason(String exceptionReason) {
        this.exceptionReason = exceptionReason;
    }

    public void setExceptionStartDate(LocalDate exceptionStartDate) {
        this.exceptionStartDate = exceptionStartDate;
    }

    public void setExceptionEndDate(LocalDate exceptionEndDate) {
        this.exceptionEndDate = exceptionEndDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setClearedForSlaughter(boolean clearedForSlaughter) {
        this.clearedForSlaughter = clearedForSlaughter;
    }

    // MÉTODOS TOSTRING
    @Override
    public String toString() {
        return "Animal{" +
                "idAnimal=" + idAnimal +
                ", batchId=" + batchId +
                ", weight=" + weight +
                ", earTag='" + earTag + '\'' +
                ", notes='" + notes + '\'' +
                ", exceptionReason='" + exceptionReason + '\'' +
                ", exceptionStartDate=" + exceptionStartDate +
                ", exceptionEndDate=" + exceptionEndDate +
                ", birthDate=" + birthDate +
                ", updatedAt=" + updatedAt +
                ", active=" + active +
                ", clearedForSlaughter=" + clearedForSlaughter +
                '}';
    }
}
