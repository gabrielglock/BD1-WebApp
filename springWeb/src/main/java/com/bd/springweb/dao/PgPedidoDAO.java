package com.bd.springweb.dao;

import com.bd.springweb.model.Cliente;
import com.bd.springweb.model.Loja;
import com.bd.springweb.model.Pedido;
import com.bd.springweb.model.PedidoProduto;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


@Repository
public class PgPedidoDAO implements PedidoDAO{

    private final DataSource dataSource;
    private final PgProdutoDAO pgProdutoDAO;

    public PgPedidoDAO(DataSource dataSource) {
        this.dataSource = dataSource;
        this.pgProdutoDAO = new PgProdutoDAO(dataSource);
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
        /*try (Connection connection = dataSource.getConnection();
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
        }*/
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
                    pedido.setIdCliente(cliente.getCpf());
                    pedido.setIdLoja(loja.getCnpj());
                    pedido.setProdutos(buscarProdutosDoPedido(pedido.getId()));
                    pedidos.add(pedido);
                }
            }
        }

        return pedidos;
    }
    private List<PedidoProduto> buscarProdutosDoPedido(int pedidoId) throws SQLException {
        List<PedidoProduto> produtos = new ArrayList<>();

        String query = "SELECT pp.produto_id, prod.nome, pp.quantidade, pp.preco " +
                "FROM webapp.pedido_produto pp " +
                "JOIN webapp. produto prod ON pp.produto_id = prod.id " +
                "WHERE pp.pedido_id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, pedidoId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    PedidoProduto produto = new PedidoProduto();
                    produto.setIdPedido(pedidoId);
                    produto.setIdProduto(rs.getInt("produto_id"));

                    produto.setQuantidade(rs.getInt("quantidade"));
                    produto.setPreco(rs.getDouble("preco"));

                    produtos.add(produto);
                }
            }
        }

        return produtos;
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
                    pedido.setIdCliente(cliente.getCpf());
                    pedido.setIdLoja(loja.getCnpj());

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
            statement.setString(3, pedido.getIdCliente());
            statement.setString(4, pedido.getIdLoja());
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
                pedido.setIdCliente(cliente.getCpf());
                pedido.setIdLoja(loja.getCnpj());

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
                    pedido.setIdCliente(cliente.getCpf());
                    pedido.setIdLoja(loja.getCnpj());
                    pedido.setProdutos(buscarProdutosDoPedido(pedido.getId()));

                    pedidos.add(pedido);
                }
            }
        }

        return pedidos;
    }

    @Override
    public boolean realizarPedido(String idCliente, String idLoja, List<PedidoProduto> produtos) {
        // Iniciar uma transação para garantir que tudo seja executado corretamente
        try {Connection connection = dataSource.getConnection();
            connection.setAutoCommit(false);

            // Verificar o estoque de cada produto
            for (PedidoProduto produtoPedido : produtos) {
                if (!pgProdutoDAO.hasStock(produtoPedido.getIdProduto(), produtoPedido.getQuantidade())) {
                    connection.rollback();
                    return false;  // Se algum produto não tiver estoque, cancelar o pedido
                }
            }

            // Se tudo ok, inserir o pedido e os produtos
            String sqlPedido = "INSERT INTO webapp.pedido (cliente_cpf, loja_cnpj, data, valor_total) VALUES (?, ?, NOW(), ?) RETURNING id";
            try (PreparedStatement stmtPedido = connection.prepareStatement(sqlPedido)) {
                stmtPedido.setString(1, idCliente);
                stmtPedido.setString(2, idLoja);
                stmtPedido.setDouble(3, calcularValorTotal(produtos));

                ResultSet rs = stmtPedido.executeQuery();
                int idPedido = 0;
                if (rs.next()) {
                    idPedido = rs.getInt("id");
                }

                // Inserir os produtos no pedido e atualizar o estoque
                for (PedidoProduto produtoPedido : produtos) {
                    String sqlProduto = "INSERT INTO webapp.pedido_produto (pedido_id, produto_id, quantidade, preco) VALUES (?, ?, ?, ?)";
                    try (PreparedStatement stmtProduto = connection.prepareStatement(sqlProduto)) {
                        stmtProduto.setInt(1, idPedido);
                        stmtProduto.setInt(2, produtoPedido.getIdProduto());
                        stmtProduto.setInt(3, produtoPedido.getQuantidade());
                        stmtProduto.setDouble(4, produtoPedido.getPreco());

                        stmtProduto.executeUpdate();

                        // Atualizar o estoque
                        pgProdutoDAO.updateStock(produtoPedido.getIdProduto(), produtoPedido.getQuantidade());
                    }
                }

                // Commit na transação
                connection.commit();
                return true;

            } catch (SQLException e) {
                connection.rollback();
                e.printStackTrace();
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    private double calcularValorTotal(List<PedidoProduto> produtos) {
        double total = 0;
        for (PedidoProduto produto : produtos) {
            total += produto.getQuantidade() * produto.getPreco();
        }
        System.out.println(total);
        return total;
    }
}
