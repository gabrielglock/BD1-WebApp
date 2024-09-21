package com.bd.springweb.controller;

import com.bd.springweb.dao.PgLojaDAO;
import com.bd.springweb.model.Loja;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
public class LojaController {

    private final PgLojaDAO pgLojaDAO;

    public LojaController(PgLojaDAO pgLojaDAO) {
        this.pgLojaDAO = pgLojaDAO;
    }
    @GetMapping("/api/lojas")
    public ResponseEntity<List<Loja>> getAllLojas() {
        try {
            List<Loja> lojas = pgLojaDAO.readAll();
            return ResponseEntity.ok(lojas);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/api/lojas/{cnpj}")
    public ResponseEntity<Loja> getFromString(@PathVariable String cnpj) {
        try {
            Loja loja = pgLojaDAO.getFromString(cnpj);
            return ResponseEntity.ok(loja);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @PostMapping("/api/lojas")
    public ResponseEntity<Void> createLoja(@RequestBody Loja loja) {
        try {
            pgLojaDAO.create(loja);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    @PutMapping("/api/lojas/{cnpj}")
    public ResponseEntity<Void> updateLoja(@PathVariable String cnpj, @RequestBody Loja loja) {
        try {
            loja.setCnpj(cnpj);
            pgLojaDAO.update(loja);
            return ResponseEntity.ok().build();
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    @DeleteMapping("/api/lojas/{cnpj}")
    public ResponseEntity<Void> deleteLoja(@PathVariable String cnpj) {
        try {
            pgLojaDAO.deleteFromString(cnpj);
            return ResponseEntity.noContent().build();
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
