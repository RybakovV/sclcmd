package ua.com.juja.rybakov.sclcmd.model;

import ua.com.juja.rybakov.sqlcmd.model.DatabaseManager;
import ua.com.juja.rybakov.sqlcmd.model.MysqlDatabaseManager;

import static org.junit.Assert.assertEquals;


public class MysqlDatabaseManagerTest extends DatabaseManagerTest {

    @Override
    public DatabaseManager getDatabaseManager() {
        DatabaseManager manager = new MysqlDatabaseManager();
        manager.setDatabaseName("mysqlcmd");
        manager.setUserName("root");
        manager.setUserPassword("root");
        return manager;
    }

}
