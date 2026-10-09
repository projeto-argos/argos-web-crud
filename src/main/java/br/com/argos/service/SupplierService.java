package br.com.argos.service;

import br.com.argos.dao.SupplierDAO;
import br.com.argos.exceptions.RequiredFieldException;
import br.com.argos.exceptions.ValidationException;
import br.com.argos.model.Supplier;
import br.com.argos.util.Normalizer;
import br.com.argos.util.Validador;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static br.com.argos.util.Normalizer.*;

/** Regras de negócio e validações relacionadas a fornecedores. */
public class SupplierService {

    private final SupplierDAO supplierDAO;

    public SupplierService() {
        this(new SupplierDAO());
    }

    public SupplierService(SupplierDAO supplierDAO) {
        this.supplierDAO = Objects.requireNonNull(supplierDAO, "SupplierDAO cannot be null");
    }

    public void create(Supplier supplier) {
        Supplier normalized = normalizeSupplier(supplier);
        validateSupplier(normalized);
        supplierDAO.insert(normalized);
    }

    public Supplier findById(UUID id) {
        if (id == null) {
            throw new RequiredFieldException("id");
        }
        return supplierDAO.findById(id);
    }

    public List<Supplier> findAll() {
        return supplierDAO.findAll();
    }

    public List<Supplier> findByName(String name) {
        if (name == null || name.isBlank()) {
            throw new RequiredFieldException("name");
        }
        return supplierDAO.findByName(name.strip());
    }

    public Supplier findByEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new RequiredFieldException("email");
        }
        return supplierDAO.findByEmail(Normalizer.email(email));
    }

    public void update(Supplier supplier) {
        Supplier normalized = normalizeSupplier(supplier);
        if (normalized.getIdSupplier() == null) {
            throw new RequiredFieldException("id");
        }
        validateSupplier(normalized);
        supplierDAO.update(normalized);
    }

    public void delete(UUID id) {
        if (id == null) {
            throw new RequiredFieldException("id");
        }
        supplierDAO.delete(id);
    }

    public void deleteByCnpj(String cnpj) {
        String digits = onlyDigits(cnpj);
        if (digits == null) {
            throw new RequiredFieldException("cnpj");
        }
        if (!Validador.cnpjValido(digits)) {
            throw new ValidationException("Invalid CNPJ");
        }
        supplierDAO.deleteByCnpj(digits);
    }

    /** Limpa os dados: texto vazio vira null, CNPJ e telefone ficam só com dígitos. */
    private Supplier normalizeSupplier(Supplier supplier) {
        if (supplier == null) {
            throw new RequiredFieldException("supplier");
        }

        return new Supplier(
                supplier.getIdSupplier(),
                supplier.getAddressId(),
                text(supplier.getFullName()),
                onlyDigits(supplier.getCnpj()),
                onlyDigits(supplier.getPhone()),
                Normalizer.email(supplier.getEmail()),
                supplier.getUpdatedAt(),
                supplier.isActive()
        );
    }

    private void validateSupplier(Supplier supplier) {

        // OBRIGATÓRIOS
        if (supplier.getFullName() == null) {
            throw new RequiredFieldException("name");
        }

        if (supplier.getFullName().length() > 120) {
            throw new ValidationException("Name cannot exceed 120 characters");
        }

        if (supplier.getCnpj() == null) {
            throw new RequiredFieldException("cnpj");
        }

        if (!Validador.cnpjValido(supplier.getCnpj())) {
            throw new ValidationException("Invalid CNPJ");
        }

        // OPCIONAIS
        if (supplier.getPhone() != null && !Validador.telefoneValido(supplier.getPhone())) {
            throw new ValidationException("Invalid phone");
        }

        if (supplier.getEmail() != null) {
            if (supplier.getEmail().length() > 120) {
                throw new ValidationException("Email cannot exceed 120 characters");
            }
            if (!Validador.emailValido(supplier.getEmail())) {
                throw new ValidationException("Invalid email");
            }
        }
    }
}