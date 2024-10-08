package com.bd.springweb.dto;

public class GastosClienteDTO {
    private String cpf;
    private String nome;
    private double totalGasto;

    public GastosClienteDTO(String cpf, String nome, double totalGasto) {
        this.cpf = cpf;
        this.nome = nome;
        this.totalGasto = totalGasto;
    }


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