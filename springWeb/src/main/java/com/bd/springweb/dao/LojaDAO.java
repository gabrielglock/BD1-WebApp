package com.bd.springweb.dao;

import com.bd.springweb.model.Loja;

import java.sql.SQLException;

public interface LojaDAO extends DAO<Loja> {
    public Loja getFromString(String id) throws SQLException;

    public void deleteFromString(String id) throws SQLException;
}
