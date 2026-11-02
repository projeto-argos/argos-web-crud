
package br.com.argos.service;

import br.com.argos.dao.PropertyDAO;
import br.com.argos.exceptions.RequiredFieldException;
import br.com.argos.exceptions.ValidationException;
import br.com.argos.model.Property;
import br.com.argos.util.FilterUtil;
import br.com.argos.util.Validator;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static br.com.argos.util.Normalizer.onlyDigits;
import static br.com.argos.util.Normalizer.text;

/**
 * Regras de negócio e validações relacionadas a propriedades.
 */
public class PropertyService {

    private final PropertyDAO propertyDAO;

    public PropertyService() {
        this(new PropertyDAO());
    }

    public PropertyService(PropertyDAO propertyDAO) {
        this.propertyDAO = Objects.requireNonNull(
                propertyDAO, "PropertyDAO cannot be null");
    }

    public void create(Property property) {
        Property normalized = normalizeProperty(property);
        validateProperty(normalized);
        propertyDAO.insert(normalized);
    }

    public Property findById(UUID id) {
        if (id == null) {
            throw new RequiredFieldException("ID");
        }

        return propertyDAO.findById(id);
    }

    public List<Property> filter(String name, String cnpj, String phone) {
        return findAll().stream()
                .filter(property -> FilterUtil.contains(property.getName(), name))
                .filter(property -> FilterUtil.contains(property.getCnpj(), FilterUtil.digits(cnpj)))
                .filter(property -> FilterUtil.contains(property.getPhone(), FilterUtil.digits(phone)))
                .toList();
    }

    public List<Property> findAll() {
        return propertyDAO.findAll();
    }

    public List<Property> findByName(String name) {
        if (name == null || name.isBlank()) {
            throw new RequiredFieldException("nome");
        }

        return propertyDAO.findByName(name.trim());
    }

    public List<Property> findByUser(UUID userId) {
        if (userId == null) {
            throw new RequiredFieldException("usuário");
        }

        return propertyDAO.findByUser(userId);
    }

    public void update(Property property) {
        Property normalized = normalizeProperty(property);

        if (normalized.getIdProperty() == null) {
            throw new RequiredFieldException("ID");
        }

        validateProperty(normalized);
        propertyDAO.update(normalized);
    }

    public void delete(UUID id) {
        if (id == null) {
            throw new RequiredFieldException("ID");
        }

        propertyDAO.delete(id);
    }

    /** Exclui a propriedade pelo endereço dela. */
    public void deleteByAddress(UUID addressId) {
        if (addressId == null) {
            throw new RequiredFieldException("endereço");
        }

        propertyDAO.deleteByAddress(addressId);
    }

    public int removeUserLinks(UUID userId) {
        if (userId == null) {
            throw new RequiredFieldException("usuário");
        }

        return propertyDAO.removeUserLinks(userId);
    }

    private Property normalizeProperty(Property property) {
        if (property == null) {
            throw new ValidationException("A propriedade não pode ser nula.");
        }

        List<UUID> userIds = property.getUserIds() == null
                ? new ArrayList<>()
                : new ArrayList<>(property.getUserIds());
        userIds.removeIf(Objects::isNull);
        userIds = new ArrayList<>(new LinkedHashSet<>(userIds));

        return new Property(
                property.getIdProperty(),
                property.getAddressId(),
                userIds,
                text(property.getName()),
                onlyDigits(property.getPhone()),
                onlyDigits(property.getCnpj()),
                property.getUpdatedAt(),
                property.isActive()
        );
    }

    private void validateProperty(Property property) {

//          OBRIGATÓRIOS
        if (property.getName() == null || property.getName().isBlank()) {
            throw new RequiredFieldException("nome");
        }

        if (property.getName().length() > 120) {
            throw new ValidationException("O nome não pode exceder 120 caracteres.");}

        if (property.getAddressId() == null) {
            throw new RequiredFieldException("endereço");
        }

        // toda propriedade precisa de pelo menos um usuário
        if (property.getUserIds() == null || property.getUserIds().isEmpty()) {
            throw new RequiredFieldException("usuários");
        }

//        OPTCIONAIS
        if (property.getCnpj() != null && !Validator.isValidCnpj(property.getCnpj())) {
            throw new ValidationException("CNPJ inválido.");
        }

        if (property.getPhone() != null && !Validator.isValidPhone(property.getPhone())) {
            throw new ValidationException("Telefone inválido.");
        }
    }
}
