package model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Fornecedor {

    // ATRIBUTOS
    private UUID idFornecedor;
    private String cnpj;
    private  String nome;
    private String telefone;
    private String email;
    private LocalDateTime atualizadoEm;

    // METODOS CONSTRUTORES
    public Fornecedor() {
    }

    public Fornecedor(UUID idFornecedor, String cnpj, String nome, String telefone, String email, LocalDateTime atualizadoEm) {
        this.idFornecedor = idFornecedor;
        this.cnpj = cnpj;
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
        this.atualizadoEm = atualizadoEm;
    }

    // METODOS GETTERS
    public UUID getIdFornecedor() {
        return idFornecedor;
    }

    public String getCnpj() {
        return cnpj;
    }

    public String getNome() {
        return nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getEmail() {
        return email;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    // METODOS SETTERS

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    // SAIDA FORMATADA
    @Override
    public String toString(){
        return "ID do Fornecedor: " + this.idFornecedor +
                "\nCNPJ: " + this.cnpj +
                "\nNome: " + this.nome +
                "\nTelefone: " + this.telefone +
                "\nE-mail: " + this.email +
                "\nAtualizado Em: " + this.atualizadoEm;
    }
}
