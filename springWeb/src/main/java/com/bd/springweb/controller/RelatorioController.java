package com.bd.springweb.controller;


import com.bd.springweb.dao.PgRelatorioDAO;
import com.bd.springweb.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.sql.SQLException;

import java.util.List;

@RestController
public class RelatorioController {
    private final PgRelatorioDAO pgRelatorioDAO;


    public RelatorioController(PgRelatorioDAO pgRelatorioDAO) {
        this.pgRelatorioDAO = pgRelatorioDAO;
    }

    @GetMapping("/api/relatorios/vendas-por-loja")
    public ResponseEntity<List<TotalVendasPorLojaDTO>> getTotalVendasPorLoja(
            @RequestParam String dataInicio,
            @RequestParam String dataFim) {
        try {
            Date dataInicioDate = Date.valueOf(dataInicio);
            Date dataFimDate = Date.valueOf(dataFim);

            List<TotalVendasPorLojaDTO> resultado = pgRelatorioDAO.getTotalVendasPorLoja(dataInicioDate, dataFimDate);
            return ResponseEntity.ok(resultado);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }

    }
    @GetMapping("/api/relatorios/vendas-por-mes")
    public ResponseEntity<List<VendasPorMesDTO>> getVendasPorMes(@RequestParam String cnpj) {
        try {
            List<VendasPorMesDTO> resultado = pgRelatorioDAO.getVendasPorMes(cnpj);
            return ResponseEntity.ok(resultado);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
    @GetMapping("/api/relatorios/ticket-medio-por-mes")
    public ResponseEntity<List<TicketMedioPorMesDTO>> getTicketMedioPorMes(@RequestParam String cnpj) {
        try {
            List<TicketMedioPorMesDTO> resultado = pgRelatorioDAO.getTicketMedioPorMes(cnpj);
            return ResponseEntity.ok(resultado);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();

        }
    }
    @GetMapping("/api/relatorios/ticket-medio-por-loja")
    public ResponseEntity<List<TicketMedioPorLojaDTO>> getTicketMedioPorLoja() {
        try {
            List<TicketMedioPorLojaDTO> resultado = pgRelatorioDAO.getTicketMedioPorLoja();
            return ResponseEntity.ok(resultado);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
    @GetMapping("/api/relatorios/top-clientes")
    public ResponseEntity<List<GastosClienteDTO>> getClientesQueMaisGastam() {
        try {
            List<GastosClienteDTO> resultado = pgRelatorioDAO.getClientesQueMaisGastam();
            return ResponseEntity.ok(resultado);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
    @GetMapping("/api/relatorios/produto-preferido")
    public ResponseEntity<ProdutoPreferidoDTO> getProdutoPreferidoPorCliente(@RequestParam String cpf) {
        try {
            ProdutoPreferidoDTO resultado = pgRelatorioDAO.getProdutoPreferidoPorCliente(cpf);
            return ResponseEntity.ok(resultado);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
    @GetMapping("/api/relatorios/total-gasto-por-cliente")
    public ResponseEntity<TotalGastoClienteDTO> getTotalGastoPorCliente(@RequestParam String cpf) {
        try {
            TotalGastoClienteDTO resultado = pgRelatorioDAO.getTotalGastoPorCliente(cpf);
            return ResponseEntity.ok(resultado);
        } catch (SQLException e) {
            return ResponseEntity.status(500).build();  // Tratar erros de forma adequada
        }
    }

}
