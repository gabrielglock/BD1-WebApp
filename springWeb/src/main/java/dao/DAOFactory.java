package dao;

import jdbc.ConnectionFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public abstract class DAOFactory implements AutoCloseable{

    protected Connection connection;

    public static DAOFactory getInstance() throws IOException, ClassNotFoundException, SQLException {
        Connection connection = ConnectionFactory.getInstance().getConnection();
        DAOFactory daoFactory;

        if(ConnectionFactory.getDbServer().equals("postgresql")){
            daoFactory = new PgDAOFactory(connection);
        }
        else {
            throw new RuntimeException("Servidor de banco de dados não suportado.");

        }
        return daoFactory;
    }
    public void beginTransaction() throws SQLException{
        try {
            connection.setAutoCommit(false);


        } catch (SQLException e) {
            System.err.println(e.getMessage());
            throw new SQLException("Erro ao abrir transação.");
        }
    }

    public void commitTransaction() throws SQLException{
        try {
            connection.commit();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            throw new SQLException("Erro ao finalizar a transação.");

        }
    }
    public void rollbackTransaction() throws SQLException{
        try {
            connection.rollback();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            throw new SQLException("Erro ao executar transação.");

        }
    }
    public void closeConnection() throws SQLException{
        try {
            connection.close();

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            throw new SQLException("Erro ao fechar a conexão do banco de dados.");

        }
    }
    public void endTransaction() throws SQLException{
        try{
            connection.setAutoCommit(true);

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            throw new SQLException("Erro ao finalizar a transação.");
        }
    }


    @Override
    public void close() throws SQLException {
        closeConnection();
    }
}
