package br.com.argos.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Address {

    // ATRIBUTOS
    private UUID idAddress;
    private Integer number;
    private String complement;
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private LocalDateTime updatedAt;
    private boolean active;


    // MÉTODOS CONSTRUTORES
    public Address() {
    }

    public Address(Integer number, String complement, String street, String city, String state, String zipCode) {
        this.number = number;
        this.complement = complement;
        this.street = street;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
    }

    public Address(UUID idAddress, Integer number, String complement, String street, String city, String state, String zipCode) {
        this.idAddress = idAddress;
        this.number = number;
        this.complement = complement;
        this.street = street;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
    }

    public Address(UUID idAddress, Integer number, String complement, String street, String city, String state, String zipCode, LocalDateTime updatedAt, boolean active) {
        this.idAddress = idAddress;
        this.number = number;
        this.complement = complement;
        this.street = street;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.updatedAt = updatedAt;
        this.active = active;
    }

    // MÉTODOS GETTERS
    public UUID getIdAddress() {
        return idAddress;
    }

    public Integer getNumber() {
        return number;
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

    public String getZipCode() {
        return zipCode;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public boolean isActive() {
        return active;
    }

    // MÉTODOS SETTERS
    public void setIdAddress(UUID idAddress) {
        this.idAddress = idAddress;
    }

    public void setNumber(Integer number) {
        this.number = number;
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

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    // MÉTODOS TOSTRING
    @Override
    public String toString() {
        return "Address{" +
                "idAddress=" + idAddress +
                ", number=" + number +
                ", complement='" + complement + '\'' +
                ", street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", updatedAt=" + updatedAt +
                ", active=" + active +
                '}';
    }
}