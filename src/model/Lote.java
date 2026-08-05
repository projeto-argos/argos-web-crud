package model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Lote {

    // ATRIBUTOS
    private UUID idLote;
    private String categoria;
    private LocalDateTime atualizadoEm;
    private UUID idRebanho;

    // METODOS CONSTRUTORES


    public Lote() {
    }

    public Lote(UUID idLote, String categoria, LocalDateTime atualizadoEm, UUID idRebanho) {
        this.idLote = idLote;
        this.categoria = categoria;
        this.atualizadoEm = atualizadoEm;
        this.idRebanho = idRebanho;
    }

    // METODOS GETTERS
    public UUID getIdLote() {
        return idLote;
    }

    public String getCategoria() {
        return categoria;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public UUID getIdRebanho() {
        return idRebanho;
    }

    // METODOS SETTERS
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    // SAIDA FORMATADA
    @Override
    public String toString(){
        return "ID do Lote: " + this.idLote +
                "\nCategoria: " + this.categoria +
                "\nAtualizado Em: " + this.atualizadoEm +
                "\nId do Rebanho: " + this.idRebanho;
    }
}
