package br.com.argos.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class AnimalExcecao {

    // ATRIBUTOS
    private UUID idAnimal;
    private double peso;
    private LocalDate dataNascimento;
    private String observacoes;
    private int brinco;
    private LocalDateTime atualizadoEm;
    private UUID idLote;
    private boolean ativo;

    // METODOS CONSTRUTORES
    public AnimalExcecao() {
    }

    public AnimalExcecao(UUID idAnimal, double peso, LocalDate dataNascimento, String observacoes, int brinco, LocalDateTime atualizadoEm, UUID idLote, boolean ativo) {
        this.idAnimal = idAnimal;
        this.peso = peso;
        this.dataNascimento = dataNascimento;
        this.observacoes = observacoes;
        this.brinco = brinco;
        this.atualizadoEm = atualizadoEm;
        this.idLote = idLote;
        this.ativo = ativo;
    }

    // METODOS GETTERS
    public UUID getIdAnimal() {
        return idAnimal;
    }

    public double getPeso() {
        return peso;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public int getBrinco() {
        return brinco;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public UUID getIdLote() {
        return idLote;
    }

    public boolean getAtivo() {
        return ativo;
    }

    // METODOS SETTERS


    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    // SAIDA FORMATADA
    @Override
    public String toString(){
        return "Id do Animal: " + this.idAnimal +
                "\nPeso: " + this.peso +
                "\nData de Nascimento: " + this.dataNascimento +
                "\nObservações: " + this.observacoes +
                "\nBrinco: " + this.brinco +
                "\nAtualizado Em: " + this.atualizadoEm +
                "\nId do Lote: " + this.idLote;
    }
}
