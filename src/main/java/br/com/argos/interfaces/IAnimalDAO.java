package br.com.argos.interfaces;

import br.com.argos.model.Animal;

import java.util.List;
import java.util.UUID;

public interface IAnimalDAO extends GenericDAO<Animal, UUID> {
    /**
     * Busca um animal pela identificação da etiqueta auricular.
     *
     * @param earTag identificação da etiqueta auricular
     * @return animal encontrado ou {@code null} se não houver correspondência
     */
    Animal findByEarTag(String earTag);

    /**
     * Busca os animais ativos associados ao lote informado.
     *
     * @param idBatch identificador do lote
     * @return lista dos animais encontrados
     */
    List<Animal> findByBatch(UUID idBatch);

    /**
     * Desativa o animal associado à identificação da etiqueta auricular.
     *
     * @param earTag identificação da etiqueta auricular
     */
    void deleteByEarTag(String earTag);
}
