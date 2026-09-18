package br.com.argos.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Herd {

    // ATRIBUTOS
    private UUID idHerd;
    private UUID propertyId;
    private String name;
    private String breed;
    private String description;
    private LocalDateTime updatedAt;
    private boolean active;

    // MÉTODOS CONSTRUTORES
    public Herd() {
    }

    public Herd(UUID idHerd, UUID propertyId, String name, String breed, String description, LocalDateTime updatedAt, boolean active) {
        this.idHerd = idHerd;
        this.propertyId = propertyId;
        this.name = name;
        this.breed = breed;
        this.description = description;
        this.updatedAt = updatedAt;
        this.active = active;
    }

    public Herd(UUID propertyId, String name, String breed, String description, boolean active) {
        this.propertyId = propertyId;
        this.name = name;
        this.breed = breed;
        this.description = description;
        this.active = active;
    }

    // MÉTODOS GETTERS
    public UUID getIdHerd() {
        return idHerd;
    }

    public UUID getPropertyId() {
        return propertyId;
    }

    public String getName() {
        return name;
    }

    public String getBreed() {
        return breed;
    }

    public String getDescription() {
        return description;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public void setDescription(String description) {
        this.description = description;
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
                ", name='" + name + '\'' +
                ", breed='" + breed + '\'' +
                ", description='" + description + '\'' +
                ", updatedAt=" + updatedAt +
                ", active=" + active +
                '}';
    }
}
