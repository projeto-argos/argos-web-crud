package br.com.argos.service;

import br.com.argos.dao.PropertyDAO;
import br.com.argos.exceptions.RequiredFieldException;
import br.com.argos.exceptions.ValidationException;
import br.com.argos.model.Property;
import br.com.argos.util.Validador;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static br.com.argos.util.Normalizer.onlyDigits;
import static br.com.argos.util.Normalizer.text;

/** Regras de negócio e validações relacionadas a propriedades. */
public class PropertyService {

    private final PropertyDAO propertyDAO;

    public PropertyService() {
        this(new PropertyDAO());
    }

    public PropertyService(PropertyDAO propertyDAO) {
        this.propertyDAO = Objects.requireNonNull(propertyDAO, "PropertyDAO cannot be null");
    }

    public void create(Property property) {
        Property normalized = normalizeProperty(property);
        validateProperty(normalized);
        propertyDAO.insert(normalized);
    }

    public Property findById(UUID id) {
        if (id == null) {
            throw new RequiredFieldException("id");
        }
        return propertyDAO.findById(id);
    }

    public List<Property> findAll() {
        return propertyDAO.findAll();
    }

    public List<Property> findByName(String name) {
        if (name == null || name.isBlank()) {
            throw new RequiredFieldException("name");
        }
        return propertyDAO.findByName(name.trim());
    }

    public List<Property> findByUser(UUID userId) {
        if (userId == null) {
            throw new RequiredFieldException("userId");
        }
        return propertyDAO.findByUser(userId);
    }

    public void update(Property property) {
        Property normalized = normalizeProperty(property);
        if (normalized.getIdProperty() == null) {
            throw new RequiredFieldException("id");
        }
        validateProperty(normalized);
        propertyDAO.update(normalized);
    }

    public void delete(UUID id) {
        if (id == null) {
            throw new RequiredFieldException("id");
        }
        propertyDAO.delete(id);
    }

    public int deleteByUser(UUID userId) {
        if (userId == null) {
            throw new RequiredFieldException("userId");
        }
        return propertyDAO.deleteByUser(userId);
    }

    /** Limpa os dados */
    private Property normalizeProperty(Property property) {
        if (property == null) {
            throw new ValidationException("Property cannot be null");
        }

        return new Property(
                property.getIdProperty(),
                property.getUserId(),
                property.getAddressId(),
                text(property.getName()),
                onlyDigits(property.getCnpj()),
                onlyDigits(property.getPhone()),
                property.getUpdatedAt(),
                property.isActive());
    }

    private void validateProperty(Property property) {
        // OBRIGATÓRIOS
        if (property.getName() == null) {
            throw new RequiredFieldException("name");
        }

        if (property.getName().length() > 120) {
            throw new ValidationException("Name cannot exceed 120 characters");
        }

        if (property.getUserId() == null) {
            throw new RequiredFieldException("userId");
        }

        // OPCIONAIS
        if (property.getCnpj() != null && !Validador.cnpjValido(property.getCnpj())) {
            throw new ValidationException("Invalid CNPJ");
        }

        if (property.getPhone() != null && !Validador.telefoneValido(property.getPhone())) {
            throw new ValidationException("Invalid phone");
        }
    }
}