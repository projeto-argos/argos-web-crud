package br.com.argos.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Medication {

    // ATRIBUTOS
    private UUID idMedication;
    private UUID supplierId;
    private String tradeName;
    private String activeIngredient;
    private String therapeuticCategory;
    private String unitOfMeasure;
    private LocalDateTime updatedAt;
    private boolean active;
    private int withdrawalPeriodDays;
    private BigDecimal dose;

    // MÉTODOS CONSTRUTORES
    public Medication() {
    }

    public Medication(UUID idMedication, UUID supplierId, String tradeName, String activeIngredient, String therapeuticCategory, String unitOfMeasure, LocalDateTime updatedAt, boolean active, int withdrawalPeriodDays, BigDecimal dose) {
        this.idMedication = idMedication;
        this.supplierId = supplierId;
        this.tradeName = tradeName;
        this.activeIngredient = activeIngredient;
        this.therapeuticCategory = therapeuticCategory;
        this.unitOfMeasure = unitOfMeasure;
        this.updatedAt = updatedAt;
        this.active = active;
        this.withdrawalPeriodDays = withdrawalPeriodDays;
        this.dose = dose;
    }

    public Medication(UUID supplierId, String tradeName, String activeIngredient, String therapeuticCategory, String unitOfMeasure, boolean active, int withdrawalPeriodDays, BigDecimal dose) {
        this.supplierId = supplierId;
        this.tradeName = tradeName;
        this.activeIngredient = activeIngredient;
        this.therapeuticCategory = therapeuticCategory;
        this.unitOfMeasure = unitOfMeasure;
        this.active = active;
        this.withdrawalPeriodDays = withdrawalPeriodDays;
        this.dose = dose;
    }

    // MÉTODOS GETTERS
    public UUID getIdMedication() {
        return idMedication;
    }

    public UUID getSupplierId() {
        return supplierId;
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

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public boolean isActive() {
        return active;
    }

    public int getWithdrawalPeriodDays() {
        return withdrawalPeriodDays;
    }

    public BigDecimal getDose() {
        return dose;
    }

    // MÉTODOS SETTERS
    public void setIdMedication(UUID idMedication) {
        this.idMedication = idMedication;
    }

    public void setSupplierId(UUID supplierId) {
        this.supplierId = supplierId;
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

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setWithdrawalPeriodDays(int withdrawalPeriodDays) {
        this.withdrawalPeriodDays = withdrawalPeriodDays;
    }

    public void setDose(BigDecimal dose) {
        this.dose = dose;
    }

    // MÉTODO TOSTRING
    @Override
    public String toString() {
        return "Medication{" +
                "idMedication=" + idMedication +
                ", supplierId=" + supplierId +
                ", tradeName='" + tradeName + '\'' +
                ", activeIngredient='" + activeIngredient + '\'' +
                ", therapeuticCategory='" + therapeuticCategory + '\'' +
                ", unitOfMeasure='" + unitOfMeasure + '\'' +
                ", updatedAt=" + updatedAt +
                ", active=" + active +
                ", withdrawalPeriodDays=" + withdrawalPeriodDays +
                ", dose=" + dose +
                '}';
    }
}
