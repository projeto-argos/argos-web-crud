package br.com.argos.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Medicamento {

    // ATRIBUTOS
    private UUID idMedicamento;
    private String nome;
    private double doseMl;
    private String principioAtivo;
    private int carenciaIndicada;
    private String categoriaTerapeutica;
    private LocalDateTime atualizadoEm;
    private UUID idFornecedor;
    private boolean ativo;

    // METODOS CONSTRUTORES
    public Medicamento() {
    }

    public Medicamento(UUID idMedicamento, String nome, double doseMl, String principioAtivo, int carenciaIndicada, String categoriaTerapeutica, LocalDateTime atualizadoEm, UUID idFornecedor, boolean ativo) {
        this.idMedicamento = idMedicamento;
        this.nome = nome;
        this.doseMl = doseMl;
        this.principioAtivo = principioAtivo;
        this.carenciaIndicada = carenciaIndicada;
        this.categoriaTerapeutica = categoriaTerapeutica;
        this.atualizadoEm = atualizadoEm;
        this.idFornecedor = idFornecedor;
        this.ativo = ativo;
    }

    // METODOS GETTERS
    public UUID getIdMedicamento() {
        return idMedicamento;
    }

    public String getNome() {
        return nome;
    }

    public double getDoseMl() {
        return doseMl;
    }

    public String getPrincipioAtivo() {
        return principioAtivo;
    }

    public int getCarenciaIndicada() {
        return carenciaIndicada;
    }

    public String getCategoriaTerapeutica() {
        return categoriaTerapeutica;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public UUID getIdFornecedor() {
        return idFornecedor;
    }

    public boolean isAtivo() {
        return ativo;
    }

    // METODOS SETTERS

    // SAIDA FORMATADA
    @Override
    public String toString(){
        return "ID do Medicamento: " + this.idMedicamento +
                "\nNome: " + this.nome +
                "\nDose ML: " +this.doseMl +
                "\nPrincípio Ativo: " + this.principioAtivo +
                "\nCarência Indicada: " + this.carenciaIndicada +
                "\nCategoria Terapeutica: " + this.categoriaTerapeutica +
                "\nAtualizado Em: " + this.atualizadoEm +
                "\nID do Fornecedor: " + this.idFornecedor;
    }
}
