package ua.com.juja.sqlcmd.model;

public interface DatabaseManager {


    DataSet[] getTableData(String tableName);

    String[] getAllTablesOfDataBase();

    void connectToDataBase(String database, String user, String password);

    void update(String tableName, int id, DataSet data);

    int getColumnCount(String tableName);

    String[] getColumnNames(String tableName);

    void insert(String tableName, DataSet data);

    String clear(String tableName);

    String getDatabaseName();

    String getUserName();

    String getUserPassword();

    void setDatabaseName(String databaseName);

    void setUserName(String userName);

    void setUserPassword(String userPassword);

    String getVersionDatabase();

    boolean isConnected();


}
