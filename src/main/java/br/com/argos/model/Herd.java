package br.com.argos.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Herd {

    // ATRIBUTOS
    private UUID idHerd;
    private UUID propertyId;
    private Integer headCount;
    private String purpose;
    private String name;
    private String breed;
    private LocalDateTime updatedAt;
    private boolean active;

    // MÉTODOS CONSTRUTORES
    public Herd() {
    }

    public Herd(UUID idHerd, UUID propertyId, Integer headCount, String purpose, String name, String breed, LocalDateTime updatedAt, boolean active) {
        this.idHerd = idHerd;
        this.propertyId = propertyId;
        this.headCount = headCount;
        this.purpose = purpose;
        this.name = name;
        this.breed = breed;
        this.updatedAt = updatedAt;
        this.active = active;
    }

    public Herd(UUID propertyId, Integer headCount, String purpose, String name, String breed, boolean active) {
        this.propertyId = propertyId;
        this.headCount = headCount;
        this.purpose = purpose;
        this.name = name;
        this.breed = breed;
        this.active = active;
    }

    // MÉTODOS GETTERS
    public UUID getIdHerd() {
        return idHerd;
    }

    public UUID getPropertyId() {
        return propertyId;
    }

    public Integer getHeadCount() {
        return headCount;
    }

    public String getPurpose() {
        return purpose;
    }

    public String getName() {
        return name;
    }

    public String getBreed() {
        return breed;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public boolean isActive() {
        return active;
    }

    // MÉTODOS SETTERS
    public void setIdHerd(UUID idHerd) {
        this.idHerd = idHerd;
    }

    public void setPropertyId(UUID propertyId) {
        this.propertyId = propertyId;
    }

    public void setHeadCount(Integer headCount) {
        this.headCount = headCount;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBreed(String breed) {
        this.breed = breed;
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
        return "Herd{" +
                "idHerd=" + idHerd +
                ", propertyId=" + propertyId +
                ", headCount=" + headCount +
                ", purpose='" + purpose + '\'' +
                ", name='" + name + '\'' +
                ", breed='" + breed + '\'' +
                ", updatedAt=" + updatedAt +
                ", active=" + active +
                '}';
    }
}
