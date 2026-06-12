package connection;

import config.DBConfig;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

    private final DBConfig dbConfig;

    public ConnectionManager(DBConfig dbConfig) {
        this.dbConfig = dbConfig;
    }

    public Connection getConnection(String dbName) throws SQLException {

        DBConfig.DbEntry entry = dbConfig.getDbEntry(dbName);

        System.out.println("Driver: " + entry.getDriverClass());
        System.out.println("URL: " + entry.getUrl());

        Connection conn = DriverManager.getConnection(
                entry.getUrl(),
                entry.getUsername(),
                entry.getPassword());

        if (conn != null) {
            System.out.println("Connected successfully");
        }

        return conn;
    }
}