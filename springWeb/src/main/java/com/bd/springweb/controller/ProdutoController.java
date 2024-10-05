package com.bd.springweb.controller;

import com.bd.springweb.dao.PgProdutoDAO;
import com.bd.springweb.model.Pedido;
import com.bd.springweb.model.Produto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
public class ProdutoController {

    private final PgProdutoDAO pgProdutoDAO;

    public ProdutoController(PgProdutoDAO pgProdutoDAO) {
        this.pgProdutoDAO = pgProdutoDAO;
    }


    @PostMapping("/api/produtos")
    public ResponseEntity<Void> criarPedido(@RequestBody Produto produto) {
        try {
            pgProdutoDAO.create(produto);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        }
    }
    @GetMapping("/api/produtos/{id}")
    public ResponseEntity<Produto> buscarPedido(@PathVariable Integer id) {
        try {
            Produto produto = pgProdutoDAO.read(id);
            return produto != null ? ResponseEntity.ok(produto) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}