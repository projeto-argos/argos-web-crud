package br.com.argos.interfaces;

import br.com.argos.exceptions.DataAccessException;
import java.util.List;

    /**
     * Interface genérica que define as operações básicas de acesso
     * e manipulação de entidades no banco de dados.
     *
     * @param <T> tipo da entidade que será manipulada
     * @param <ID> tipo do identificador da entidade
     */
public interface GenericDAO<T, ID> {

    /**
     * Insere uma nova entidade no banco de dados.
     *
     * @param entity entidade que será inserida
     * @throws DataAccessException se ocorrer algum erro ao acessar o banco de dados.
     */
     void insert(T entity);

    /**
     * Atualiza uma entidade já existente no banco de dados.
     *
     * @param entity entidade que será atualizada.
     * @throws DataAccessException se ocorrer algum erro ao acessar o banco de dados.
     */
    void update(T entity);

    /**
     * Deleta uma entidade pelo seu identificador.
     *
     * @param id identificador da entidade
     * @throws DataAccessException se ocorrer algum erro ao acessar o banco de dados.
     */
    void delete(ID id);

    /**
     * Busca todas as entidades cadastradas.
     *
     * @return lista contendo todas as entidades encontradas.
     * @throws DataAccessException se ocorrer algum erro ao acessar o banco de dados.
     */
    List<T> findAll();

    /**
     * Busca uma entidade pelo seu identificador.
     *
     * @param id identificador da entidade
     * @return entity encontrada ou null caso não exista
     * @throws DataAccessException se ocorrer algum erro ao acessar o banco de dados.
     */
    T findById(ID id);

}
