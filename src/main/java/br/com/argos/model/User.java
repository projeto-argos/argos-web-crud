package br.com.argos.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class User {

    // ATRIBUTOS
    private UUID idUser; // ID DO USÚARIO
    private String fullName; // NOME COMPLETO DO USÚARIO
    private String cpf; // CPF DO USÚARIO
    private String email; // EMAIL DO USÚARIO
    private String phone; // TELEFONE DO USÚARIO
    private String role; // CARGO DO USÚARIO
    private String password; // SENHA DO USÚARIO
    private LocalDate birthDate; // DATA DE NASCIMENTO DO USÚARIO
    private LocalDateTime updatedAt; // ATUALIZADO EM
    private boolean active; // ATIVO

    // MÉTODOS CONSTRUTORES
    public User() {
    }

    public User(UUID idUser, String fullName, String cpf, String email, String phone, String role, String password, LocalDate birthDate, LocalDateTime updatedAt, boolean active) {
        this.idUser = idUser;
        this.fullName = fullName;
        this.cpf = cpf;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.password = password;
        this.birthDate = birthDate;
        this.updatedAt = updatedAt;
        this.active = active;
    }

    public User(String fullName, String cpf, String email, String phone, String role, String password, LocalDate birthDate, boolean active) {
        this.fullName = fullName;
        this.cpf = cpf;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.password = password;
        this.birthDate = birthDate;
        this.active = active;
    }

    // MÉTODOS GETTERS
    public UUID getIdUser() {
        return idUser;
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

    public String getRole() {
        return role;
    }

    public String getPassword() {
        return password;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public boolean isActive() {
        return active;
    }

    // MÉTODOS SETTERS
    public void setIdUser(UUID idUser) {
        this.idUser = idUser;
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

    public void setRole(String role) {
        this.role = role;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
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
        return "User{" +
                "idUser=" + idUser +
                ", fullName='" + fullName + '\'' +
                ", cpf='" + cpf + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", role='" + role + '\'' +
                ", password='" + password + '\'' +
                ", birthDate=" + birthDate +
                ", updatedAt=" + updatedAt +
                ", active=" + active +
                '}';
    }
}