package br.com.argos.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class EnderecoPropriedade {

    // ATRIBUTOS
    private String bairro;
    private String observacoes;
    private int numero;
    private String cidade;
    private String estado;
    private LocalDateTime atualizadoEm;
    private UUID idPropriedade;
    private boolean ativo;

    // METODOS CONSTRUTORES
    public EnderecoPropriedade() {
    }

    public EnderecoPropriedade(String bairro, String observacoes, int numero, String cidade, String estado, LocalDateTime atualizadoEm, UUID idPropriedade, boolean ativo) {
        this.bairro = bairro;
        this.observacoes = observacoes;
        this.numero = numero;
        this.cidade = cidade;
        this.estado = estado;
        this.atualizadoEm = atualizadoEm;
        this.idPropriedade = idPropriedade;
        this.ativo = ativo;
    }

    // METODOS GETTERS
    public String getBairro() {
        return bairro;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public int getNumero() {
        return numero;
    }

    public String getCidade() {
        return cidade;
    }

    public String getEstado() {
        return estado;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public UUID getIdPropriedade() {
        return idPropriedade;
    }

    public boolean isAtivo() {
        return ativo;
    }

    // METODOS SETTERS
    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    // SAIDA FORMATADA
    @Override
    public String toString(){
        return "Bairro: " + this.bairro +
                "\nObservações: " + this.observacoes +
                "\nNúmero: " + this.numero +
                "\nCidade: " + this.cidade +
                "\nEstado: " + this.estado +
                "\nAtualizado Em: " + this.atualizadoEm +
                "\nID da Propriedade: " + this.idPropriedade;
    }
}
