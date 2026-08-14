package model;

import java.time.LocalDateTime;
import java.util.UUID;

public class EnderecoFornecedor {

    // ATRIBUTOS
    private int numero;
    private String observacoes;
    private LocalDateTime atualizadoEm;
    private String bairro;
    private String estado;
    private String cidade;
    private UUID idFornecedor;
    private boolean ativo;

    // METODOS CONSTRUTORES
    public EnderecoFornecedor() {
    }

    public EnderecoFornecedor(int numero, String observacoes, LocalDateTime atualizadoEm, String bairro, String estado, String cidade, UUID idFornecedor, boolean ativo) {
        this.numero = numero;
        this.observacoes = observacoes;
        this.atualizadoEm = atualizadoEm;
        this.bairro = bairro;
        this.estado = estado;
        this.cidade = cidade;
        this.idFornecedor = idFornecedor;
        this.ativo = ativo;
    }

    // METODOS GETTERS
    public int getNumero() {
        return numero;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public String getBairro() {
        return bairro;
    }

    public String getEstado() {
        return estado;
    }

    public String getCidade() {
        return cidade;
    }

    public UUID getIdFornecedor() {
        return idFornecedor;
    }

    public boolean getAtivo() {
        return ativo;
    }

    // METODOS SETTERS

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    // SAIDA FORMATADA
    @Override
    public String toString(){
        return "Número: " + this.numero +
                "\nObservações: " + this.observacoes +
                "\nAtualizado Em: " + this.atualizadoEm +
                "\nBairro: " + this.bairro +
                "\nEstado: " + this.estado +
                "\nCidade : " + this.cidade +
                "\nID do Fornecedor: " + this.idFornecedor;
    }
}
