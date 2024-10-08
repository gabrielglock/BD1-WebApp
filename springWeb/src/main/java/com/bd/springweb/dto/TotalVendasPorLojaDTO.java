package com.bd.springweb.dto;

public class TotalVendasPorLojaDTO {
    private String nome;




    private double totalVendas;

    public TotalVendasPorLojaDTO(String nome, double totalVendas) {
        this.nome = nome;
        this.totalVendas = totalVendas;


    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getTotalVendas() {
        return totalVendas;
    }

    public void setTotalVendas(double totalVendas) {
        this.totalVendas = totalVendas;
    }
}
