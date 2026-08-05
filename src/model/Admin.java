package model;

public class Admin {

    // ATRIBUTOS
    private String cpf;
    private String nome;
    private String email;
    private String telefone;

    // METODOS CONSTRUTORES

    public Admin() {
    }
    public Admin(String cpf, String nome, String email, String telefone) {
        this.cpf = cpf;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
    }

    // METODOS GETTERS
    public String getCpf() {
        return cpf;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefone() {
        return telefone;
    }

    // METODOS SETTERS

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    // SAIDA FORMATADA
    @Override
    public String toString(){
        return "CPF: " + this.cpf +
                "\nNome: " + this.nome +
                "\nE-mail: " + this.email +
                "\nTelefone: " + this.telefone;
    }
}
