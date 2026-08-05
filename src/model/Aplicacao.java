package model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Aplicacao {

    // ATRIBUTOS
    private UUID idAplicacao;
    private double doseAplicada;
    private String membroAplicacao;
    private String objetivo;
    private LocalDateTime dataHora;
    private String localAplicacao;
    private String observacoes;
    private LocalDateTime atualizadoEm;
    private UUID idAnimal;
    private UUID idMedicamento;
    private UUID idEstoqueMedicamento;

    // METODOS CONSTRUTORES
    public Aplicacao() {
    }

    public Aplicacao(UUID idAplicacao, double doseAplicada, String membroAplicacao, String objetivo, LocalDateTime dataHora, String localAplicacao, String observacoes, LocalDateTime atualizadoEm, UUID idAnimal, UUID idMedicamento, UUID idEstoqueMedicamento) {
        this.idAplicacao = idAplicacao;
        this.doseAplicada = doseAplicada;
        this.membroAplicacao = membroAplicacao;
        this.objetivo = objetivo;
        this.dataHora = dataHora;
        this.localAplicacao = localAplicacao;
        this.observacoes = observacoes;
        this.atualizadoEm = atualizadoEm;
        this.idAnimal = idAnimal;
        this.idMedicamento = idMedicamento;
        this.idEstoqueMedicamento = idEstoqueMedicamento;
    }

    // METODOS GETTERS

    public UUID getIdAplicacao() {
        return idAplicacao;
    }

    public double getDoseAplicada() {
        return doseAplicada;
    }

    public String getMembroAplicacao() {
        return membroAplicacao;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public String getLocalAplicacao() {
        return localAplicacao;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public UUID getIdAnimal() {
        return idAnimal;
    }

    public UUID getIdMedicamento() {
        return idMedicamento;
    }

    public UUID getIdEstoqueMedicamento() {
        return idEstoqueMedicamento;
    }

    // METODOS SETTERS

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    // SAIDA FORMATADA
    @Override
    public String toString(){
        return "ID da Aplicação: " + this.idAplicacao +
                "\nDose Aplicada: " + this.doseAplicada +
                "\nMembro da Aplicação: " + this.membroAplicacao +
                "\nObjetivo: " + this.objetivo +
                "\nData e Hora: " + this.dataHora +
                "\nLocal da Aplicação: " + this.localAplicacao +
                "\nObservações: " + this.observacoes +
                "\nAtualizado Em: " + this.atualizadoEm +
                "\nID do Animal: " + this.idAnimal +
                "\nID do Medicamento: " + this.idMedicamento+
                "\nID do Estoque do Medicamento: " + this.idEstoqueMedicamento;
    }
}
