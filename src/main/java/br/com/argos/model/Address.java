package br.com.argos.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Address {

    // ATRIBUTOS
    private UUID idAddress;
    private String complement;
    private String street;
    private String city;
    private String state;
    private LocalDateTime updatedAt;
    private boolean active;
    private Integer number;

    // MÉTODOS CONSTRUTORES
    public Address() {
    }

    public Address(UUID idAddress, String complement, String street, String city, String state, LocalDateTime updatedAt, boolean active, Integer number) {
        this.idAddress = idAddress;
        this.complement = complement;
        this.street = street;
        this.city = city;
        this.state = state;
        this.updatedAt = updatedAt;
        this.active = active;
        this.number = number;
    }

    public Address(String complement, String street, String city, String state, boolean active, Integer number) {
        this.complement = complement;
        this.street = street;
        this.city = city;
        this.state = state;
        this.active = active;
        this.number = number;
    }

    // MÉTODOS GETTERS
    public UUID getIdAddress() {
        return idAddress;
    }

    public String getComplement() {
        return complement;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public boolean isActive() {
        return active;
    }

    public Integer getNumber() {
        return number;
    }

    // MÉTODOS SETTERS
    public void setIdAddress(UUID idAddress) {
        this.idAddress = idAddress;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    // MÉTODOS TOSTRING
    @Override
    public String toString() {
        return "Address{" +
                "idAddress=" + idAddress +
                ", complement='" + complement + '\'' +
                ", street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", updatedAt=" + updatedAt +
                ", active=" + active +
                ", number=" + number +
                '}';
    }
}
