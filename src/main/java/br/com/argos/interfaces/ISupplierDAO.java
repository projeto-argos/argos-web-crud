package br.com.argos.interfaces;

import br.com.argos.model.Supplier;

import java.util.List;

public interface ISupplierDAO {
    List<Supplier> findByName(String name);
    Supplier findByEmail(String email);
    void deleteByCnpj(String cnpj);
}