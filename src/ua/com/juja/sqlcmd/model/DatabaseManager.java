package ua.com.juja.sqlcmd.model;

/**
 * Created by Rybakov Vitaliy on 12.09.2016.
 */
public interface DatabaseManager {
    String getTableString(String tableName);

    String getStringTableData(String tableName);

    String getHeaderOfTheTable(String tableName);

    DataSet[] getTableData(String tableName);

    String getEmptyTable(String tableName);

    int getMaxColumnSize(String tableName);

    String[] getAllTablesOfDataBase();

    void connectToDataBase(String database, String user, String password);

    void update(String tableName, int id, DataSet data);

    int getColumnCount(String tableName);

    String[] getColumnNames(String tableName);

    void insert(String tableName, DataSet data);

    void clear(String tableName);

    String getDatabaseName();

    String getUserName();

    String getUserPassword();

    void setDatabaseName(String databaseName);

    void setUserName(String userName);

    void setUserPassword(String userPassword);

    String getVersionDatabase();

    boolean isConnected();
}
