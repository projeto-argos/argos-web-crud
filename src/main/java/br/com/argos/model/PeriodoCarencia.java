package br.com.argos.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class PeriodoCarencia {

    // ATRIBUTOS
    private UUID idCarencia;
    private boolean liberadoParaAbate;
    private LocalDate dataFimCarencia;
    private LocalDateTime atualizadoEm;
    private UUID idAnimal;
    private boolean ativo;

    // METODOS CONSTRUTORES
    public PeriodoCarencia() {
    }

    public PeriodoCarencia(UUID idCarencia, boolean liberadoParaAbate, LocalDate dataFimCarencia, LocalDateTime atualizadoEm, UUID idAnimal, boolean ativo) {
        this.idCarencia = idCarencia;
        this.liberadoParaAbate = liberadoParaAbate;
        this.dataFimCarencia = dataFimCarencia;
        this.atualizadoEm = atualizadoEm;
        this.idAnimal = idAnimal;
        this.ativo = ativo;
    }

    // METODOS GETTERS

    public UUID getIdCarencia() {
        return idCarencia;
    }

    public boolean isLiberadoParaAbate() {
        return liberadoParaAbate;
    }

    public LocalDate getDataFimCarencia() {
        return dataFimCarencia;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public UUID getIdAnimal() {
        return idAnimal;
    }

    public boolean isAtivo() {
        return ativo;
    }

    // METODOS SETTERS

    // SAIDA FORMATADA
    @Override
    public String toString(){
        return "ID da Carência: " + this.idCarencia +
                "\nLiberado para Abate: " + this.liberadoParaAbate +
                "\nData do Fim da Carência: " + this.dataFimCarencia +
                "\nAtualizado Em: " + this.atualizadoEm +
                "\nID do Animal: " + this.idAnimal;
    }

}
