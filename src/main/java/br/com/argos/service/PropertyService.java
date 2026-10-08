package br.com.argos.service;

import br.com.argos.dao.PropertyDAO;
import br.com.argos.exceptions.RequiredFieldException;
import br.com.argos.exceptions.ValidationException;
import br.com.argos.model.Property;
import br.com.argos.util.Validador;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/** Regras de negócio e validações relacionadas a propriedades. */
public class PropertyService {

    private final PropertyDAO propertyDAO;

    public PropertyService() {
        this(new PropertyDAO());
    }

    public PropertyService(PropertyDAO propertyDAO){
        this.propertyDAO = Objects.requireNonNull(propertyDAO, "PropertyDAO cannot be null");
    }

    public void create(Property property){
        Property limpo = normalizeProperty(property);
        validateProperty(property, true);
        propertyDAO.insert(property);
    }

    public Property findById(UUID id){
        if (id == null){
            throw new RequiredFieldException("id");
        }
        return propertyDAO.findById(id);
    }

    public List<Property> findAll(){
        return propertyDAO.findAll();
    }

    public void update(Property property){
        if (property == null){
            throw new ValidationException("Property cannot be null");
        }
        if (property.getIdProperty() == null){
            throw new RequiredFieldException("id");
        }
        validateProperty(property, false);
        propertyDAO.update(property);
    }

    public void delete(UUID id){
        if (id == null){
            throw new RequiredFieldException("id");
        }
        propertyDAO.delete(id);
    }

    private void validateProperty(Property property, boolean newProperty) {

        if (property == null) {
            throw new ValidationException("Property cannot be null");
        }

        if (property.getName() == null || property.getName().isBlank()) {
            throw new RequiredFieldException("name");
        }

        if (property.getName().length() > 120){
            throw new ValidationException("Name cannot exceed 120 characters");
        }

        if (property.getCnpj() == null || property.getCnpj().isBlank()) {
            throw new RequiredFieldException("cnpj");
        }

        if (!Validador.cnpjValido(property.getCnpj())) {
            throw new ValidationException("Invalid CNPJ");
        }

        if (property.getPhone() != null && !property.getPhone().isBlank()
                && !Validador.telefoneValido(property.getPhone())){
            throw new ValidationException("Invalid phone.");
        }
    }
}
