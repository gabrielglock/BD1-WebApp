package com.bd.springweb.controller;

import com.bd.springweb.dao.PgClienteDAO;
import com.bd.springweb.model.Cliente;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
public class ClienteController {
    private final PgClienteDAO pgClienteDAO;

    public ClienteController(PgClienteDAO pgClienteDAO) {this.pgClienteDAO = pgClienteDAO;}

    @GetMapping("/api/clientes/{cpf}")
    public ResponseEntity<Cliente> getFromString(@PathVariable String cpf) {
        try {
            Cliente cliente = pgClienteDAO.getFromString(cpf);
            return ResponseEntity.ok(cliente);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @GetMapping("/api/clientes")
    public ResponseEntity<List<Cliente>> getAllLojas() {
        try {
            List<Cliente> clientes = pgClienteDAO.readAll();
            return ResponseEntity.ok(clientes);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PostMapping("/api/clientes")
    public ResponseEntity<Void> createCliente(@RequestBody Cliente cliente) {
        try {
            pgClienteDAO.create(cliente);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    @DeleteMapping("/api/clientes/{cpf}")
    public ResponseEntity<Void> deleteCliente(@PathVariable String cpf) {
        try {
            pgClienteDAO.deleteFromString(cpf);
            return ResponseEntity.noContent().build();
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
