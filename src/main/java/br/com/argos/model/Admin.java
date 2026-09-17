package br.com.argos.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Admin {

    // ATRIBUTOS
    private UUID idAdmin;
    private String fullName;
    private String cpf;
    private String email;
    private String phone;
    private String password;
    private LocalDateTime updatedAt;
    private boolean active;

    // MÉTODOS CONSTRUTORES
    public Admin() {
    }

    public Admin(UUID idAdmin, String fullName, String cpf, String email, String phone, String password, LocalDateTime updatedAt, boolean active) {
        this.idAdmin = idAdmin;
        this.fullName = fullName;
        this.cpf = cpf;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.updatedAt = updatedAt;
        this.active = active;
    }

    public Admin(String fullName, String cpf, String email, String phone, String password, boolean active) {
        this.fullName = fullName;
        this.cpf = cpf;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.active = active;
    }

    // MÉTODOS GETTERS
    public UUID getIdAdmin() {
        return idAdmin;
    }

    public String getFullName() {
        return fullName;
    }

    public String getCpf() {
        return cpf;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public boolean isActive() {
        return active;
    }

    // MÉTODOS SETTERS
    public void setIdAdmin(UUID idAdmin) {
        this.idAdmin = idAdmin;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPassword(String password) {
        this.password = password;
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
        return "Admin{" +
                "idAdmin=" + idAdmin +
                ", fullName='" + fullName + '\'' +
                ", cpf='" + cpf + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", updatedAt=" + updatedAt +
                ", active=" + active +
                '}';
    }
}