package com.bd.springweb.dao;

import com.bd.springweb.model.Cliente;

import com.bd.springweb.model.EnderecoCliente;
import org.springframework.stereotype.Repository;


import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
@Repository
public class PgClienteDAO implements ClienteDAO {

    private final DataSource dataSource;

    public PgClienteDAO(DataSource dataSource) {
        this.dataSource = dataSource;

    }
    private static final String CREATE_QUERY =
            "INSERT INTO webapp.cliente(cpf, nome, contato) " +
                    "VALUES(?, ?, ?);";

    private static final String READ_QUERY =
            "SELECT cpf, nome, contato " +
                    "FROM webapp.cliente " +
                    "WHERE cpf = ?;";

    private static final String UPDATE_QUERY =
            "UPDATE webapp.cliente " +
                    "SET nome = ?, contato = ? " +
                    "WHERE cpf = ?;";

    private static final String DELETE_QUERY =
            "DELETE FROM webapp.cliente " +
                    "WHERE cpf = ?;";


    private static final String LIST_QUERY =
            "SELECT cpf, nome, contato " +
                    "FROM webapp.cliente;";

    private static final String CREATE_ENDERECO_QUERY =
            "INSERT INTO webapp.endereco_cliente (cliente_cpf, endereco) " +
                    "VALUES (?, ?);";
    private static final String LISTAR_ENDERECOS_POR_CLIENTE_QUERY =
            "SELECT cliente_cpf, endereco FROM webapp.endereco_cliente WHERE cliente_cpf = ?;";

    @Override
    public Cliente getFromString(String cpf) throws SQLException {
        Cliente cliente = new Cliente();
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(READ_QUERY)){
            statement.setString(1, cpf);
            try(ResultSet resultSet = statement.executeQuery()){
                if(resultSet.next()){
                    cliente.setCpf(cpf);
                    cliente.setNome(resultSet.getString("nome"));
                    cliente.setContato(resultSet.getString("contato"));


                } else {
                    throw new SQLException("Erro ao buscar cliente: Cliente não encontrada.");
                }
            }
            catch (SQLException e){
                Logger.getLogger(PgClienteDAO.class.getName()).log(Level.SEVERE, "DAO", e);
                if(e.getMessage().equals("Erro ao buscar cliente: Cliente não encontrada.")) {
                    throw e;
                }
                else {
                    throw new SQLException("Erro ao buscar cliente.");
                }

            }
            return cliente;
        }
    }
    @Override
    public void addEnderecoCliente(EnderecoCliente enderecoCliente) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(CREATE_ENDERECO_QUERY)) {

            statement.setString(1, enderecoCliente.getCpf());
            statement.setString(2, enderecoCliente.getEndereco());
            statement.executeUpdate();
        }
    }

    @Override
    public void deleteFromString(String cpf) throws SQLException {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)){
            statement.setString(1, cpf);
            if(statement.executeUpdate() < 1){
                throw new SQLException("Erro ao deletar Cliente: cliente não encontrado.");
            }
        } catch (SQLException e) {
            Logger.getLogger(PgClienteDAO.class.getName()).log(Level.SEVERE, "DAO", e);
            if(e.getMessage().equals("Erro ao deletar cliente: cliente não encontrado.")) {
                throw e;
            }else {
                throw new SQLException("Erro ao deletar cliente.");
            }
        }

    }

    @Override
    public void create(Cliente cliente) throws SQLException {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)){
            statement.setString(1, cliente.getCpf());
            statement.setString(2, cliente.getNome());
            statement.setString(3, cliente.getContato());

            statement.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(PgClienteDAO.class.getName()).log(Level.SEVERE, "DAO", e);
            if(e.getMessage().contains("uq_cliente_cpf")){
                throw new SQLException("CPF já existe no banco de dados");
            } else if(e.getMessage().contains("not-null")){
                throw new SQLException("Erro ao inserir Cliente, existem campos em branco.");
            } else {
                throw new SQLException("Erro ao inserir cliente.");
            }

        }

    }
    @Override
    public List<EnderecoCliente> listarEnderecoPorCliente(String cpfCliente) throws SQLException {


        List<EnderecoCliente> enderecos = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(LISTAR_ENDERECOS_POR_CLIENTE_QUERY)) {

            statement.setString(1, cpfCliente);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    EnderecoCliente endereco = new EnderecoCliente();
                    endereco.setCpf(rs.getString("cliente_cpf"));
                    endereco.setEndereco(rs.getString("endereco"));
                    enderecos.add(endereco);
                }
            }
        }

        return enderecos;
    }

    @Override
    public Cliente read(Integer id) throws SQLException {
        return null;
    }

    @Override
    public void update(Cliente cliente) throws SQLException {

    }

    @Override
    public void delete(Integer id) throws SQLException {

    }

    @Override
    public List<Cliente> readAll() throws SQLException {
        List<Cliente> clienteList = new ArrayList<>();
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(LIST_QUERY);
            ResultSet result = statement.executeQuery()){
            while(result.next()){
                Cliente cliente = new Cliente();
                cliente.setCpf(result.getString("cpf"));
                cliente.setNome(result.getString("nome"));
                cliente.setContato(result.getString("contato"));

                clienteList.add(cliente);
            }
        } catch (SQLException e) {
            Logger.getLogger(PgLojaDAO.class.getName()).log(Level.SEVERE, "DAO", e);
            throw new SQLException("Erro ao listar clientes.");
        }
        return clienteList;
    }
    @Override
    public Cliente listarClienteComEnderecos(String cpfCliente) throws SQLException {
        Cliente cliente = null;

        // Primeiro, buscar os dados do cliente
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statementCliente = connection.prepareStatement(READ_QUERY)) {

            statementCliente.setString(1, cpfCliente);

            try (ResultSet rsCliente = statementCliente.executeQuery()) {
                if (rsCliente.next()) {
                    cliente = new Cliente();
                    cliente.setCpf(rsCliente.getString("cpf"));
                    cliente.setNome(rsCliente.getString("nome"));
                    cliente.setContato(rsCliente.getString("contato"));

                    // Agora, buscar os endereços do cliente
                    cliente.setEnderecosCliente(listarEnderecoPorCliente(cpfCliente));
                }
            }
        }

        return cliente;
    }
}
