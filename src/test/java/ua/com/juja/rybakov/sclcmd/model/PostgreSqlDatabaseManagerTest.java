package ua.com.juja.rybakov.sclcmd.model;


import ua.com.juja.rybakov.sqlcmd.model.DatabaseManager;
import ua.com.juja.rybakov.sqlcmd.model.PostgreSqlDatabaseManager;



public class PostgreSqlDatabaseManagerTest extends DatabaseManagerTest {

    @Override
    public DatabaseManager getDatabaseManager() {
        DatabaseManager manager = new PostgreSqlDatabaseManager();
        manager.setDatabaseName("pgsqlcmd");
        manager.setUserName("postgres");
        manager.setUserPassword("postgres");
        return manager;
    }
}
