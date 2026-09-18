package br.com.argos.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Herd {

    // ATRIBUTOS
    private UUID idHerd; // ID DO REBANHO
    private UUID propertyId; // ID DA PROPRIEDADE DO REBANHO
    private Integer originalHeadCount; // QUANTIDADE ORIGINAL DE CABEÇAS DO REBANHO
    private String name; // NOME DO REBANHO
    private String breed; // RAÇA DO REBANHO
    private String  purpose; // FINALIDADE DO REBANHO
    private LocalDateTime updatedAt; // ATUALIZADO EM
    private boolean active; // ATIVO

    // MÉTODOS CONSTRUTORES
    public Herd() {
    }

    public Herd(UUID idHerd, UUID propertyId, Integer originalHeadCount, String name, String breed, String purpose, LocalDateTime updatedAt, boolean active) {
        this.idHerd = idHerd;
        this.propertyId = propertyId;
        this.originalHeadCount = originalHeadCount;
        this.name = name;
        this.breed = breed;
        this.purpose = purpose;
        this.updatedAt = updatedAt;
        this.active = active;
    }

    public Herd(UUID propertyId, Integer originalHeadCount, String name, String breed, String purpose, boolean active) {
        this.propertyId = propertyId;
        this.originalHeadCount = originalHeadCount;
        this.name = name;
        this.breed = breed;
        this.purpose = purpose;
        this.active = active;
    }

    // MÉTODOS GETTERS
    public UUID getIdHerd() {
        return idHerd;
    }

    public UUID getPropertyId() {
        return propertyId;
    }

    public Integer getOriginalHeadCount() {
        return originalHeadCount;
    }

    public String getName() {
        return name;
    }

    public String getBreed() {
        return breed;
    }

    public String getPurpose() {
        return purpose;
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

    public void setOriginalHeadCount(Integer originalHeadCount) {
        this.originalHeadCount = originalHeadCount;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
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
                ", originalHeadCount=" + originalHeadCount +
                ", name='" + name + '\'' +
                ", breed='" + breed + '\'' +
                ", purpose='" + purpose + '\'' +
                ", updatedAt=" + updatedAt +
                ", active=" + active +
                '}';
    }
}
