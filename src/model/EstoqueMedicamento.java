package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class EstoqueMedicamento {

    // ATRIBUTOS
    private UUID idEstoqueMedicamento;
    private LocalDate dataValidade;
    private String status;
    private String localArmazenamento;
    private LocalDate dataFabricacao;
    private String loteFabricacao;
    private LocalDate dataEntrega;
    private int qtdDisponivel;
    private LocalDateTime atualizadoEm;
    private UUID idMedicamento;

    // METODOS CONSTRUTORES
    public EstoqueMedicamento() {
    }

    public EstoqueMedicamento(UUID idEstoqueMedicamento, LocalDate dataValidade, String status, String localArmazenamento, LocalDate dataFabricacao, String loteFabricacao, LocalDate dataEntrega, int qtdDisponivel, LocalDateTime atualizadoEm, UUID idMedicamento) {
        this.idEstoqueMedicamento = idEstoqueMedicamento;
        this.dataValidade = dataValidade;
        this.status = status;
        this.localArmazenamento = localArmazenamento;
        this.dataFabricacao = dataFabricacao;
        this.loteFabricacao = loteFabricacao;
        this.dataEntrega = dataEntrega;
        this.qtdDisponivel = qtdDisponivel;
        this.atualizadoEm = atualizadoEm;
        this.idMedicamento = idMedicamento;
    }

    //  METODOS GETTERS
    public UUID getIdEstoqueMedicamento() {
        return idEstoqueMedicamento;
    }

    public LocalDate getDataValidade() {
        return dataValidade;
    }

    public String getStatus() {
        return status;
    }

    public String getLocalArmazenamento() {
        return localArmazenamento;
    }

    public LocalDate getDataFabricacao() {
        return dataFabricacao;
    }

    public String getLoteFabricacao() {
        return loteFabricacao;
    }

    public LocalDate getDataEntrega() {
        return dataEntrega;
    }

    public int getQtdDisponivel() {
        return qtdDisponivel;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public UUID getIdMedicamento() {
        return idMedicamento;
    }

    // METODOS SETTERS
    public void setQtdDisponivel(int qtdDisponivel) {
        this.qtdDisponivel = qtdDisponivel;
    }

    public void setLocalArmazenamento(String localArmazenamento) {
        this.localArmazenamento = localArmazenamento;
    }

    // SAIDA FORMATADA


    @Override
    public String toString() {
        return "ID do Estoque do Medicamento: " + this.idEstoqueMedicamento +
                "\nData de Validade: " + this.dataValidade +
                "\nStatus: " + this.status +
                "\nLocal de Armazenamento: " + this.localArmazenamento +
                "\nData de Fabricação: " + this.dataFabricacao +
                "\nLote de Fabricação: " + this.loteFabricacao +
                "\nData de Entrega: " + this.dataEntrega +
                "\nQuantidade Disponível: " + this.qtdDisponivel +
                "\nAtualizado Em: " + this.atualizadoEm +
                "\nID do Medicamento: " + this.idMedicamento;
    }
}
