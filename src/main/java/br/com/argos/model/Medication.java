package br.com.argos.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Medication {

    // ATRIBUTOS
    private UUID idMedication; // ID DO MEDICAMENTO
    private UUID supplierId; // ID DO FORNECEDOR DO MEDICAMENTO
    private Integer indicatedGracePeriodDays; // DIAS DO PERIODO DE CARENCIA INDICADO DO MEDICAMENTO
    private BigDecimal dosege; // DOSE DO MEDICAMENTO
    private String tradeName; // NOME COMERCIAL DO MEDICAMENTO
    private String activeIngredient; // PRINCIPIO ATIVO DO MEDICAMENTO
    private String therapeuticCategory; // CATEGORIA TERAPEUTICA DO MEDICAMENTO
    private String unitOfMeasure; // UNIDADE DE MEDIDA DO MEDICAMENTO
    private String indication; // INDICAÇÃO DO MEDICAMENTO
    private LocalDateTime updatedAt; // ATUALIZADO EM
    private boolean active; // ATIVO



    // MÉTODOS CONSTRUTORES
    public Medication() {
    }

    public Medication(UUID idMedication, UUID supplierId, Integer indicatedGracePeriodDays, BigDecimal dosege, String tradeName, String activeIngredient, String therapeuticCategory, String unitOfMeasure, String indication, LocalDateTime updatedAt, boolean active) {
        this.idMedication = idMedication;
        this.supplierId = supplierId;
        this.indicatedGracePeriodDays = indicatedGracePeriodDays;
        this.dosege = dosege;
        this.tradeName = tradeName;
        this.activeIngredient = activeIngredient;
        this.therapeuticCategory = therapeuticCategory;
        this.unitOfMeasure = unitOfMeasure;
        this.indication = indication;
        this.updatedAt = updatedAt;
        this.active = active;
    }

    public Medication(UUID supplierId, Integer indicatedGracePeriodDays, BigDecimal dosege, String tradeName, String activeIngredient, String therapeuticCategory, String unitOfMeasure, String indication, boolean active) {
        this.supplierId = supplierId;
        this.indicatedGracePeriodDays = indicatedGracePeriodDays;
        this.dosege = dosege;
        this.tradeName = tradeName;
        this.activeIngredient = activeIngredient;
        this.therapeuticCategory = therapeuticCategory;
        this.unitOfMeasure = unitOfMeasure;
        this.indication = indication;
        this.active = active;
    }

    // MÉTODOS GETTERS
    public UUID getIdMedication() {
        return idMedication;
    }

    public UUID getSupplierId() {
        return supplierId;
    }

    public Integer getIndicatedGracePeriodDays() {
        return indicatedGracePeriodDays;
    }

    public BigDecimal getDosege() {
        return dosege;
    }

    public String getTradeName() {
        return tradeName;
    }

    public String getActiveIngredient() {
        return activeIngredient;
    }

    public String getTherapeuticCategory() {
        return therapeuticCategory;
    }

    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public String getIndication() {
        return indication;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public boolean isActive() {
        return active;
    }

    // MÉTODOS SETTERS
    public void setIdMedication(UUID idMedication) {
        this.idMedication = idMedication;
    }

    public void setSupplierId(UUID supplierId) {
        this.supplierId = supplierId;
    }

    public void setIndicatedGracePeriodDays(Integer indicatedGracePeriodDays) {
        this.indicatedGracePeriodDays = indicatedGracePeriodDays;
    }

    public void setDosege(BigDecimal dosege) {
        this.dosege = dosege;
    }

    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
    }

    public void setActiveIngredient(String activeIngredient) {
        this.activeIngredient = activeIngredient;
    }

    public void setTherapeuticCategory(String therapeuticCategory) {
        this.therapeuticCategory = therapeuticCategory;
    }

    public void setUnitOfMeasure(String unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public void setIndication(String indication) {
        this.indication = indication;
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
        return "Medication{" +
                "idMedication=" + idMedication +
                ", supplierId=" + supplierId +
                ", indicatedGracePeriodDays=" + indicatedGracePeriodDays +
                ", dosege=" + dosege +
                ", tradeName='" + tradeName + '\'' +
                ", activeIngredient='" + activeIngredient + '\'' +
                ", therapeuticCategory='" + therapeuticCategory + '\'' +
                ", unitOfMeasure='" + unitOfMeasure + '\'' +
                ", indication='" + indication + '\'' +
                ", updatedAt=" + updatedAt +
                ", active=" + active +
                '}';
    }
}

