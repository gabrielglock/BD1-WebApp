package jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public abstract class ConnectionFactory {
    private static ConnectionFactory instance = null;
    protected static String propertiesPath = "db.properties";
    private static String dbServer;

    protected ConnectionFactory() {

    }
    public static ConnectionFactory getInstance() throws IOException {
        if (instance == null) {
            Properties properties = new Properties();

            try {
                InputStream input = ConnectionFactory.class.getClassLoader().getResourceAsStream(propertiesPath);
                properties.load(input);
                dbServer = properties.getProperty("server");

            } catch (IOException e) {
                System.err.println(e.getMessage());
                throw new IOException("Não foi possivel conseguir as informações do banco de dados em " + propertiesPath);
            }
            if(getDbServer().equals("postgresql")){
                instance = new PgConnectionFactory();
            }
            else{
                throw new RuntimeException("Servidor não suportado.");
            }
        }
        return instance;
    }

    public static String getDbServer() {
        return dbServer;
    }
    public abstract Connection getConnection() throws SQLException, IOException, ClassNotFoundException;
}
