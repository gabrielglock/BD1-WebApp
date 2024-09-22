package com.bd.springweb.model;

import java.util.List;

public class Cliente {

    private String cpf;
    private String nome;
    private String contato;
    private List<EnderecoCliente> enderecosCliente;

    // Construtor vazio
    public Cliente() {}

    // Construtor completo
    public Cliente(String cpf, String nome, String contato) {
        this.cpf = cpf;
        this.nome = nome;
        this.contato = contato;
    }

    // Getters e Setters
    public String getCpf() {
        return cpf;
    }

    public List<EnderecoCliente> getEnderecosCliente() {
        return enderecosCliente;
    }

    public void setEnderecosCliente(List<EnderecoCliente> enderecosCliente) {
        this.enderecosCliente = enderecosCliente;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }


}
