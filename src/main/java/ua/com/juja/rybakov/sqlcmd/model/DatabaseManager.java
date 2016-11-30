package ua.com.juja.rybakov.sqlcmd.model;

import java.sql.SQLException;

public interface DatabaseManager {


    DataSet[] getTableData(String tableName);

    String[] getAllTablesOfDataBase();

    /**
     *
     * @param database
     * @param user
     * @param password
     */

    void connectToDataBase(String database, String user, String password);

    void update(String tableName, int id, DataSet data);

    int getColumnCount(String tableName);

    String[] getColumnNames(String tableName);

    void insert(String tableName, DataSet data);

    void clear(String tableName) throws SQLException;

    String getDatabaseName();

    String getUserName();

    String getUserPassword();

    void setDatabaseName(String databaseName);

    void setUserName(String userName);

    void setUserPassword(String userPassword);

    String getVersionDatabase();

    boolean isConnected();


}
