package com.bd.springweb.dao;

import com.bd.springweb.model.Produto;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;
@Repository
public class PgProdutoDAO implements ProdutoDAO{


    private final DataSource dataSource;
    public PgProdutoDAO(DataSource dataSource) {this.dataSource = dataSource;}

    private static final String CREATE_PRODUTO_QUERY =
            "INSERT INTO webapp.produto (nome, preco, qtd_disponivel, descricao, loja_cnpj) VALUES (?, ?, ?, ?, ?)";

    private static final String LISTAR_TODOS_PRODUTOS_QUERY =
            "SELECT id, nome, descricao, preco FROM webapp.produto";

    private static final String BUSCAR_PRODUTO_POR_ID_QUERY =
            "SELECT id, nome, preco, qtd_disponivel, descricao, loja_cnpj FROM webapp.produto WHERE id = ?";
    @Override
    public void create(Produto produto) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(CREATE_PRODUTO_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, produto.getNome());
            statement.setDouble(2, produto.getPreco());
            statement.setInt(3, produto.getQtd_disponivel());
            statement.setString(4, produto.getDescricao());
            statement.setString(5, produto.getCnpj());


            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Falha ao criar produto, nenhuma linha foi inserida.");
            }

            try (ResultSet rs = statement.getGeneratedKeys()) {
                if (rs.next()) {
                    produto.setId(rs.getInt(1));
                } else {
                    throw new SQLException("Erro ao criar produto, ID não gerado.");
                }
            }
        }


    }

    @Override
    public Produto read(Integer id) throws SQLException {
        Produto produto = null;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(BUSCAR_PRODUTO_POR_ID_QUERY)) {

            statement.setInt(1, id);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    produto = new Produto();
                    produto.setId(rs.getInt("id"));
                    produto.setNome(rs.getString("nome"));
                    produto.setPreco(rs.getDouble("preco"));
                    produto.setQtd_disponivel(rs.getInt("qtd_disponivel"));
                    produto.setDescricao(rs.getString("descricao"));


                    produto.setCnpj(rs.getString("loja_cnpj")); //
                }
            }
        }

        return produto;
    }

    @Override
    public void update(Produto produto) throws SQLException {

    }

    @Override
    public void delete(Integer id) throws SQLException {

    }

    @Override
    public List<Produto> readAll() throws SQLException {
        return List.of();
    }
}
