package br.com.argos.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Supplier {

    // ATRIBUTOS
    private UUID idSupplier; // ID DO FORNECEDOR
    private UUID addressId; // ID DO ENDEREÇO DO FORNECEDOR
    private String fullName; // NOME COMPLETO DO FORNECEDOR
    private String cnpj; // CNPJ DO FORNECEDOR
    private String phone; // TELEFONE DO FORNECEDOR
    private String email; // EMAIL DO FORNECEDOR
    private LocalDateTime updatedAt; // ATUALIZADO EM
    private boolean active; // ATIVO

    // MÉTODOS CONSTRUTORES
    public Supplier() {
    }

    public Supplier(UUID idSupplier, UUID addressId, String fullName, String cnpj, String phone, String email, LocalDateTime updatedAt, boolean active) {
        this.idSupplier = idSupplier;
        this.addressId = addressId;
        this.fullName = fullName;
        this.cnpj = cnpj;
        this.phone = phone;
        this.email = email;
        this.updatedAt = updatedAt;
        this.active = active;
    }

    public Supplier(UUID addressId, String fullName, String cnpj, String phone, String email, boolean active) {
        this.addressId = addressId;
        this.fullName = fullName;
        this.cnpj = cnpj;
        this.phone = phone;
        this.email = email;
        this.active = active;
    }

    // MÉTODOS GETTERS
    public UUID getIdSupplier() {
        return idSupplier;
    }

    public UUID getAddressId() {
        return addressId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getCnpj() {
        return cnpj;
    }

    public String getPhone() {
        return phone;
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
    public void setIdSupplier(UUID idSupplier) {
        this.idSupplier = idSupplier;
    }

    public void setAddressId(UUID addressId) {
        this.addressId = addressId;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    // MÉTODO TO STRING
    @Override
    public String toString() {
        return "Supplier{" +
                "idSupplier=" + idSupplier +
                ", addressId=" + addressId +
                ", fullName='" + fullName + '\'' +
                ", cnpj='" + cnpj + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", updatedAt=" + updatedAt +
                ", active=" + active +
                '}';
    }
}