package com.bd.springweb.dto;

import java.sql.Date;

public class VendasPorMesDTO {
    private Date mes;
    private double totalVendas;

    public VendasPorMesDTO(Date mes, double totalVendas) {
        this.mes = mes;
        this.totalVendas = totalVendas;
    }

    public Date getMes() {
        return mes;
    }

    public void setMes(Date mes) {
        this.mes = mes;
    }

    public double getTotalVendas() {
        return totalVendas;
    }

    public void setTotalVendas(double totalVendas) {
        this.totalVendas = totalVendas;
    }
}

