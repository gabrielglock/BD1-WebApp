package com.bd.springweb.dto;

public class TicketMedioPorLojaDTO {
    private String nome;
    private double ticketMedio;
    private String cnpj;



    public TicketMedioPorLojaDTO(String cnpj, String nome, double ticketMedio) {
        this.cnpj = cnpj;
        this.nome = nome;
        this.ticketMedio = ticketMedio;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getTicketMedio() {
        return ticketMedio;
    }

    public void setTicketMedio(double ticketMedio) {
        this.ticketMedio = ticketMedio;
    }
    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }
}
