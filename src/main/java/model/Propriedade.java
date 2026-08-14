package model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Propriedade {

    // ATRIBUTOS
    private UUID idPropriedade;
    private LocalDateTime atualizadoEm;
    private String telefone;
    private String nome;
    private UUID idUsuario;
    private boolean ativo;

    // METODOS CONSTRUTORES
    public Propriedade() {
    }

    public Propriedade(UUID idPropriedade, LocalDateTime atualizadoEm, String telefone, String nome, UUID idUsuario, boolean ativo) {
        this.idPropriedade = idPropriedade;
        this.atualizadoEm = atualizadoEm;
        this.telefone = telefone;
        this.nome = nome;
        this.idUsuario = idUsuario;
        this.ativo = ativo;
    }


    // METODOS GETTERS
    public UUID getIdPropriedade() {
        return idPropriedade;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getNome() {
        return nome;
    }

    public UUID getIdUsuario() {
        return idUsuario;
    }

    public boolean getAtivo() {
        return ativo;
    }

    // METODOS SETTERS
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    // SAÍDA FORMATADA
    @Override
    public String toString() {
        return "ID da Propriedade: " + this.idPropriedade +
                "\nAtualizado em: " + this.atualizadoEm +
                "\nTelefone: " + this.telefone +
                "\nNome: " + this.nome +
                "\nID do Usuário: " + this.idUsuario;

    }

}