package com.bd.springweb.controller;

import com.bd.springweb.dao.PedidoDAO;
import com.bd.springweb.model.Pedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
public class PedidoController {

        private final PedidoDAO pedidoDAO;

        @Autowired
        public PedidoController(PedidoDAO pedidoDAO) {
            this.pedidoDAO = pedidoDAO;
        }

        @PostMapping("/api/pedidos")
        public ResponseEntity<Void> criarPedido(@RequestBody Pedido pedido) {
            try {
                pedidoDAO.create(pedido);
                return ResponseEntity.status(HttpStatus.CREATED).build();
            } catch (SQLException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }

        @GetMapping("/api/pedidos/{id}")
        public ResponseEntity<Pedido> buscarPedido(@PathVariable Integer id) {
            try {
                Pedido pedido = pedidoDAO.read(id);
                return pedido != null ? ResponseEntity.ok(pedido) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            } catch (SQLException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }

        @PutMapping("/api/pedidos/{id}")
        public ResponseEntity<Void> atualizarPedido(@PathVariable Integer id, @RequestBody Pedido pedido) {
            try {
                pedido.setId(id);
                pedidoDAO.update(pedido);
                return ResponseEntity.ok().build();
            } catch (SQLException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }

}

