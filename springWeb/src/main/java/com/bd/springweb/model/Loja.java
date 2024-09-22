package com.bd.springweb.model;

public class Loja {


    private String cnpj;
    private String nome;
    private String contato;
    private String hr_funcionamento;
    private String endereco;
    private String email;

    public Loja() {
    }

    public Loja(String cnpj, String nome, String contato, String hr_funcionamento, String endereco, String email) {
        this.cnpj = cnpj;
        this.nome = nome;
        this.contato = contato;
        this.hr_funcionamento = hr_funcionamento;
        this.endereco = endereco;
        this.email = email;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getHr_funcionamento() {
        return hr_funcionamento;
    }

    public void setHr_funcionamento(String hr_funcionamento) {
        this.hr_funcionamento = hr_funcionamento;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
