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

public class SupplierService {

    private final SupplierDAO supplierDAO;

    public SupplierService() {
        this(new SupplierDAO());
    }

    public SupplierService(SupplierDAO supplierDAO){
        this.supplierDAO = Objects.requireNonNull(supplierDAO, "SupplierDAO cannot be null");
    }

    public void create(Supplier supplier){
        Supplier normalized = normalizedSupplier(supplier);
        validateSupplier(normalized);
        supplierDAO.insert(normalized);
    }

    public Supplier findById(UUID id){
        if (id == null) {
            throw new RequiredFieldException("id");
        }
        return supplierDAO.findById(id);
    }

    public List<Supplier> findAll(){
        return supplierDAO.findAll();
    }

    public List<Supplier> findByName(String name){
        if (name == null || name.isEmpty()) {
            throw new RequiredFieldException("name");
        }
        return supplierDAO.findByName(name.strip());
    }

    public Supplier findByEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new RequiredFieldException("email");
        }
        return supplierDAO.findByEmail(Normalizer.email(email));
    }

    public void update(Supplier supplier){
        Supplier normalized = normalizedSupplier(supplier);
        if (normalized.getIdSupplier() == null){
            throw new RequiredFieldException("id");
        }
        validateSupplier(normalized);
        supplierDAO.update(normalized);
    }

    public void delete(UUID id){
        if (id == null) {
            throw new RequiredFieldException("id");
        }
        supplierDAO.delete(id);
    }

//    fazer algum metédo para deletar

    private Supplier normalizedSupplier(Supplier supplier){
        if (supplier == null) {
            throw new RequiredFieldException("Supplier cannot be null.");
        }

        return new Supplier(
                supplier.getIdSupplier(),
                supplier.getAddressId(),
                text(supplier.getFullName()),
                onlyDigits(supplier.getCnpj()),
                onlyDigits(supplier.getPhone()),
                email(supplier.getEmail())
        );
    }
    private void validateSupplier(Supplier supplier){

//        OBRIGATÓRIOS
        if (supplier == null){
            throw new RequiredFieldException("supplier");
        }

        if (supplier.getFullName() == null || supplier.getFullName().isEmpty()){
            throw new RequiredFieldException("name");
        }

        if (supplier.getCnpj() == null || supplier.getCnpj().isEmpty()){
            throw new RequiredFieldException("cnpj");
        }

        if (!Validador.cnpjValido(supplier.getCnpj())){
            throw new ValidationException("Invalid CNPJ");
        }


//        OPCIONAIS
        if (supplier.getPhone() != null || !supplier.getPhone().isBlank()){
            throw new ValidationException("phone");
        }

        if (supplier.getEmail() != null || !supplier.getEmail().isBlank()){
            throw new ValidationException("email");
        }

        if (supplier.getAddressId() != null){
                throw new ValidationException("id_address");
        }

    }
}
