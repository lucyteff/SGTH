package com.sgth.model;

public class Usuario {
    private int id;
    private String nome;
    private String login;
    private String senha;
    private String papel; // ATENDENTE, ENFERMEIRO, MEDICO, ADMIN

    public Usuario() {}

    public Usuario(int id, String nome, String login, String senha, String papel) {
        this.id = id;
        this.nome = nome;
        this.login = login;
        this.senha = senha;
        this.papel = papel;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public String getPapel() { return papel; }
    public void setPapel(String papel) { this.papel = papel; }

    @Override
    public String toString() {
        return String.format("ID: %d | Nome: %s | Papel: %s", id, nome, papel);
    }
}