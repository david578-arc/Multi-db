package app;

import config.DBConfig;
import config.SqlQuery;
import connection.ConnectionManager;
import query.QueryHandler;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.Map;

public class DBMain {
    public static void main(String[] args) throws Exception {
    	//System.out.print("bg");
        DBConfig dbConfig = DBConfig.loadFromXml("src/config/db-config.xml");
        ConnectionManager connectionManager = new ConnectionManager(dbConfig);
        QueryHandler queryHandler = new QueryHandler(connectionManager);
        SqlQuery sqlQueries = new SqlQuery("src/config/sql.properties");

        String createTableSql = sqlQueries.get("fourthDB.createTable");
        String insertSql = sqlQueries.get("fourthDB.insertEmployee");
        String selectSql = sqlQueries.get("fourthDB.selectAllEmployees");

        queryHandler.executeUpdate("fourthDB",createTableSql);
        queryHandler.executeUpdate("fourthDB",insertSql,"David","Software development",65000.0);
        queryHandler.executeUpdate("fourthDB",insertSql,"sam","Ml",78000.0);
        DBConfig.DbEntry db = dbConfig.getDbEntry("fourthDB");
      
        List<Map<String, Object>> rows = queryHandler.executeSelect("fourthDB", selectSql);
        System.out.println("Query results:");
        for (Map<String, Object> row : rows) {
            System.out.println(row);
        }
    }
}
