package com.bd.springweb.dao;

import com.bd.springweb.dto.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


@Repository
public class PgRelatorioDAO {



    private final DataSource dataSource;

    public PgRelatorioDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // Metodo específico para consultar o total de vendas por loja em um período
    public List<TotalVendasPorLojaDTO> getTotalVendasPorLoja(Date dataInicio, Date dataFim) throws SQLException {
        String query = "SELECT l.nome AS loja, SUM(p.valor_total) AS total_vendas " +
                "FROM webapp.pedido p " +
                "JOIN webapp.loja l ON p.loja_cnpj = l.cnpj " +
                "WHERE p.data BETWEEN ? AND ? " +
                "GROUP BY l.nome " +
                "ORDER BY total_vendas DESC";

        List<TotalVendasPorLojaDTO> resultados = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setDate(1, dataInicio);
            stmt.setDate(2, dataFim);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                resultados.add(new TotalVendasPorLojaDTO(rs.getString("loja"), rs.getDouble("total_vendas")));
            }
        }
        return resultados;
    }

    public List<VendasPorMesDTO> getVendasPorMes(String cnpj){
        String query = "SELECT DATE_TRUNC('month', p.data) AS mes, " +
                "SUM(p.valor_total) AS total_vendas " +
                "FROM webapp.pedido p " +
                "WHERE p.loja_cnpj = ? " +
                "GROUP BY mes " +
                "ORDER BY mes";

        List<VendasPorMesDTO> resultados = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, cnpj);  // Filtra pelo CNPJ da loja
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                // Converte a data para o mês e ano e cria um DTO
                Date mes = rs.getDate("mes");
                double totalVendas = rs.getDouble("total_vendas");

                resultados.add(new VendasPorMesDTO(mes, totalVendas));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resultados;
    }

    public List<TicketMedioPorMesDTO> getTicketMedioPorMes(String cnpj) throws SQLException {
        String query = "SELECT DATE_TRUNC('month', p.data) AS mes, " +
                "AVG(p.valor_total) AS ticket_medio " +
                "FROM webapp.pedido p " +
                "WHERE p.loja_cnpj = ? " +
                "GROUP BY mes " +
                "ORDER BY mes";

        List<TicketMedioPorMesDTO> resultados = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, cnpj);  // Filtra pelo CNPJ da loja
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Date mes = rs.getDate("mes");
                double ticketMedio = rs.getDouble("ticket_medio");

                resultados.add(new TicketMedioPorMesDTO(mes, ticketMedio));
            }
        }
        return resultados;
    }
    public List<TicketMedioPorLojaDTO> getTicketMedioPorLoja() throws SQLException {
        String query = "SELECT p.loja_cnpj, l.nome, AVG(p.valor_total) AS ticket_medio " +
                "FROM webapp.pedido p " +
                "JOIN webapp.loja l ON p.loja_cnpj = l.cnpj " +
                "GROUP BY l.nome, p.loja_cnpj " +
                "ORDER BY ticket_medio DESC;";
        List<TicketMedioPorLojaDTO> resultados = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String cnpj = rs.getString("loja_cnpj");
                String nome = rs.getString("nome");
                double ticketMedio = rs.getDouble("ticket_medio");

                resultados.add(new TicketMedioPorLojaDTO(cnpj, nome, ticketMedio));
            }
        }
        return resultados;
    }
    public List<GastosClienteDTO> getClientesQueMaisGastam() throws SQLException {
        String query = "SELECT c.cpf, c.nome, SUM(p.valor_total) AS total_gasto " +
                "FROM webapp.pedido p " +
                "JOIN webapp.cliente c ON p.cliente_cpf = c.cpf " +
                "GROUP BY c.cpf, c.nome " +
                "ORDER BY total_gasto DESC";

        List<GastosClienteDTO> resultados = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String cpf = rs.getString("cpf");
                String nome = rs.getString("nome");
                double totalGasto = rs.getDouble("total_gasto");

                resultados.add(new GastosClienteDTO(cpf, nome, totalGasto));
            }
        }
        return resultados;
    }
    public ProdutoPreferidoDTO getProdutoPreferidoPorCliente(String cpf) throws SQLException {
        String query = "SELECT p.nome AS nome_produto, SUM(pp.quantidade) AS total_comprado " +
                "FROM webapp.pedido_produto pp " +
                "JOIN webapp.pedido pe ON pp.pedido_id = pe.id " +
                "JOIN webapp.produto p ON pp.produto_id = p.id " +
                "WHERE pe.cliente_cpf = ? " +
                "GROUP BY p.nome " +
                "ORDER BY total_comprado DESC " +
                "LIMIT 1";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, cpf);  // Filtra pelo CPF do cliente
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String nomeProduto = rs.getString("nome_produto");
                int totalComprado = rs.getInt("total_comprado");

                return new ProdutoPreferidoDTO(nomeProduto, totalComprado);
            }
        }
        return null;
    }
    public TotalGastoClienteDTO getTotalGastoPorCliente(String cpf) throws SQLException {
        String query = "SELECT c.cpf, c.nome, SUM(p.valor_total) AS total_gasto " +
                "FROM webapp.pedido p " +
                "JOIN webapp.cliente c ON p.cliente_cpf = c.cpf " +
                "WHERE c.cpf = ? " +
                "GROUP BY c.cpf, c.nome";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, cpf);  // Filtra pelo CPF do cliente
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String nome = rs.getString("nome");
                double totalGasto = rs.getDouble("total_gasto");

                return new TotalGastoClienteDTO(cpf, nome, totalGasto);
            }
        }
        return null;

    }
}
