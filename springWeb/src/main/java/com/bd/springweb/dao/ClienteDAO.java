package com.bd.springweb.dao;

import com.bd.springweb.model.Cliente;
import com.bd.springweb.model.EnderecoCliente;
import com.bd.springweb.model.Loja;

import java.sql.SQLException;
import java.util.List;

public interface ClienteDAO extends DAO<Cliente> {
    Cliente getFromString(String id) throws SQLException;

    void deleteFromString(String id) throws SQLException;

    void addEnderecoCliente(EnderecoCliente enderecoCliente) throws SQLException;

    List<EnderecoCliente> listarEnderecoPorCliente(String cpfCliente) throws SQLException;
    Cliente listarClienteComEnderecos(String cpfCliente) throws SQLException;
}
