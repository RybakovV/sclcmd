package ua.com.juja.rybakov.sqlcmd.model;

import ua.com.juja.rybakov.sqlcmd.controller.Sign;

import java.sql.*;
import java.util.*;


public class PostgreSqlDatabaseManager implements DatabaseManager {
    private Connection connection;
    private String databaseName;
    private String userName;
    private String userPassword;
    private Configuration configuration = new Configuration();

    @Override
    public List<DataSet> getTableData(String tableName) {
        int tableSize = getCountRows(tableName);
        List<DataSet> result = new LinkedList<>();
        if (tableSize > 0) {
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery("SELECT * FROM public." + tableName)) {
                result = getDataSetList(tableName, resultSet);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private List<DataSet> getDataSetList(String tableName, ResultSet resultSet) throws SQLException {
        List<DataSet> result = new LinkedList<>();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = getColumnCount(tableName);
        while (resultSet.next()) {
            DataSet dataSet = new DataSetImpl();
            for (int i = 1; i <= columnCount; i++) {
                dataSet.put(metaData.getColumnName(i), resultSet.getObject(i));
            }
            result.add(dataSet);
        }
        return result;
    }

    private int getCountRows(String tableName) {
        int countRows = 0;
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM public." + tableName)) {
            resultSet.next();
            countRows = resultSet.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return countRows;
    }

    @Override
    public Set<String> getAllTablesOfDataBase() {
        Set<String> tables = new TreeSet<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT table_name FROM information_schema.tables WHERE table_schema='public'")) {
            while (resultSet.next()) {
                tables.add(resultSet.getString("table_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tables;
    }

    @Override
    public void connectToDataBase(Sign sign) {
        String database =  sign.getDatabaseName();
        String user = sign.getUserName();
        String password = sign.getUserPassword();
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Please register you JDBC driver", e);
        }
        try {
            String url = "jdbc:postgresql://" + configuration.getPostgreSqlServer() + "/" + database;
            connection = DriverManager.getConnection(url, user, password);

        } catch (SQLException e1) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException eClassNotFoundMySQL) {
                throw new RuntimeException("Please register you JDBC driver", eClassNotFoundMySQL);
            }
            try {
                String url = "jdbc:mysql://" + configuration.getMysqlServer() + ":"
                        + configuration.getMysqlPort() + "/"
                        + database + "?useSSL=" + configuration.getMysqlUseSsl();

                connection = DriverManager.getConnection(url, user, password);

            } catch (SQLException e) {
                connection = null;
                throw new RuntimeException(String.format("Can't connect to Database: %s by User: %s or Password: %s. ",
                        database, user, password), e);
            }
        }
    }

    @Override
    public void update(String tableName, int id, DataSet data) {
        try (Statement statement = connection.createStatement()) {
            String columnNameSet = data.getColumnNames().toString();
            columnNameSet = columnNameSet.substring(1, columnNameSet.length() - 1);
            String valueSet = data.getValuesString();
            String sql = "UPDATE public." + tableName + " SET (" + columnNameSet + ") = (" + valueSet + ") WHERE id = " + id;
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getColumnCount(String tableName) {
        int columnCount = 0;
        ResultSetMetaData metaData;
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM public." + tableName)) {
            metaData = resultSet.getMetaData();
            columnCount = metaData.getColumnCount();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return columnCount;
    }

    @Override
    public Set<String> getColumnNames(String tableName) {
        int columnCount = getColumnCount(tableName);
        Set<String> columnNames = new LinkedHashSet<>();
        if (columnCount > 0) {
            ResultSetMetaData metaData;
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery("SELECT * FROM public." + tableName)) {
                metaData = resultSet.getMetaData();
                for (int i = 0; i < columnCount; i++) {
                    columnNames.add(metaData.getColumnName(i + 1));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return columnNames;
    }

    @Override
    public void insert(String tableName, DataSet data) {
        try (Statement statement = connection.createStatement()) {
            String valueSet = data.getValuesString();
            String columnNames = data.getColumnNames().toString();
            columnNames = columnNames.substring(1, columnNames.length() - 1);
            String sql = "INSERT INTO public." + tableName + "(" + columnNames + ") VALUES (" + valueSet + ")";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException("", e);
        }
    }

    @Override
    public void delete(String tableName, DataSet data) {
        try (Statement statement = connection.createStatement()) {
            String valueSet = data.getValuesString();
            String columnNames = data.getColumnNames().toString();
            columnNames = columnNames.substring(1, columnNames.length() - 1);
            String sql = "DELETE FROM public." + tableName + "WHERE (" + columnNames + ") = (" + valueSet + ")";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException("", e);
        }
    }

    @Override
    public void clear(String tableName) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            String sql = "DELETE FROM public." + tableName;
            statement.executeUpdate(sql);
        }
    }

    @Override
    public String getDatabaseName() {
        return databaseName;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public String getUserPassword() {
        return userPassword;
    }

    @Override
    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    @Override
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    @Override
    public String getVersionDatabase() {
        Statement statement = null;
        String result = "";
        ResultSet resultSet = null;
        String sql;
        try {
            statement = connection.createStatement();
            sql = "SHOW \"event_source\"";
            resultSet = statement.executeQuery(sql);
            resultSet.next();
            result += resultSet.getString(1);
        } catch (SQLException e) {
            try {
                statement = connection.createStatement();
                sql = "SHOW VARIABLES LIKE \"version_comment\"";
                resultSet = statement.executeQuery(sql);
                resultSet.next();
                result += resultSet.getString(2);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result.split(" ")[0];
    }

    @Override
    public boolean isConnected() {
        return connection != null;
    }

    @Override
    public Set<String> getAllDataBases() {
        Set<String> result = new TreeSet<>();
        try (Statement statement = connection.createStatement()) {
            if (statement != null) {
                try (ResultSet resultSet = statement.executeQuery("SELECT datname FROM pg_database\n" +
                        "WHERE datistemplate = false;")) {
                    while (resultSet.next()) {
                        result.add(resultSet.getString(1));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
