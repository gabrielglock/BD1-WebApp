package com.bd.springweb.dto;

public class TicketMedioDTO {
    private double ticketMedio;

    public TicketMedioDTO(double ticketMedio) {
        this.ticketMedio = ticketMedio;
    }

    // Getters e Setters
    public double getTicketMedio() {
        return ticketMedio;
    }

    public void setTicketMedio(double ticketMedio) {
        this.ticketMedio = ticketMedio;
    }
}
