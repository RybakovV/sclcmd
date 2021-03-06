package ua.com.juja.rybakov.sqlcmd.model;

import ua.com.juja.rybakov.sqlcmd.controller.Sign;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public interface DatabaseManager {

    List<DataSet> getTableData(String tableName);

    Set<String> getAllTablesOfDataBase();

    void connectToDataBase(Sign sign);

    void update(String tableName, int id, DataSet data);

    int getColumnCount(String tableName);

    Set<String> getColumnNames(String tableName);

    void insert(String tableName, DataSet data);

    void delete(String tableName, DataSet data);

    void clear(String tableName) throws SQLException;

    String getDatabaseName();

    String getUserName();

    String getUserPassword();

    void setDatabaseName(String databaseName);

    void setUserName(String userName);

    void setUserPassword(String userPassword);

    String getVersionDatabase();

    boolean isConnected();

    Set<String> getAllDataBases();
}
