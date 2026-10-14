package br.com.argos.interfaces;

import br.com.argos.model.Herd;

import java.util.List;
import java.util.UUID;

public interface IHerdDAO extends GenericDAO<Herd, UUID> {
    /**
     * Busca os rebanhos ativos cujo nome corresponde ao texto informado.
     *
     * @param name texto usado na busca pelo nome
     * @return lista dos rebanhos encontrados
     */
    List<Herd> findByName(String name);

    /**
     * Busca os rebanhos ativos associados à propriedade informada.
     *
     * @param propertyId identificador da propriedade
     * @return lista dos rebanhos encontrados
     */
    List<Herd> findByProperty(UUID propertyId);

    /**
     * Desativa os rebanhos ativos associados à propriedade informada.
     *
     * @param propertyId identificador da propriedade
     * @return quantidade de registros afetados
     */
    int deleteByProperty(UUID propertyId);
}
