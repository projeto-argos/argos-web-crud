package br.com.argos.interfaces;

import br.com.argos.model.Lot;

import java.util.List;
import java.util.UUID;

public interface ILotDAO extends GenericDAO<Lot, UUID> {

    /**
     * Busca um lote ativo pelo código e pelo rebanho associado.
     *
     * @param lotCode código do lote
     * @param idHerd identificador do rebanho
     * @return lote encontrado ou null caso não exista
     */
    Lot findByLotCodeAndHerd(String lotCode, UUID idHerd);

    /**
     * Busca os lotes ativos associados ao rebanho informado.
     *
     * @param idHerd identificador do rebanho
     * @return lista dos lotes encontrados
     */
    List<Lot> findByHerd(UUID idHerd);

    /**
     * Busca os lotes ativos pertencentes à categoria informada.
     *
     * @param category categoria dos lotes
     * @return lista dos lotes encontrados
     */
    List<Lot> findByCategory(String category);

    /**
     * Desativa os lotes ativos associados ao rebanho informado.
     *
     * @param idHerd identificador do rebanho
     */
    void deleteByHerd(UUID idHerd);
}
