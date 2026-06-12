package config;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DBConfig {
    public static class DbEntry {
        private final String name;
        private final String driverClass;
        private final String url;
        private final String username;
        private final String password;

        public DbEntry(String name, String driverClass, String url, String username, String password) {
            this.name = name;
            this.driverClass = driverClass;
            this.url = url;
            this.username = username;
            this.password = password;
        }

        public String getName() {
            return name;
        }

        public String getDriverClass() {
            return driverClass;
        }

        public String getUrl() {
            return url;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }
    }

    private final Map<String, DbEntry> dbEntries;

    private DBConfig(Map<String, DbEntry> dbEntries) {
        this.dbEntries = Collections.unmodifiableMap(dbEntries);
    }
    public static DBConfig loadFromXml(String configPath) throws Exception {
        File configFile = new File(configPath);
        System.out.println(configFile.getAbsolutePath());
        if (!configFile.exists()) {
            throw new IllegalArgumentException("DB config file not found: " + configFile.getAbsolutePath());
        }
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(configFile);
        document.getDocumentElement().normalize();
        NodeList dbNodes = document.getElementsByTagName("db");
        Map<String, DbEntry> entries = new HashMap<>();

        for (int i = 0; i < dbNodes.getLength(); i++) {
            Node dbNode = dbNodes.item(i);
            if (dbNode.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            Element dbElement = (Element) dbNode;
            String name = dbElement.getAttribute("name");
            if (name == null || name.isEmpty()) {
                throw new IllegalArgumentException("Each <db> element must have a non-empty name attribute.");
            }
            String driver = getTextContent(dbElement, "driver");
            String url = getTextContent(dbElement, "url");
            String username = getTextContent(dbElement, "username");
            String password = getTextContent(dbElement, "password");

            entries.put(name, new DbEntry(name, driver, url, username, password));
        }
        return new DBConfig(entries);
    }
    
    public DbEntry getDbEntry(String name) {
        if (!dbEntries.containsKey(name)) {
            throw new IllegalArgumentException("Unknown database name: " + name);
        }
        return dbEntries.get(name);
    }
    public Set<String> getDbNames() {
        return dbEntries.keySet();
    }
    private static String getTextContent(Element parent, String tagName) {
        NodeList nodes = parent.getElementsByTagName(tagName);
        if (nodes.getLength() == 0) {
            return "";
        }
        return nodes.item(0).getTextContent().trim();
    }
}
