package com.bd.springweb.dto;

public class TotalGastoClienteDTO {
    private String cpf;
    private String nome;
    private double totalGasto;

    public TotalGastoClienteDTO(String cpf, String nome, double totalGasto) {
        this.cpf = cpf;
        this.nome = nome;
        this.totalGasto = totalGasto;
    }

    // Getters e Setters
    public String getCpf() {
        return cpf;
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

    public double getTotalGasto() {
        return totalGasto;
    }

    public void setTotalGasto(double totalGasto) {
        this.totalGasto = totalGasto;
    }
}
