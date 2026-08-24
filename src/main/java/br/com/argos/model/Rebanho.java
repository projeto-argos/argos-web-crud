package br.com.argos.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Rebanho {

    // ATRIBUTOS
    private UUID idRebanho;
    private String raca;
    private String finalidade;
    private String nome;
    private LocalDateTime atualizadoEm;
    private UUID idPropriedade;
    private boolean ativo;
    private int qtdCabecas;

    // METODOS CONSTRUTORES
    public Rebanho() {
    }

    public Rebanho(UUID idRebanho, String raca, String finalidade, String nome, LocalDateTime atualizadoEm, UUID idPropriedade, boolean ativo, int qtdCabecas) {
        this.idRebanho = idRebanho;
        this.raca = raca;
        this.finalidade = finalidade;
        this.nome = nome;
        this.atualizadoEm = atualizadoEm;
        this.idPropriedade = idPropriedade;
        this.ativo = ativo;
        this.qtdCabecas = qtdCabecas;

    }

    // METODOS GETTERS
    public UUID getIdRebanho() {
        return idRebanho;
    }

    public String getRaca() {
        return raca;
    }

    public String getFinalidade() {
        return finalidade;
    }

    public String getNome() {
        return nome;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public UUID getIdPropriedade() {
        return idPropriedade;
    }

    public boolean getAtivo() {
        return ativo;
    }

    public int getQtdCabecas() {
        return qtdCabecas;
    }

    // METODOS SETTERS
    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setFinalidade(String finalidade) {
        this.finalidade = finalidade;
    }

    public void setRaca(String raca) {
        this.raca = raca;
    }

    // SAIDA FORMATADA
    @Override
    public String toString(){
        return "ID do Rebanho: " + this.idRebanho +
                "\nRaça: " + this.raca +
                "\nFinalidade: " + this.finalidade +
                "\nNome: " + this.nome +
                "\nAtualizado Em: " + this.atualizadoEm +
                "\nID da Propriedade: " + this.idPropriedade;
    }
}
