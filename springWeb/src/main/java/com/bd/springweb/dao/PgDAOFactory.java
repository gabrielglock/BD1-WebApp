package com.bd.springweb.dao;

import java.sql.Connection;

public class PgDAOFactory extends DAOFactory {


    public PgDAOFactory(Connection connection) {
        this.connection = connection;
    }


}
