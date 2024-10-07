package com.bd.springweb.controller;

import com.bd.springweb.dao.PgPedidoDAO;
import com.bd.springweb.model.Pedido;
import com.bd.springweb.model.PedidoProduto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;



@RestController
public class PedidoController{

    public PedidoController(PgPedidoDAO pgPedidoDAO) {
        this.pgPedidoDAO = pgPedidoDAO;
    }

    private final PgPedidoDAO pgPedidoDAO;


//        @PostMapping("/api/pedidos")
//        public ResponseEntity<Void> criarPedido(@RequestBody Pedido pedido) {
//            try {
//                pgPedidoDAO.create(pedido);
//                return ResponseEntity.status(HttpStatus.CREATED).build();
//            } catch (SQLException e) {
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//            }
//        }

        @GetMapping("/api/pedidos/{id}")
        public ResponseEntity<Pedido> buscarPedido(@PathVariable Integer id) {
            try {
                Pedido pedido = pgPedidoDAO.read(id);
                return pedido != null ? ResponseEntity.ok(pedido) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            } catch (SQLException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }


        @GetMapping("/api/pedidos/cliente/{cpf}")
        public ResponseEntity<List<Pedido>> listarPedidosPorCliente(@PathVariable String cpf) {
            try {
                List<Pedido> pedidos = pgPedidoDAO.listarPorCliente(cpf);
                return pedidos.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(pedidos);
            } catch (SQLException e) {
                e.printStackTrace();  // Log da exceção
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
        @GetMapping("/api/pedidos/loja/{cnpj}")
        public ResponseEntity<List<Pedido>> listarPedidosPorLoja(@PathVariable String cnpj) {
            try {
                List<Pedido> pedidos = pgPedidoDAO.listarPorLoja(cnpj);
                return pedidos.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(pedidos);
            } catch (SQLException e) {
                e.printStackTrace();  // Log da exceção
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
        @PostMapping("/api/pedidos/checkout")
        public ResponseEntity<String> realizarCheckout(
            @RequestBody Pedido pedido ){
            String idCliente = pedido.getIdCliente();
            String idLoja = pedido.getIdLoja();
            List<PedidoProduto> produtos = pedido.getProdutos();


        boolean sucesso = pgPedidoDAO.realizarPedido(idCliente, idLoja, produtos);

        if (sucesso) {
            return ResponseEntity.ok("Pedido realizado com sucesso.");
        } else {

            return ResponseEntity.badRequest().body("Falha ao realizar pedido. Estoque insuficiente.");
        }
    }


}

