package com.bd.springweb.controller;

import com.bd.springweb.dao.PedidoDAO;
import com.bd.springweb.model.Pedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
public class PedidoController {

        private final PedidoDAO pedidoDAO;

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


        @GetMapping("/api/pedidos/cliente/{cpf}")
        public ResponseEntity<List<Pedido>> listarPedidosPorCliente(@PathVariable String cpf) {
            try {
                List<Pedido> pedidos = pedidoDAO.listarPorCliente(cpf);
                return pedidos.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(pedidos);
            } catch (SQLException e) {
                e.printStackTrace();  // Log da exceção
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
        @GetMapping("/api/pedidos/loja/{cnpj}")
        public ResponseEntity<List<Pedido>> listarPedidosPorLoja(@PathVariable String cnpj) {
            try {
                List<Pedido> pedidos = pedidoDAO.listarPorLoja(cnpj);
                return pedidos.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(pedidos);
            } catch (SQLException e) {
                e.printStackTrace();  // Log da exceção
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }



}

