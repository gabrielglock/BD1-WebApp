package com.bd.springweb.dao;

import com.bd.springweb.model.Loja;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Repository
public class PgLojaDAO implements LojaDAO {

    private final DataSource dataSource;

    public PgLojaDAO(DataSource dataSource) {
        this.dataSource = dataSource;

    }

    private static final String CREATE_QUERY =
            "INSERT INTO webapp.loja(cnpj, nome, contato, hr_funcionamento, endereco, email) " +
                    "VALUES(?, ?, ?, ?, ?, ?);";

    private static final String READ_QUERY =
            "SELECT cnpj, nome, contato, hr_funcionamento, endereco, email " +
                    "FROM webapp.loja " +
                    "WHERE cnpj = ?;";

    private static final String UPDATE_QUERY =
            "UPDATE webapp.loja " +
                    "SET nome = ?, contato = ?, hr_funcionamento = ?, endereco = ?, email = ? " +
                    "WHERE cnpj = ?;";

    private static final String DELETE_QUERY =
            "DELETE FROM webapp.loja " +
            "WHERE cnpj = ?;";


    private static final String LIST_QUERY =
            "SELECT cnpj, nome, contato, hr_funcionamento, endereco, email " +
            "FROM webapp.loja;";




    @Override
    public void create(Loja loja) throws SQLException {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)){
            statement.setString(1, loja.getCnpj());
            statement.setString(2, loja.getNome());
            statement.setString(3, loja.getContato());
            statement.setString(4, loja.getHr_funcionamento());
            statement.setString(5, loja.getEndereco());
            statement.setString(6, loja.getEmail());
            statement.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(PgLojaDAO.class.getName()).log(Level.SEVERE, "DAO", e);
            if(e.getMessage().contains("uq_loja_cnpj")){
                throw new SQLException("CNPJ já existe no banco de dados");
            } else if(e.getMessage().contains("not-null")){
                throw new SQLException("Erro ao inserir loja, existem campos em branco.");
            } else {
                throw new SQLException("Erro ao inserir loja.");
            }

        }
    }
    @Override
    public Loja read(Integer cnpj) throws SQLException {
        Loja loja = new Loja();
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(READ_QUERY)){
            statement.setInt(1, cnpj);
            try(ResultSet resultSet = statement.executeQuery()){
                if(resultSet.next()){
                    loja.setCnpj(cnpj.toString());
                    loja.setNome(resultSet.getString("nome"));
                    loja.setContato(resultSet.getString("contato"));
                    loja.setHr_funcionamento(resultSet.getString("hr_funcionamento"));
                    loja.setEndereco(resultSet.getString("endereco"));
                    loja.setEmail(resultSet.getString("email"));

                } else {
                    throw new SQLException("Erro ao buscar loja: Loja não encontrada.");
                }
            }
            catch (SQLException e){
                Logger.getLogger(PgLojaDAO.class.getName()).log(Level.SEVERE, "DAO", e);
                if(e.getMessage().equals("Erro ao buscar loja: Loja não encontrada.")) {
                    throw e;
                }
                else {
                    throw new SQLException("Erro ao buscar loja.");
                }

            }
            return loja;
        }
    }

    @Override
    public Loja getFromString(String cnpj) throws SQLException {
        Loja loja = new Loja();
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(READ_QUERY)){
            statement.setString(1, cnpj);
            try(ResultSet resultSet = statement.executeQuery()){
                if(resultSet.next()){
                    loja.setCnpj(cnpj);
                    loja.setNome(resultSet.getString("nome"));
                    loja.setContato(resultSet.getString("contato"));
                    loja.setHr_funcionamento(resultSet.getString("hr_funcionamento"));
                    loja.setEndereco(resultSet.getString("endereco"));
                    loja.setEmail(resultSet.getString("email"));

                } else {
                    throw new SQLException("Erro ao buscar loja: Loja não encontrada.");
                }
            }
            catch (SQLException e){
                Logger.getLogger(PgLojaDAO.class.getName()).log(Level.SEVERE, "DAO", e);
                if(e.getMessage().equals("Erro ao buscar loja: Loja não encontrada.")) {
                    throw e;
                }
                else {
                    throw new SQLException("Erro ao buscar loja.");
                }

            }
            return loja;
        }

    }

    @Override
    public void update(Loja loja) throws SQLException {

        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)){
            statement.setString(1, loja.getNome());
            statement.setString(2, loja.getContato());
            statement.setString(3, loja.getHr_funcionamento());
            statement.setString(4, loja.getEndereco());
            statement.setString(5, loja.getEmail());
            statement.setString(6, loja.getCnpj());
            if(statement.executeUpdate() < 1){
                throw new SQLException("Erro ao atualizar loja.");
            }

        } catch (SQLException e) {
            Logger.getLogger(PgLojaDAO.class.getName()).log(Level.SEVERE, "DAO", e);
            if (e.getMessage().equals("Erro ao atualizar loja.")) {
                throw e;
            }
            else if(e.getMessage().contains("not-null")){
                throw new SQLException("Erro ao atualizar loja: Existem campos em branco.");
            } else {
                throw new SQLException("Erro ao atualizar loja.");
            }
        }


    }


    @Override
    public void deleteFromString(String cpnj) throws SQLException{
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)){
            statement.setString(1, cpnj);
            if(statement.executeUpdate() < 1){
                throw new SQLException("Erro ao deletar loja: Loja não encontrada.");
            }
        } catch (SQLException e) {
            Logger.getLogger(PgLojaDAO.class.getName()).log(Level.SEVERE, "DAO", e);
            if(e.getMessage().equals("Erro ao deletar loja: Loja não encontrada.")) {
                throw e;
            }else {
                throw new SQLException("Erro ao deletar loja.");
            }
        }

    }
    @Override
    public void delete(Integer cpnj) throws SQLException{
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)){
            statement.setInt(1, cpnj);
            if(statement.executeUpdate() < 1){
                throw new SQLException("Erro ao deletar loja: Loja não encontrada.");
            }
        } catch (SQLException e) {
            Logger.getLogger(PgLojaDAO.class.getName()).log(Level.SEVERE, "DAO", e);
            if(e.getMessage().equals("Erro ao deletar loja: Loja não encontrada.")) {
                throw e;
            }else {
                throw new SQLException("Erro ao deletar loja.");
            }
        }

    }
    @Override
    public List<Loja> readAll() throws SQLException{
        List<Loja> lojaList = new ArrayList<>();
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(LIST_QUERY);
            ResultSet result = statement.executeQuery()){
            while(result.next()){
                Loja loja = new Loja();
                loja.setCnpj(result.getString("cnpj"));
                loja.setNome(result.getString("nome"));
                loja.setContato(result.getString("contato"));
                loja.setHr_funcionamento(result.getString("hr_funcionamento"));
                loja.setEndereco(result.getString("endereco"));
                loja.setEmail(result.getString("email"));
                lojaList.add(loja);
            }
        } catch (SQLException e) {
            Logger.getLogger(PgLojaDAO.class.getName()).log(Level.SEVERE, "DAO", e);
            throw new SQLException("Erro ao listar lojas.");
        }
        return lojaList;

    }
}
