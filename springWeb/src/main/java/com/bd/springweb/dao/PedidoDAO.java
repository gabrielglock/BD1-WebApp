package com.bd.springweb.dao;

import com.bd.springweb.model.Cliente;
import com.bd.springweb.model.Loja;
import com.bd.springweb.model.Pedido;

import java.sql.SQLException;
import java.util.List;

public interface PedidoDAO extends DAO<Pedido>{
    public List<Pedido> listarPorCliente(String cpf) throws SQLException;
    public List<Pedido> listarPorLoja(String cnpj) throws SQLException;

}
