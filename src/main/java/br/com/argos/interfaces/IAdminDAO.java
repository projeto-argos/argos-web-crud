package br.com.argos.interfaces;

import br.com.argos.model.Admin;

import java.util.UUID;

public interface IAdminDAO extends GenericDAO<Admin, UUID> {
    Admin findByEmail(String email);
    void updatePassword(UUID id, String password);
    void deleteByCpf(String cpf);
}
