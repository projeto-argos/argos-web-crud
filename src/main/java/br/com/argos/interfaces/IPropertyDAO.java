package br.com.argos.interfaces;

import br.com.argos.model.Property;

import java.util.List;
import java.util.UUID;

public interface IPropertyDAO extends GenericDAO<Property, UUID> {
    /**
     * Busca as propriedades ativas cujo nome corresponde ao texto informado.
     *
     * @param name texto usado na busca pelo nome
     * @return lista das propriedades encontradas
     */
    List<Property> findByName(String name);

    /**
     * Busca as propriedades ativas associadas ao usuário informado.
     *
     * @param userId identificador do usuário
     * @return lista das propriedades encontradas
     */
    List<Property> findByUser(UUID userId);

    /**
     * Desativa a propriedade ativa que usa o endereço informado.
     * Cada propriedade tem um endereço só, então apaga no máximo uma.
     *
     * @param addressId identificador do endereço da propriedade
     */
    void deleteByAddress(UUID addressId);

    /**
     * Remove os vínculos entre o usuário informado e as propriedades (tabela user_property).
     *
     * @param userId identificador do usuário
     * @return quantidade de registros afetados
     */
    int removeUserLinks(UUID userId);
}
