package br.com.argos.interfaces;

import java.sql.SQLException;
import java.util.List;

    /**
     * Interface genérica que define as operações básicas de acesso
     * e manipulação de entidades no banco de dados.
     *
     * @param <T> tipo da entidade que será manipulada
     * @param <ID> tipo do identificador da entidade
     */
public interface GenericDAO <T, ID> {

    /**
     * Insere uma nova entidade no banco de dados.
     *
     * @param entity entidade que será inserida
     * @throws SQLException caso ocorra um erro durante a operação no banco
     */
     void insert(T entity) throws SQLException;

    /**
     * Atualiza uma entidade já existente no banco de dados.
     *
     * @param entity entidade que será atualizada.
     * @throws SQLException caso ocorra um erro durante a operação no banco
     */
    void update(T entity) throws SQLException;

    /**
     * Deleta uma entidade pelo seu identificador.
     *
     * @param id identificador da entidade
     * @return entity deletada ou null caso não exista
     * @throws SQLException caso ocorra um erro durante a operação no banco
     */
    void delete(ID id) throws SQLException;

    /**
     * Busca todas as entidades cadastradas.
     *
     * @return lista contendo todas as entidades encontradas.
     * @throws SQLException caso ocorra um erro durante a consulta no banco de dados.
     */
    List<T> findAll() throws SQLException;

    /**
     * Busca uma entidade pelo seu identificador.
     *
     * @param id identificador da entidade
     * @return entity encontrada ou null caso não exista
     * @throws SQLException caso ocorra um erro durante a consulta
     */
    T findById(ID id) throws SQLException;

}
