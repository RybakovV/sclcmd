package ua.com.juja.rybakov.sclcmd.model;


import ua.com.juja.rybakov.sqlcmd.model.DatabaseManager;
import ua.com.juja.rybakov.sqlcmd.model.PostgresqlDatabaseManager;



public class PostgresqlDatabaseManagerTest extends DatabaseManagerTest {

    @Override
    public DatabaseManager getDatabaseManager() {
        DatabaseManager postgresqlDatabaseManager = new PostgresqlDatabaseManager();
        postgresqlDatabaseManager.setDatabaseName("pgsqlcmd");
        postgresqlDatabaseManager.setUserName("postgres");
        postgresqlDatabaseManager.setUserPassword("postgres");
        return postgresqlDatabaseManager;
    }
}
