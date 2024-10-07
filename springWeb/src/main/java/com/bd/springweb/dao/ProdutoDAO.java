package com.bd.springweb.dao;

import com.bd.springweb.model.Produto;

public interface ProdutoDAO extends DAO<Produto> {

    boolean hasStock(Integer idProduto, Integer quantidade);
    void updateStock(Integer idProduto, Integer quantidade);
}
