package ua.com.juja.sqlcmd.model;

import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;

import java.sql.*;
import java.util.Arrays;

public class MysqlDatabaseManager implements DatabaseManager {

    private Connection connection;
    private String databaseName;
    private String userName;
    private String userPassword;

    @Override
    public DataSet[] getTableData(String tableName) {
        int tableSize = getCountRows(tableName);
        DataSet[] result = new DataSet[tableSize];
        if (tableSize > 0) {
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName)) {
                ResultSetMetaData resultSetMetaData;
                if (resultSet != null) {
                    resultSetMetaData = resultSet.getMetaData();
                    int index = 0;
                    int columnCount = getColumnCount(tableName);
                    while (resultSet.next()) {
                        DataSet dataSet = new DataSet();
                        for (int i = 1; i <= columnCount; i++) {
                            dataSet.put(resultSetMetaData.getColumnName(i), resultSet.getObject(i));
                        }
                        result[index] = dataSet;
                        index++;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }


    private int getCountRows(String tableName) {
        int countRows = 0;
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM `" + tableName + "`")) {
            resultSet.next();
            countRows = resultSet.getInt(1);
        } catch (SQLException e) {
            //do nothing
        }
        return countRows;
    }

    @Override
    public String[] getAllTablesOfDataBase() {
        int countTables = 0;
        String[] tables = new String[100];
        try (Statement statement = connection.createStatement()) {
            if (statement != null) {
                try (ResultSet resultSet = statement.executeQuery("SHOW TABLES")) {
                    while (resultSet.next()) {
                        tables[countTables] = resultSet.getString(1);
                        countTables++;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tables = Arrays.copyOf(tables, countTables, String[].class);
        Arrays.sort(tables);
        return tables;
    }

    @Override
    public void connectToDataBase(String database, String user, String password) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Please register you JDBC driver", e);
        }
        try {
            String url = "jdbc:mysql://127.0.0.1:3306/" + database + "?useSSL=false";
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException eSQLException) {
            try {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException eClassNotFoundPostgres) {
                throw new RuntimeException("Please register you JDBC driver", eClassNotFoundPostgres);
            }
            try {
                String url = "jdbc:postgresql://localhost/" + database;
                connection = DriverManager.getConnection(url, user, password);
            } catch (SQLException ePostgresException) {
                connection = null;
                throw new RuntimeException(String.format("Can't connect to Database: %s by User: %s or Password: %s. ", database, user, password), ePostgresException);
            }
        }
    }

    @Override
    public void update(String tableName, int id, DataSet data) {
        try (Statement statement = connection.createStatement()) {
            String[] columnName = data.getColumnNames();
            Object[] value = data.getValues();
            String sql = "UPDATE " + tableName + " SET ";
            for (int i = 0; i < columnName.length; i++) {
                sql += columnName[i] + " = '" + value[i] + "', ";
            }
            sql = sql.substring(0, sql.length() - 2);
            sql += " WHERE id = " + id;
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getColumnCount(String tableName) {
        int columnCount = 0;
        ResultSetMetaData resultSetMetaData;
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM `" + tableName + "`")) {
            resultSetMetaData = resultSet.getMetaData();
            columnCount = resultSetMetaData.getColumnCount();
        } catch (SQLException e) {
            return columnCount;
        }
        return columnCount;
    }


    @Override
    public String[] getColumnNames(String tableName) {
        int columnCount = getColumnCount(tableName);
        String[] columnNames = new String[columnCount];
        if (columnCount > 0) {
            ResultSetMetaData resultSetMetaData;
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName)) {
                resultSetMetaData = resultSet.getMetaData();
                for (int i = 0; i < columnCount; i++) {
                    columnNames[i] = resultSetMetaData.getColumnName(i + 1);
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
            String columnNameSet = Arrays.toString(data.getColumnNames());
            columnNameSet = columnNameSet.substring(1, columnNameSet.length() - 1);
            String sql = "INSERT INTO " + tableName + " (" + columnNameSet + ") VALUES (" + valueSet + ")";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException("", e);
        }
    }

    @Override
    public void clear(String tableName) throws SQLException {
        String sql = "DELETE FROM " + tableName;
        try (Statement statement = connection.createStatement()) {
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
            sql = "SHOW VARIABLES LIKE \"version_comment\"";
            resultSet = statement.executeQuery(sql);
            resultSet.next();
            result += resultSet.getString(2);
        } catch (SQLException e) {
            try {
                statement = connection.createStatement();
                sql = "SHOW \"event_source\"";
                resultSet = statement.executeQuery(sql);
                resultSet.next();
                result += resultSet.getString(1);
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
}
