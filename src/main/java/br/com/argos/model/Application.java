package br.com.argos.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Application {
    // ATRIBUTOS
    private UUID idApplication; // ID DA APLICACAO
    private UUID idUser; // ID DO USER DA APLICACAO
    private UUID idMedicationBatch; // ID DO LOTE DO MEDICAMENTO DA APLICACAO
    private UUID idBatch; // ID DO LOTE DA APLICACAO
    private BigDecimal dosage; // DOSAGEM DA APLICACAO
    private String unitOfMeasure; // UNIDADE DE MEDIDA DA APLICACAO
    private String appliedLimb; // MEMBRO APLICADO DA APLICACAO
    private String appliedSite; // LOCAL APLICADO DA APLICACAO
    private String administrationRoute; // VIA ADMINISTRACAO DA APLICACAO
    private String objective; // OBJETIVO DA APLICACAO
    private String notes; // OBSERVACOES DA APLICACAO
    private LocalDateTime dateTime; // DATA E HORA DA APLICACAO
    private LocalDateTime updatedAt; // ATUALIZADO EM
    private boolean active; // ATIVO

    // MÉTODOS CONSTRUTORES
    public Application() {
    }

    // MÉTODOS GETTERS
    public UUID getIdApplication() {
        return idApplication;
    }

    public UUID getIdUser() {
        return idUser;
    }

    public UUID getIdMedicationBatch() {
        return idMedicationBatch;
    }

    public UUID getIdBatch() {
        return idBatch;
    }

    public BigDecimal getDosage() {
        return dosage;
    }

    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public String getAppliedLimb() {
        return appliedLimb;
    }

    public String getAppliedSite() {
        return appliedSite;
    }

    public String getAdministrationRoute() {
        return administrationRoute;
    }

    public String getObjective() {
        return objective;
    }

    public String getNotes() {
        return notes;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public boolean isActive() {
        return active;
    }

    // MÉTODOS SETTERS
    public void setIdApplication(UUID idApplication) {
        this.idApplication = idApplication;
    }

    public void setIdUser(UUID idUser) {
        this.idUser = idUser;
    }

    public void setIdMedicationBatch(UUID idMedicationBatch) {
        this.idMedicationBatch = idMedicationBatch;
    }

    public void setIdBatch(UUID idBatch) {
        this.idBatch = idBatch;
    }

    public void setDosage(BigDecimal dosage) {
        this.dosage = dosage;
    }

    public void setUnitOfMeasure(String unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public void setAppliedLimb(String appliedLimb) {
        this.appliedLimb = appliedLimb;
    }

    public void setAppliedSite(String appliedSite) {
        this.appliedSite = appliedSite;
    }

    public void setAdministrationRoute(String administrationRoute) {
        this.administrationRoute = administrationRoute;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
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
        return "Application{" +
                "idApplication=" + idApplication +
                ", idUser=" + idUser +
                ", idMedicationBatch=" + idMedicationBatch +
                ", idBatch=" + idBatch +
                ", dosage=" + dosage +
                ", unitOfMeasure='" + unitOfMeasure + '\'' +
                ", appliedLimb='" + appliedLimb + '\'' +
                ", appliedSite='" + appliedSite + '\'' +
                ", administrationRoute='" + administrationRoute + '\'' +
                ", objective='" + objective + '\'' +
                ", notes='" + notes + '\'' +
                ", dateTime=" + dateTime +
                ", updatedAt=" + updatedAt +
                ", active=" + active +
                '}';
    }
}
