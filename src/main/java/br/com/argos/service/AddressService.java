
package br.com.argos.service;

import br.com.argos.dao.AddressDAO;
import br.com.argos.exceptions.RequiredFieldException;
import br.com.argos.exceptions.ValidationException;
import br.com.argos.model.Address;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static br.com.argos.util.Normalizer.text;

/**
 * Regras de negócio e validações relacionadas a endereços.
 */
public class AddressService {

    private final AddressDAO addressDAO;

    public AddressService() {
        this(new AddressDAO());
    }

    public AddressService(AddressDAO addressDAO) {
        this.addressDAO = Objects.requireNonNull(
                addressDAO, "AddressDAO cannot be null");
    }

    // CREATE
    public void create(Address address) {
        Address normalized = normalizeAddress(address);
        validateAddress(normalized);
        addressDAO.insert(normalized);
    }

    // READ BY ID
    public Address findById(UUID id) {
        if (id == null) {
            throw new RequiredFieldException("id");
        }

        return addressDAO.findById(id);
    }

    // READ ALL
    public List<Address> findAll() {
        return addressDAO.findAll();
    }

    // UPDATE
    public void update(Address address) {
        Address normalized = normalizeAddress(address);

        if (normalized.getIdAddress() == null) {
            throw new RequiredFieldException("id");
        }

        validateAddress(normalized);
        addressDAO.update(normalized);
    }

    // SOFT DELETE
    public void delete(UUID id) {
        if (id == null) {
            throw new RequiredFieldException("id");
        }

        addressDAO.delete(id);
    }

    // NORMALIZATION
    private Address normalizeAddress(Address address) {
        if (address == null) {
            throw new ValidationException("Address cannot be null");
        }

        return new Address(
                address.getIdAddress(),
                address.getNumber(),
                text(address.getComplement()),
                text(address.getStreet()),
                text(address.getCity()),
                text(address.getState()) == null
                        ? null
                        : text(address.getState()).toUpperCase(),
                address.getUpdatedAt(),
                address.isActive()
        );
    }

    // VALIDATION
    private void validateAddress(Address address) {

        // OPTIONAL

        if (address.getNumber() != null
                && address.getNumber() < 0) {
            throw new ValidationException(
                    "Address number cannot be negative");
        }

        if (address.getStreet() != null
                && address.getStreet().length() > 150) {
            throw new ValidationException(
                    "Street cannot exceed 150 characters");
        }

        if (address.getCity() != null
                && address.getCity().length() > 100) {
            throw new ValidationException(
                    "City cannot exceed 100 characters");
        }

        if (address.getState() != null
                && !address.getState().matches("[A-Z]{2}")) {
            throw new ValidationException(
                    "State must contain exactly 2 letters");
        }
    }
}
