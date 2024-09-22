package com.bd.springweb.dao;

import com.bd.springweb.model.Cliente;
import com.bd.springweb.model.Loja;
import com.bd.springweb.model.Pedido;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


@Repository
public class PgPedidoDAO implements PedidoDAO{

    private final DataSource dataSource;

    public PgPedidoDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    private static final String CREATE_QUERY =
            "INSERT INTO webapp.pedido (data, valor_total, cliente_cpf, loja_cnpj) " +
                    "VALUES (?, ?, ?, ?);";
    private static final String LISTAR_POR_CLIENTE_QUERY =
            "SELECT p.id, p.data, p.valor_total, c.cpf, c.nome, l.cnpj, l.nome AS loja_nome " +
                    "FROM webapp.pedido p " +
                    "JOIN webapp.cliente c ON p.cliente_cpf = c.cpf " +
                    "JOIN webapp.loja l ON p.loja_cnpj = l.cnpj " +
                    "WHERE c.cpf = ?";
    private static final String LISTAR_POR_LOJA_QUERY =
            "SELECT p.id, p.data, p.valor_total, c.cpf, c.nome, l.cnpj, l.nome AS loja_nome " +
                    "FROM webapp.pedido p " +
                    "JOIN webapp.cliente c ON p.cliente_cpf = c.cpf " +
                    "JOIN webapp.loja l ON p.loja_cnpj = l.cnpj " +
                    "WHERE l.cnpj = ?";

    private static final String READ_QUERY =
            "SELECT p.id, p.data, p.valor_total, c.cpf, c.nome, l.cnpj, l.nome AS loja_nome " +
                    "FROM webapp.pedido p " +
                    "JOIN webapp.cliente c ON p.cliente_cpf = c.cpf " +
                    "JOIN webapp.loja l ON p.loja_cnpj = l.cnpj " +
                    "WHERE p.id = ?;";

    private static final String UPDATE_QUERY =
            "UPDATE webapp.pedido " +
                    "SET data = ?, valor_total = ?, cliente_cpf = ?, loja_cnpj = ? " +
                    "WHERE id = ?;";

    private static final String DELETE_QUERY =
            "DELETE FROM webapp.pedido " +
                    "WHERE id = ?;";

    private static final String LIST_QUERY =
            "SELECT p.id, p.data, p.valor_total, c.cpf, c.nome, l.cnpj, l.nome AS loja_nome " +
                    "FROM webapp.pedido p " +
                    "JOIN webapp.cliente c ON p.cliente_cpf = c.cpf " +
                    "JOIN webapp.loja l ON p.loja_cnpj = l.cnpj;";

    @Override
    public void create(Pedido pedido) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(CREATE_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            statement.setDate(1, new java.sql.Date(pedido.getData().getTime()));
            statement.setDouble(2, pedido.getValorTotal());
            statement.setString(3, pedido.getCliente().getCpf());
            statement.setString(4, pedido.getLoja().getCnpj());
            statement.executeUpdate();

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Falha ao criar pedido, nenhuma linha foi inserida.");
            }

            // Obter o ID gerado
            try (ResultSet rs = statement.getGeneratedKeys()) {
                if (rs.next()) {
                    pedido.setId(rs.getInt(1));
                } else {
                    throw new SQLException("Erro ao criar pedido, ID não gerado.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Imprime a exceção para o log
            throw new SQLException("Erro ao criar pedido: " + e.getMessage());
        }
    }
    @Override
    public List<Pedido> listarPorCliente(String cpf) throws SQLException {
        List<Pedido> pedidos = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(LISTAR_POR_CLIENTE_QUERY)) {

            statement.setString(1, cpf);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Cliente cliente = new Cliente();
                    cliente.setCpf(rs.getString("cpf"));
                    cliente.setNome(rs.getString("nome"));
                    Loja loja = new Loja();
                    loja.setCnpj(rs.getString("cnpj"));
                    loja.setNome(rs.getString("loja_nome"));

                    Pedido pedido = new Pedido();
                    pedido.setId(rs.getInt("id"));
                    pedido.setData(rs.getDate("data"));
                    pedido.setValorTotal(rs.getDouble("valor_total"));
                    pedido.setCliente(cliente);
                    pedido.setLoja(loja);

                    pedidos.add(pedido);
                }
            }
        }

        return pedidos;
    }



    @Override
    public Pedido read(Integer id) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(READ_QUERY)) {

            statement.setLong(1, id);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    Cliente cliente = new Cliente();
                    cliente.setCpf(rs.getString("cpf"));
                    cliente.setNome(rs.getString("nome"));

                    Loja loja = new Loja();
                    loja.setCnpj(rs.getString("cnpj"));
                    loja.setNome(rs.getString("loja_nome"));

                    Pedido pedido = new Pedido();
                    pedido.setId(rs.getInt("id"));
                    pedido.setData(rs.getDate("data"));
                    pedido.setValorTotal(rs.getDouble("valor_total"));
                    pedido.setCliente(cliente);
                    pedido.setLoja(loja);

                    return pedido;
                }
            }
        }
        return null;
    }

    @Override
    public void update(Pedido pedido) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {

            statement.setDate(1, new java.sql.Date(pedido.getData().getTime()));
            statement.setDouble(2, pedido.getValorTotal());
            statement.setString(3, pedido.getCliente().getCpf());
            statement.setString(4, pedido.getLoja().getCnpj());
            statement.setLong(5, pedido.getId());

            statement.executeUpdate();
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {

            statement.setLong(1, id);
            statement.executeUpdate();
        }
    }

    @Override
    public List<Pedido> readAll() throws SQLException {
        List<Pedido> pedidos = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(LIST_QUERY);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setCpf(rs.getString("cpf"));
                cliente.setNome(rs.getString("nome"));

                Loja loja = new Loja();
                loja.setCnpj(rs.getString("cnpj"));
                loja.setNome(rs.getString("loja_nome"));

                Pedido pedido = new Pedido();
                pedido.setId(rs.getInt("id"));
                pedido.setData(rs.getDate("data"));
                pedido.setValorTotal(rs.getDouble("valor_total"));
                pedido.setCliente(cliente);
                pedido.setLoja(loja);

                pedidos.add(pedido);
            }
        }
        return pedidos;
    }
    @Override
    public List<Pedido> listarPorLoja(String lojaCnpj) throws SQLException {
        List<Pedido> pedidos = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(LISTAR_POR_LOJA_QUERY)) {

            statement.setString(1, lojaCnpj);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Cliente cliente = new Cliente();
                    cliente.setCpf(rs.getString("cpf"));
                    cliente.setNome(rs.getString("nome"));
                    Loja loja = new Loja();
                    loja.setCnpj(rs.getString("cnpj"));
                    loja.setNome(rs.getString("loja_nome"));

                    Pedido pedido = new Pedido();
                    pedido.setId(rs.getInt("id"));
                    pedido.setData(rs.getDate("data"));
                    pedido.setValorTotal(rs.getDouble("valor_total"));
                    pedido.setCliente(cliente);
                    pedido.setLoja(loja);

                    pedidos.add(pedido);
                }
            }
        }

        return pedidos;
    }
}
