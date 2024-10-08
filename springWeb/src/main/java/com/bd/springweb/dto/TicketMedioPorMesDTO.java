package com.bd.springweb.dto;

import java.sql.Date;

public class TicketMedioPorMesDTO {
    private Date mes;
    private double ticketMedio;

    public TicketMedioPorMesDTO(Date mes, double ticketMedio) {
        this.mes = mes;
        this.ticketMedio = ticketMedio;
    }


    public Date getMes() {
        return mes;
    }

    public void setMes(Date mes) {
        this.mes = mes;
    }

    public double getTicketMedio() {
        return ticketMedio;
    }

    public void setTicketMedio(double ticketMedio) {
        this.ticketMedio = ticketMedio;
    }
}
