package br.com.argos.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Property {

    // ATRIBUTOS
    private UUID idProperty;
    private UUID userId;
    private UUID addressId;
    private String name;
    private String phone;
    private String cnpj;
    private String email;
    private LocalDateTime updatedAt;
    private boolean active;

    // MÉTODOS CONSTRUTORES
    public Property() {
    }

    public Property(UUID idProperty, UUID userId, UUID addressId, String name, String phone, String cnpj, String email, LocalDateTime updatedAt, boolean active) {
        this.idProperty = idProperty;
        this.userId = userId;
        this.addressId = addressId;
        this.name = name;
        this.phone = phone;
        this.cnpj = cnpj;
        this.email = email;
        this.updatedAt = updatedAt;
        this.active = active;
    }

    public Property(UUID userId, UUID addressId, String name, String phone, String cnpj, String email, boolean active) {
        this.userId = userId;
        this.addressId = addressId;
        this.name = name;
        this.phone = phone;
        this.cnpj = cnpj;
        this.email = email;
        this.active = active;
    }

    // MÉTODOS GETTERS
    public UUID getIdProperty() {
        return idProperty;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getAddressId() {
        return addressId;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getCnpj() {
        return cnpj;
    }

    public String getEmail() {
        return email;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public boolean isActive() {
        return active;
    }

    // MÉTODOS SETTERS
    public void setIdProperty(UUID idProperty) {
        this.idProperty = idProperty;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public void setAddressId(UUID addressId) {
        this.addressId = addressId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public void setEmail(String email) {
        this.email = email;
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
        return "Property{" +
                "idProperty=" + idProperty +
                ", userId=" + userId +
                ", addressId=" + addressId +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", cnpj='" + cnpj + '\'' +
                ", email='" + email + '\'' +
                ", updatedAt=" + updatedAt +
                ", active=" + active +
                '}';
    }
}
