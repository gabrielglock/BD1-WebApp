package com.bd.springweb.controller;

import com.bd.springweb.dao.PgClienteDAO;
import com.bd.springweb.model.Cliente;


import com.bd.springweb.model.EnderecoCliente;
import com.bd.springweb.model.Loja;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
public class ClienteController {
    private final PgClienteDAO pgClienteDAO;

    public ClienteController(PgClienteDAO pgClienteDAO) {this.pgClienteDAO = pgClienteDAO;}

//    @GetMapping("/api/clientes_daa/{cpf}")
//    public ResponseEntity<Cliente> getFromString(@PathVariable String cpf) {
//        try {
//            Cliente cliente = pgClienteDAO.getFromString(cpf);
//            return ResponseEntity.ok(cliente);
//        } catch (SQLException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
//    }


    @PutMapping("/api/clientes/{cpf}")
    public ResponseEntity<Void> updateCliente(@PathVariable String cpf, @RequestBody Cliente cliente) {
        try {
            cliente.setCpf(cpf);
            pgClienteDAO.update(cliente);
            return ResponseEntity.ok().build();
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

        @GetMapping("/api/clientes")
    public ResponseEntity<List<Cliente>> getAllClientes() {
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
    @PostMapping("/api/clientes/{cpf}/enderecos")
    public ResponseEntity<Void> adicionarEndereco(@PathVariable String cpf, @RequestBody EnderecoCliente endereco) {
        try {
            endereco.setCpf(cpf);
            pgClienteDAO.addEnderecoCliente(endereco);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/api/clientes/enderecos/{cpf}")
    public ResponseEntity<List<EnderecoCliente>> listarEnderecosPorCliente(@PathVariable String cpf) {
        try {
            List<EnderecoCliente> enderecos = pgClienteDAO.listarEnderecoPorCliente(cpf);
            return enderecos != null ? ResponseEntity.ok(enderecos) : ResponseEntity.notFound().build();
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/api/clientes/{cpf}")
    public ResponseEntity<Cliente> listarClienteComEnderecos(@PathVariable String cpf) {
        try {
            Cliente cliente = pgClienteDAO.listarClienteComEnderecos(cpf);
            return cliente != null ? ResponseEntity.ok(cliente) : ResponseEntity.notFound().build();
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
