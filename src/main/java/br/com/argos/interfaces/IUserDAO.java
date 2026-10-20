package br.com.argos.interfaces;

import br.com.argos.model.User;
import java.util.UUID;

public interface IUserDAO extends GenericDAO<User, UUID> {
    /**
     * Busca um usuário pelo endereço de e-mail.
     *
     * @param email endereço de e-mail usado na busca
     * @return usuário encontrado ou {@code null} se não houver correspondência
     */
    User findByEmail(String email);

    /**
     * Busca um usuário ativo pelo endereço de e-mail.
     *
     * @param email endereço de e-mail usado na busca
     * @return usuário encontrado com senha ou {@code null} se não houver correspondência
     */
    User findByEmailForAuthentication(String email);

    /**
     * Atualiza a senha do usuário identificado.
     *
     * @param id identificador do usuário
     * @param password nova senha
     */
    void updatePassword(UUID id, String password);

    /**
     * Desativa o usuário associado ao CPF informado.
     *
     * @param cpf CPF do usuário
     */
    void deleteByCpf(String cpf);
}
