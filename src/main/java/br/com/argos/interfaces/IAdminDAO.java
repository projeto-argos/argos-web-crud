package br.com.argos.interfaces;

import br.com.argos.model.Admin;

import java.util.UUID;

public interface IAdminDAO extends GenericDAO<Admin, UUID> {
    /**
     * Busca um administrador pelo endereço de e-mail.
     *
     * @param email endereço de e-mail usado na busca
     * @return administrador encontrado ou {@code null} se não houver correspondência
     */
    Admin findByEmail(String email);

    /**
     * Atualiza a senha do administrador identificado.
     *
     * @param id identificador do administrador
     * @param password nova senha
     */
    void updatePassword(UUID id, String password);

    /**
     * Desativa o administrador associado ao CPF informado.
     *
     * @param cpf CPF do administrador
     */
    void deleteByCpf(String cpf);
}
