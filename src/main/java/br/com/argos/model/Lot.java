package br.com.argos.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class Lot {

    // ATRIBUTOS
    private UUID idLot;
    private UUID herdId;
    private Integer HeadCount;
    private String category;
    private String lotCode;
    private LocalDate openDate;
    private LocalDateTime updatedAt;
    private boolean active;

    // MÉTODOS CONSTRUTORES

    public Lot() {
    }

    public Lot(UUID herdId, Integer headCount, String category, String lotCode, LocalDate openDate) {
        this.herdId = herdId;
        HeadCount = headCount;
        this.category = category;
        this.lotCode = lotCode;
        this.openDate = openDate;
    }

    public Lot(UUID idLot, UUID herdId, Integer headCount, String category, String lotCode, LocalDate openDate, LocalDateTime updatedAt, boolean active) {
        this.idLot = idLot;
        this.herdId = herdId;
        HeadCount = headCount;
        this.category = category;
        this.lotCode = lotCode;
        this.openDate = openDate;
        this.updatedAt = updatedAt;
        this.active = active;
    }

    // MÉTODOS GETTERS
    public UUID getIdLot() {
        return idLot;
    }

    public UUID getHerdId() {
        return herdId;
    }

    public Integer getHeadCount() {
        return HeadCount;
    }

    public String getCategory() {
        return category;
    }

    public String getLotCode() {
        return lotCode;
    }

    public LocalDate getOpenDate() {
        return openDate;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public boolean isActive() {
        return active;
    }

    // MÉTODOS SETTERS
    public void setIdLot(UUID idLot) {
        this.idLot = idLot;
    }

    public void setHerdId(UUID herdId) {
        this.herdId = herdId;
    }

    public void setHeadCount(Integer headCount) {
        HeadCount = headCount;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setLotCode(String lotCode) {
        this.lotCode = lotCode;
    }

    public void setOpenDate(LocalDate openDate) {
        this.openDate = openDate;
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
        return "Lot{" +
                "idLot=" + idLot +
                ", herdId=" + herdId +
                ", HeadCount=" + HeadCount +
                ", category='" + category + '\'' +
                ", lotCode='" + lotCode + '\'' +
                ", openDate=" + openDate +
                ", updatedAt=" + updatedAt +
                ", active=" + active +
                '}';
    }
}