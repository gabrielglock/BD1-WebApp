package com.bd.springweb.dao;

import com.bd.springweb.model.Cliente;
import com.bd.springweb.model.Loja;

import java.sql.SQLException;

public interface ClienteDAO extends DAO<Cliente> {
    public Cliente getFromString(String id) throws SQLException;

    public void deleteFromString(String id) throws SQLException;
}
