package ua.com.juja.sclcmd.model;

import ua.com.juja.sqlcmd.model.DatabaseManager;

import ua.com.juja.sqlcmd.model.PostgresqlDatabaseManager;

import static org.junit.Assert.assertEquals;

/**
 * Created by MEBELBOS-2 on 10.08.2016.
 */
public class PostgresqlDatabaseManagerTest extends DatabaseManagerTest{

    @Override
    public DatabaseManager getDatabaseManager(){
        DatabaseManager postgresqlDatabaseManager = new PostgresqlDatabaseManager();
        postgresqlDatabaseManager.setDatabaseName("pgsqlcmd");
        postgresqlDatabaseManager.setUserName("postgres");
        postgresqlDatabaseManager.setUserPassword("postgres");
        return postgresqlDatabaseManager;
    }
}
