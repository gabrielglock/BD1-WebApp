package com.bd.springweb.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class PgConnectionFactory extends ConnectionFactory {

    private String dbHost;
    private String dbPort;
    private String dbName;
    private String dbUser;
    private String dbPassword;


    public PgConnectionFactory(){

    }
    public void readProperties()throws IOException{
        Properties properties = new Properties();
        try {
            InputStream input = this.getClass().getClassLoader().getResourceAsStream("propertiesPath");
            properties.load(input);

            dbHost = properties.getProperty("host");
            dbPort = properties.getProperty("port");
            dbName = properties.getProperty("name");
            dbPassword = properties.getProperty("password");

        } catch (IOException e){
            System.err.println(e.getMessage());

            throw new IOException("Erro ao obter informações do banco de dados.");

        }



    }
    @Override
    public Connection getConnection() throws SQLException, IOException, ClassNotFoundException {
        Connection connection = null;

        try {
            Class.forName("org.postgresql.Driver");
            readProperties();
            String url = "jdbc:postgresql://" + dbHost + ":" + dbPort + "/" + dbName;
            connection = DriverManager.getConnection(url, dbUser, dbPassword);
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
            throw new SQLException("Erro ao conectar com o banco de dados.");
        }
        return connection;
    }

}
