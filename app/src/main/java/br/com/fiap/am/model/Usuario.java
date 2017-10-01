package br.com.fiap.am.model;

/**
 * Created by Washington on 01/10/2017.
 */

public class Usuario {
    private String nome;
    private String senha;
    private double cpf;

    public Usuario(String nome, String senha, double cpf) {
        this.nome = nome;
        this.senha = senha;
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public double getCpf() {
        return cpf;
    }

    public void setCpf(double cpf) {
        this.cpf = cpf;
    }
}
