
package br.com.argos.service;

import br.com.argos.dao.AddressDAO;
import br.com.argos.exceptions.RequiredFieldException;
import br.com.argos.exceptions.ValidationException;
import br.com.argos.model.Address;
import br.com.argos.model.Lot;
import br.com.argos.util.FilterUtil;

import java.time.LocalDate;
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
                addressDAO, "AddressDAO cannot be null"
        );
    }

    public void create(Address address) {
        createAndReturnId(address);
    }

    public UUID createAndReturnId(Address address) {
        Address normalized = normalizeAddress(address);
        validateAddress(normalized);
        addressDAO.insert(normalized);
        address.setIdAddress(normalized.getIdAddress());
        return normalized.getIdAddress();
    }

    public Address findById(UUID id) {
        if (id == null) {
            throw new RequiredFieldException("id");
        }

        return addressDAO.findById(id);
    }

    public List<Address> filter(String number, String complement, String street, String city, String state, String zipCode) {
        return findAll().stream()
                .filter(address -> number == null || number.isBlank() || String.valueOf(address.getNumber()).equals(number.trim()))
                .filter(address -> FilterUtil.contains(address.getComplement(), complement))
                .filter(address -> FilterUtil.contains(address.getStreet(), street))
                .filter(address -> FilterUtil.contains(address.getCity(), city))
                .filter(address -> FilterUtil.contains(address.getState(), state))
                .filter(address -> FilterUtil.contains(address.getZipCode(), FilterUtil.digits(zipCode)))
                .toList();
    }

    public List<Address> findAll() {
        return addressDAO.findAll();
    }

    public void update(Address address) {
        Address normalized = normalizeAddress(address);

        if (normalized.getIdAddress() == null) {
            throw new RequiredFieldException("id");
        }

        validateAddress(normalized);
        addressDAO.update(normalized);
    }

    public void delete(UUID id) {
        if (id == null) {
            throw new RequiredFieldException("id");
        }

        addressDAO.delete(id);
    }

    private Address normalizeAddress(Address address) {
        if (address == null) {
        throw new ValidationException("O endereço não pode ser nulo.");
    }

        String state = text(address.getState());
        String zipCode = text(address.getZipCode());

        return new Address(
                address.getIdAddress(),
                address.getNumber(),
                text(address.getComplement()),
                text(address.getStreet()),
                text(address.getCity()),
                state != null ? state.toUpperCase() : null,
                zipCode,
                address.getUpdatedAt(),
                address.isActive()
        );
    }

    private void validateAddress(Address address) {
        if (address.getNumber() != null && address.getNumber() < 0) {
        throw new ValidationException(
                "O número do endereço não pode ser negativo."
        );
    }

        if (address.getStreet() != null
                && address.getStreet().length() > 150) {
            throw new ValidationException(
                    "A rua não pode exceder 150 caracteres."
            );
        }

        if (address.getCity() != null
                && address.getCity().length() > 100) {
            throw new ValidationException(
                    "A cidade não pode exceder 100 caracteres."
            );
        }

        if (address.getState() != null
                && !address.getState().matches("[A-Z]{2}")) {
            throw new ValidationException(
                    "O estado deve conter exatamente 2 letras."
            );
        }

        if (address.getZipCode() != null
                && !address.getZipCode().matches("\\d{8}")) {
            throw new ValidationException(
                    "O CEP deve conter exatamente 8 dígitos."
            );
        }
    }
}
