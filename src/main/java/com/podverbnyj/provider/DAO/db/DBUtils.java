package com.podverbnyj.provider.DAO.db;


import org.apache.logging.log4j.*;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DBUtils {

    private static final Logger log = LogManager.getLogger(DBUtils.class);

    private static DBUtils instance;

    public static synchronized DBUtils getInstance() {
        if (instance == null) {
            instance = new DBUtils();
        }
        return instance;
    }

    private DataSource ds;

    private DBUtils() {
        try {
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            ds = (DataSource) envContext.lookup("jdbc/Provider");
        } catch (NamingException ex) {

            log.error("Cannot obtain a data source", ex);
            throw new IllegalStateException(ex);
        }
    }

    public Connection getConnection() {
        Connection con;
        try {
            con = ds.getConnection();
        } catch (SQLException ex) {
            log.error("Cannot obtain a connection", ex);
            throw new IllegalStateException(ex);
        }
        return con;
    }

}
