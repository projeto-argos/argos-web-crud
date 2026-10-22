package br.com.argos.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Property {

    // ATRIBUTOS
    private UUID idProperty;
    private UUID addressId;
    private List<UUID> userIds = new ArrayList<>();
    private String name;
    private String phone;
    private String cnpj;
    private LocalDateTime updatedAt;
    private boolean active;

    // MÉTODOS CONSTRUTORES
    public Property() {
    }

    public Property(UUID addressId, List<UUID> userIds, String name, String phone, String cnpj) {
        this.addressId = addressId;
        this.userIds = userIds;
        this.name = name;
        this.phone = phone;
        this.cnpj = cnpj;
    }

    public Property(UUID idProperty, UUID addressId, List<UUID> userIds, String name, String phone, String cnpj, LocalDateTime updatedAt, boolean active) {
        this.idProperty = idProperty;
        this.addressId = addressId;
        this.userIds = userIds;
        this.name = name;
        this.phone = phone;
        this.cnpj = cnpj;
        this.updatedAt = updatedAt;
        this.active = active;
    }

    // MÉTODOS GETTERS
    public UUID getIdProperty() {
        return idProperty;
    }

    public UUID getAddressId() {
        return addressId;
    }

    public List<UUID> getUserIds() {
        return userIds;
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

    public void setAddressId(UUID addressId) {
        this.addressId = addressId;
    }

    public void setUserIds(List<UUID> userIds) {
        this.userIds = userIds;
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
                ", addressId=" + addressId +
                ", userIds=" + userIds +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", cnpj='" + cnpj + '\'' +
                ", updatedAt=" + updatedAt +
                ", active=" + active +
                '}';
    }
}