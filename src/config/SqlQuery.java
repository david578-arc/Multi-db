package config;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;

public class SqlQuery {
    private final Properties queries;

    public SqlQuery(String propertiesPath) throws IOException {
        this.queries = new Properties();
        Path path = Path.of(propertiesPath);
        try (FileInputStream inputStream = new FileInputStream(path.toFile())) {
            this.queries.load(inputStream);
        }
    }

    public String get(String key) {
        String sql = queries.getProperty(key);
        if (sql == null) {
            throw new IllegalArgumentException("SQL query not found for key: " + key);
        }
        return sql;
    }
}
