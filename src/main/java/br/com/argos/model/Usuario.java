package br.com.argos.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Usuario {

    // ATRIBUTO
    private UUID idUsuario;
    private String telefone;
    private String nome;
    private String cpf;
    private String email;
    private String cnpj;
    private String cargo;
    private LocalDateTime atualizadoEm;
    private boolean ativo;

    // METODOS CONSTRUTORES
    public Usuario() {
    }

    public Usuario(UUID idUsuario, String telefone, String nome, String cpf, String email, String cnpj, String cargo, LocalDateTime atualizadoEm, boolean ativo) {
        this.idUsuario = idUsuario;
        this.telefone = telefone;
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.cnpj = cnpj;
        this.cargo = cargo;
        this.atualizadoEm = atualizadoEm;
        this.ativo = ativo;
    }

    // METODOS GETTERS
    public UUID getIdUsuario() {
        return idUsuario;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public String getEmail() {
        return email;
    }

    public String getCnpj() {
        return cnpj;
    }

    public String getCargo() {
        return cargo;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
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

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // SAIDA FORMATADA
    @Override
    public String toString(){
        return "ID do Usuário: " + this.idUsuario +
                "\nTelefone: " + this.telefone +
                "\nNome: " + this.nome +
                "\nCPF: " + this.cpf +
                "\nE-mail: " + this.email +
                "\nCNPJ: " + this.cnpj +
                "\nCargo: " + this.cargo +
                "\nAtualizado Em: " + this.atualizadoEm;
    }
}
