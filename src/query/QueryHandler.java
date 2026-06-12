package query;

import connection.ConnectionManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryHandler {
    private final ConnectionManager connectionManager;

    public QueryHandler(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public List<Map<String, Object>> executeSelect(String dbName, String sql, Object... params)
            throws SQLException, ClassNotFoundException {
        try (Connection connection = connectionManager.getConnection(dbName);
                PreparedStatement statement = connection.prepareStatement(sql)) {
            bindParameters(statement, params);
            try (ResultSet resultSet = statement.executeQuery()) {
                return toRows(resultSet);
            }
        }
    }

    public int executeUpdate(String dbName, String sql, Object... params)
            throws SQLException, ClassNotFoundException {
        try (Connection connection = connectionManager.getConnection(dbName);
                PreparedStatement statement = connection.prepareStatement(sql)) {
            bindParameters(statement, params);
            return statement.executeUpdate();
        }
    }

    private void bindParameters(PreparedStatement statement, Object... params) throws SQLException {
        if(params == null) {
            return;
        }
        for(int i = 0; i < params.length; i++) {
            statement.setObject(i + 1, params[i]);
        }
    }
    private List<Map<String, Object>> toRows(ResultSet resultSet) throws SQLException {
        List<Map<String, Object>> rows = new ArrayList<>();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount(); 
        while (resultSet.next()) {
            Map<String, Object> row = new HashMap<>();
            for (int column = 1; column <= columnCount; column++) {
                row.put(metaData.getColumnLabel(column), resultSet.getObject(column));
            }
            rows.add(row);
        }
        return rows;
    }
}
