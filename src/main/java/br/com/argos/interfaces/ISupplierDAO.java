//package br.com.argos.interfaces;
//
//import br.com.argos.model.Supplier;
//
//import java.util.List;
//import java.util.UUID;
//
//public interface ISupplierDAO extends GenericDAO<Supplier, UUID> {
//    /**
//     * Busca os fornecedores ativos cujo nome corresponde ao texto informado.
//     *
//     * @param name texto usado na busca pelo nome
//     * @return lista dos fornecedores encontrados
//     */
//    List<Supplier> findByName(String name);
//
//    /**
//     * Busca um fornecedor ativo pelo endereço de e-mail.
//     *
//     * @param email endereço de e-mail usado na busca
//     * @return fornecedor encontrado ou {@code null} se não houver correspondência
//     */
//    Supplier findByEmail(String email);
//
//    /**
//     * Desativa o fornecedor associado ao CNPJ informado.
//     *
//     * @param cnpj CNPJ do fornecedor
//     */
//    void deleteByCnpj(String cnpj);
//}
